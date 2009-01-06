package marmik.sbc.task2.peer.mock

import java.util.GregorianCalendar

import marmik.sbc.task2.peer._

class MockPosting(val session: Session, val subject: String, val author: String, val content: String, val createdAt: GregorianCalendar, val topic: MockTopic, var parent: MockPosting, var replies: List[MockPosting]) extends Posting {
  def reply(author:String, subject:String, content:String):Posting = {
    throw new UnsupportedOperationException("Go mock yourself!")
  }
  def edit(newContent: String) {
    throw new UnsupportedOperationException("Go mock yourself!")
  }
}

