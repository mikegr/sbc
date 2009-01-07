package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._
import org.xvsm.selectors._

class XVSMSession(val elevator: SpaceElevator, val superPeer: Space) extends Session {
  var localPeer: XVSMPeer = null

  def registerListener(l: Listener) { }

  def setLocalPeer(lPeer: XVSMPeer) {
    localPeer = lPeer
  }

  def peers(): Seq[XVSMPeer] = {
    superPeer.transaction()( tx => {
      val peers = tx.container("peers", Coordinators.peers: _*)
      peers.read[(String, java.net.URI)](0, new FifoSelector()).map(new XVSMPeer(elevator, this, _))
    })
  }

  def logout() { }
}
