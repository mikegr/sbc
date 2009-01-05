package marmik.sbc.task2.peer.mock

import marmik.sbc.task2.peer._

class MockPeer(name: String, topics: List[MockTopic]) extends Peer {
  for(topic <- topics)
    topic.setPeer(this)

  def name(): String = name

  def topics(): List[Topic] = topics

  def newTopic(name:String): Topic = {
    throw new UnsupportedOperationException("That's what you get for using mock objects")
  }

  override def hashCode(): Int = {
    name.hashCode
  }
}
