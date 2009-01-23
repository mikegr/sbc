package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._
import org.xvsm.selectors._
import org.xvsm.core.notifications.Operation

class XVSMTopic(val elevator: SpaceElevator, val session: XVSMSession, val peer: XVSMPeer, val name: String, var postings: List[XVSMPosting]) extends Topic {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);
  var subscribed = false
  val containerId = "postings" + name
  var notification1: Notification = null
  elevator.localSpace.implicitTransaction()(tx => {
    tx.lookupOrCreateContainer(containerId, Coordinators.postings: _*)
  })


  override def refresh() {
    log info "Refreshing"
    peer.space.transaction()(tx => {
      val postingContainer = tx.container(containerId)
      val postingTupels = postingContainer.read[(String, String, String, String)](0, new RandomSelector(Selector.CNT_ALL))
      postings = postingTupels.map(_ match {
        case (author: String, subject: String, content: String, uuid: String) =>
          new XVSMPosting(elevator, peer, this, author, subject, content, List(), uuid)
      }).toList
      log info "Refreshed"
      postings
    })
  }

  def subscribe() {
    assume(subscribed == false)
    notification1 = peer.space.registerNotification(containerId, List(Operation.Write))(entry => {
      log.debug("NEW POSTING" + entry)
      //val posting = new XVSMPosting(elevator, peer, this, entry._0, entry._1, entry._2, List())
      val posting = new XVSMPosting(elevator, peer, this, "author", "subject", "leer", List(), "uuid leer");
      session.fire(l => l.postingCreated(posting))
    })
  }

  def unsubscribe() {
    assume(subscribed == true)
    notification1.remove
  }

  def createPosting(author: String, subject: String, content: String): Posting =
    peer.space.transaction()(tx => {
      val posting = new XVSMPosting(elevator, peer, this, author, subject, content, List(), java.util.UUID.randomUUID.toString)
      val postingContainer = tx.container(containerId)
      postingContainer.write(0, (posting.author, posting.subject, posting.content, posting.uuid), new KeySelector("uuid", posting.uuid))
      postings = posting :: postings
      posting
    })

  override def equals(obj: Any) = obj match {
    case t: XVSMTopic => t.peer == this.peer && t.name == this.name
    case _ => false
  }

  override def hashCode = this.name.hashCode + this.peer.hashCode
}
