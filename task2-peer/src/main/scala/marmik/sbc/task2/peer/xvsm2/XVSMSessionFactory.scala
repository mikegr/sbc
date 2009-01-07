package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._

import org.xvsm.coordinators._
import org.xvsm.selectors._

class XVSMSessionFactory extends SessionFactory {
  val name = "XVSM2"

  def login(superPeerUrl: String, name: String): Session = {
    val elevator = new SpaceElevator()
    val superPeer = elevator.remoteSpace(new java.net.URI(superPeerUrl))
    val peers = elevator.container(null, superPeer, "peers", new KeyCoordinator())
    peers.write(null, 0, superPeerUrl)

    new XVSMSession(elevator, superPeer, peers)
  }
}
