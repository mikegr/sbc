package marmik.sbc.task2.peer.xvsm2

import marmik.sbc.task2.peer._

class XVSMSession extends Session {
  def registerListener(l: Listener) { }

  def peers(): List[Peer] = null

  def localPeer(): Peer = null

  def logout() { }
}
