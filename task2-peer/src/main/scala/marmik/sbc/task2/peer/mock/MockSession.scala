package marmik.sbc.task2.peer.mock

import marmik.sbc.task2.peer._

class MockSession(url: String, name: String) extends Session {
  var loggedIn = true
  var mockLocalPeer = new MockPeer(this, "peer l", List(new MockTopic("Bosheit", List()), new MockTopic("Freundlichkeit", List())))
  var mockPeers = List(mockLocalPeer, new MockPeer(this, "peer a", List(new MockTopic("Pure Lust am Leben", List()))), new MockPeer(this, "peer b", List()))

  def registerListener(l: Listener) {
    if(!loggedIn)
      throw new IllegalStateException("Not logged in")
  }

  def peers(): List[Peer] = {
    if(!loggedIn)
      throw new IllegalStateException("Not logged in")
    mockPeers
  }

  def localPeer(): Peer = {
    if(!loggedIn)
      throw new IllegalStateException("Not logged in")
    mockLocalPeer
  }

  def logout() {
    loggedIn = false
  }
}
