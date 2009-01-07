package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._
import org.xvsm.selectors._
import org.xvsm.core.notifications.Operation

class XVSMPeer(elevator: SpaceElevator, val session: XVSMSession, nameUrl: (String, java.net.URI)) extends Peer {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);
  val url = nameUrl._2
  val name = nameUrl._1
  val space = elevator.remoteSpace(url)
  space.registerNotification("topics", List(Operation.Write))( entry => {
    session.fire( l => l.topicCreated(this, new XVSMTopic(elevator, session, this, entry.toString, List())))
  })
  log debug "new Peer: " + url

  def topics(): List[Topic] = space.transaction()( tx => {
    log debug "topics" + url
    val c = tx.container("topics", Coordinators.topics: _*)
    c.read[String](0, new RandomSelector(Selector.CNT_ALL)).map(new XVSMTopic(elevator, session, this, _, List())).toList
  })
  def newTopic(name:String): Topic = space.transaction()( tx => {
    log debug "newTopic" + url
    val c = tx.container("topics", Coordinators.topics: _*)
    c.write(0, name, new KeySelector("name", name))
    new XVSMTopic(elevator, session, this, name, List())
  })

  override def hashCode() = url.hashCode

  override def equals(other: Any) =
    other match {
      case p: XVSMPeer => p.url==this.url && p.session==this.session
      case _ => false
    }
}
