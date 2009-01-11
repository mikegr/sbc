package marmik.sbc.task2.peer.xvsm2

import scala.collection.mutable.ListBuffer

import marmik.xvsm._
import marmik.sbc.task2.peer._
import org.xvsm.selectors._
import org.xvsm.core.notifications.Operation

class XVSMSession(val elevator: SpaceElevator, val superPeer: Space, val name: String) extends Session {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);
  var localPeer: XVSMPeer = new XVSMPeer(elevator, this, (name, elevator.localSpace.url))
  var listeners: ListBuffer[Listener] = new ListBuffer()
  superPeer.registerNotification("peers", List(Operation.Write))( entry => {
    log info "Peer joins" + entry
    fire( l => l.peerJoins(new XVSMPeer(elevator, this, entry.asInstanceOf[(String, java.net.URI)])) )
  })
  log info "Connected"

  def registerListener(l: Listener) { listeners += l }

  def peers(): Seq[XVSMPeer] = {
    log debug "Read peers"
    superPeer.transaction()( tx => {
      val peers = tx.container("peers", Coordinators.peers: _*)
      peers.read[(String, java.net.URI)](0, new RandomSelector(Selector.CNT_ALL)).map(new XVSMPeer(elevator, this, _))
    })
  }

  def logout() {
    log debug "Logout"
    superPeer.transaction()( tx => {
      val peers = tx.container("peers", Coordinators.peers: _*)
      peers.takeOne[(String, java.net.URI)](0, new KeySelector("name", localPeer.name))
    })
  }

  def setBadWordList(seq:Seq[String]) {
    throw new UnsupportedOperationException("setBadWordList not implemented")
  }

  def getBadWordList():Seq[String] = {
    throw new UnsupportedOperationException("getBadWordList not implemented")
  }

  override def hashCode = elevator.hashCode

  override def equals(other: Any) =
    other match {
      case s: XVSMSession => this.name==s.name && this.elevator==s.elevator
      case _ => false
    }

  private[xvsm2] def fire(func: Listener => Any) {
    for(listener <- listeners)
      func(listener)
  }

}
