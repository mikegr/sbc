package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._

class XVSMSession(val elevator: SpaceElevator, val superPeer: Space, val peerContainer: Container) extends Session {
  def registerListener(l: Listener) { }

  def peers(): List[Peer] = null

  def localPeer(): Peer = null

  def logout() { }
}
