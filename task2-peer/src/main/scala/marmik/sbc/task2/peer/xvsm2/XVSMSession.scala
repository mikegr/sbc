package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._
import org.xvsm.selectors._

class XVSMSession(val elevator: SpaceElevator, val superPeer: Space, val name: String) extends Session {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);
  var localPeer: XVSMPeer = new XVSMPeer(elevator, this, (name, elevator.localSpace.url))
  log info "Connected"

  def registerListener(l: Listener) { }

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

  override def hashCode = elevator.hashCode

  override def equals(other: Any) =
    other match {
      case s: XVSMSession => this.name==s.name && this.elevator==s.elevator
      case _ => false
    }
}
