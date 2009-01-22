package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._

import org.xvsm.selectors._

class XVSMSessionFactory extends SessionFactory {
  val name = "XVSM2"
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  def login(superPeerUrl: String, name: String): Session = {
    val elevator = new SpaceElevator()
    val superPeer = elevator.remoteSpace(new java.net.URI(superPeerUrl))
    superPeer.transaction()( tx => {
      val peers = tx.lookupOrCreateContainer("peers", Coordinators.peers.toArray: _*)
      peers.write(0, (name, elevator.localSpace.url), new KeySelector("name", name))
    })
    elevator.localSpace.implicitTransaction()( tx => {
      tx.createContainer("topics", Coordinators.topics: _*)
    })
    log info "Logged in"

    new XVSMSession(elevator, superPeer, name)
  }
}
