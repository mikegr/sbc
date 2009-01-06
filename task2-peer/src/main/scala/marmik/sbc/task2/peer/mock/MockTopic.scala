package marmik.sbc.task2.peer.mock

import marmik.sbc.task2.peer._

class MockTopic(n: String, var p: List[Posting]) extends Topic {
  var pe: Peer = null;

  def name(): String = n;
  def postings(): List[Posting] = p;
  def peer(): Peer = pe;

  def subscribe() {
    throw new UnsupportedOperationException("Go mock yourself!")
  }
  def unsubscribe() {
    throw new UnsupportedOperationException("Go mock yourself!")
  }

  def createPosting(author:String, subject:String, content:String): Posting = {
    val posting = new MockPosting(pe.session, subject, author, content, null, this, null, List())
    p += posting
    pe.session.asInstanceOf[MockSession].firePostingCreated(posting)
    posting
  }

  def setPostings(postings: List[Posting]) {
    p = postings
  }
  def setPeer(peer: Peer) {
    pe = peer
  }
}
