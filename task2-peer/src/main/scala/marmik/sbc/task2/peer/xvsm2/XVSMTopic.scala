package marmik.sbc.task2.peer.xvsm2


import java.util.{Properties}
import org.xvsm.core.{Entry, Tuple, AtomicEntry, ContainerRef}
import marmik.xvsm._
import marmik.sbc.task2.peer._

import org.xvsm.transactions.Transaction
import org.xvsm.selectors._
import org.xvsm.core.aspect._
import org.xvsm.core.notifications.Operation

class XVSMTopic(val elevator: SpaceElevator, val session: XVSMSession, val peer: XVSMPeer, val name: String, var postings: List[XVSMPosting]) extends Topic {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);
  var subscribed = false
  val containerId = "postings" + name
  var notification1: Notification = null
  elevator.localSpace.implicitTransaction()(tx => {
    val container = tx.lookupOrCreateContainer(containerId, Coordinators.postings: _*)
    elevator.capi.addAspect(container.backing, scalaz.javas.List.ScalaList_JavaList(List(LocalIPoint.PreWrite)), new LocalAspect() {
      override def preWrite(p1: ContainerRef, p2: Transaction, entries: java.util.List[Entry], p4: Int, p5: Properties) = {
        entries.get(0) match {
          case t: Tuple => t.getEntryAt(2) match {
            case a: AtomicEntry[String] => a.setValue(censor(a.getValue))
          }
        }
      }
    })
  })

  def censor(content: String) = {
    var censored = content
    for (badword <- session.badwords)
      censored = censored.replace(badword, "ZENSUR")
    censored
  }


  override def refresh() {
    peer.space.transaction()(tx => {
      val postingContainer = tx.container(containerId)
      val postingTupels = postingContainer.read[(String, String, String, String, String)](0, new RandomSelector(Selector.CNT_ALL))
      postings = postingTupels.map(_ match {
        case (author: String, subject: String, content: String, uuid: String, parentUUID: String) => {
          val pUUID = if (parentUUID == "") null else parentUUID
          new XVSMPosting(elevator, peer, this, author, subject, content, List(), uuid, pUUID)
        }
      }).toList
      val pmap = Map(postings.map(posting => (posting.uuid, posting)): _*)
      for (posting <- postings if posting.parentUUID != null) {
        val parent = pmap(posting.parentUUID)
        parent._replies = posting :: parent._replies
      }
      postings = postings.filter(_.parentUUID == null)
      log debug containerId + " " + postingTupels.mkString
      log info "Refreshed"
      postings
    })
  }

  def subscribe() {
    assume(subscribed == false)
    notification1 = peer.space.registerNotification(containerId, List(Operation.Write))(e => {
      refresh();
      val entry = e.asInstanceOf[(String, String, String, String, String)]
      val pUUID = if (entry._5 == "") null else entry._5
      val posting = new XVSMPosting(elevator, peer, this, entry._1, entry._2, entry._3, List(), entry._4, pUUID);
      session.fire(l => l.postingCreated(posting))
    })
    subscribed = true
  }

  def unsubscribe() {
    assume(subscribed == true)
    notification1.remove
    subscribed = false
  }

  def createPosting(author: String, subject: String, content: String): Posting =
    peer.space.transaction()(tx => {
      val posting = new XVSMPosting(elevator, peer, this, author, subject, content, List(), java.util.UUID.randomUUID.toString, null)
      val postingContainer = tx.container(containerId)
      postingContainer.write(0, (posting.author, posting.subject, posting.content, posting.uuid, ""), new KeySelector("uuid", posting.uuid))
      postings = posting :: postings
      posting
    })

  override def equals(obj: Any) = obj match {
    case t: XVSMTopic => t.peer == this.peer && t.name == this.name
    case _ => false
  }

  override def hashCode = this.name.hashCode + this.peer.hashCode
}
