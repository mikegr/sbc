package marmik.sbc.task2.peer.mock

import java.util.GregorianCalendar

import marmik.sbc.task2.peer._

class MockPosting(val session: Session, val subject: String, val author: String, var content: String, val createdAt: GregorianCalendar, val topic: MockTopic, var parent: MockPosting, var replies: List[MockPosting]) extends Posting {
  def reply(author:String, subject:String, content:String):Posting = {
    val posting = new MockPosting(session, subject, author, content, null, topic, this, List())
    replies += posting
    posting
  }
  def edit(newContent: String) {
    content = newContent
  }
}

