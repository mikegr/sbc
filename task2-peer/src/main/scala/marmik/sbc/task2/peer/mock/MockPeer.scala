package marmik.sbc.task2.peer.mock

import marmik.sbc.task2.peer._

class MockPeer(s: Session, name: String, var ts: List[Topic]) extends Peer {
  for(topic <- ts)
    topic.asInstanceOf[MockTopic].setPeer(this)

  def session(): Session = s
  def name(): String = name

  def topics(): List[Topic] = ts
  def setTopics(t: List[Topic]): Unit = { ts = t}

  def newTopic(name:String): Topic = {
    throw new UnsupportedOperationException("That's what you get for using mock objects")
  }

  override def hashCode(): Int = {
    name.hashCode
  }
}
