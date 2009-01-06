package marmik.sbc.task2.peer.mock

import marmik.sbc.task2.peer._

import scala.collection.mutable._

class MockSession(url: String, name: String) extends Session {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  var loggedIn = true
  var mockLocalPeer = new MockPeer(this, "peer l", List(new MockTopic("Bosheit", List()), new MockTopic("Freundlichkeit", List())))
  var mockPeers = List(mockLocalPeer, new MockPeer(this, "peer a", List(new MockTopic("Pure Lust am Leben", List()))), new MockPeer(this, "peer b", List()))
  val listeners = new ListBuffer[Listener]();

  val t = new Thread(new Runnable() {
    def run {
      while(true) {
        Thread.sleep(4000);
        log.info("Adding Mock Topic to local peer")
        val topic = new MockTopic("asdasd", List())
        topic.setPeer(mockLocalPeer)
        mockLocalPeer.setTopics(List(topic).union(mockLocalPeer.topics))
        for(listener <- listeners)
          listener.topicCreated(mockLocalPeer, topic)
      }
    }
  });
  t.setDaemon(true)
  t.start

  def registerListener(l: Listener) {
    if(!loggedIn)
      throw new IllegalStateException("Not logged in")
    listeners += l
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
