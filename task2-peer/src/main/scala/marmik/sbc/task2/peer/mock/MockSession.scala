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
        fireTopicCreated(mockLocalPeer, topic)
      }
    }
  });
  t.setDaemon(true)
  t.start

  log.info("Adding Mock Topic with postings to local peer")
  val topic = new MockTopic("withPostings", List())
  topic.setPostings(List(new MockPosting(this, "test 1", "martin", "ned viel", null, topic, null, List(new MockPosting(this, "auch ein test", "michael", "mehr", null, topic, null, List())))))
  topic.setPeer(mockLocalPeer)
  mockLocalPeer.setTopics(List(topic).union(mockLocalPeer.topics))

  def fireTopicCreated(peer: MockPeer, topic: MockTopic) {
    for(listener <- listeners)
      listener.topicCreated(peer, topic)
  }

  def firePostingCreated(posting: Posting) {
    for(listener <- listeners)
      listener.postingCreated(posting)
  }

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
