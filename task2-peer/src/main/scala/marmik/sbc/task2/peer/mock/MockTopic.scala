package marmik.sbc.task2.peer.mock

import marmik.sbc.task2.peer._

class MockTopic(n: String, p: List[Posting]) extends Topic {
  def name(): String = n;
  def postings(): List[Posting] = p;

  def subscribe() {
    throw new UnsupportedOperationException("Go mock yourself!")
  }
  def unsubscribe() {
    throw new UnsupportedOperationException("Go mock yourself!")
  }

  def createPosting(author:String, subject:String, content:String): Posting =
    throw new UnsupportedOperationException("Go mock yourself!")

}
