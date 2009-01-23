package marmik.sbc.task2.peer.xvsm2

import _root_.marmik.xvsm.Space
import _root_.marmik.xvsm.SpaceElevator
import marmik.sbc.task2.peer._

import java.util.GregorianCalendar
import org.xvsm.selectors.{RandomSelector, KeySelector}

class XVSMPosting(elevator: SpaceElevator, peer: XVSMPeer, val _topic: XVSMTopic, var _author: String, var _subject: String, var _content: String, var _replies: List[Posting], val uuid: String, val parentUUID: String) extends Posting {
  def topic(): Topic = _topic

  def parent(): Posting = null

  def author(): String = _author

  def subject(): String = _subject

  def createdAt(): GregorianCalendar = null

  def content(): String = _content

  def replies(): List[Posting] = _replies

  override def shouldAdd = false

  def reply(author: String, subject: String, content: String): Posting = {
    peer.space.transaction()(tx => {
      val posting = new XVSMPosting(elevator, peer, _topic, author, subject, content, List(), java.util.UUID.randomUUID.toString, uuid)
      val postingContainer = tx.container(_topic.containerId)
      postingContainer.write(0, (posting.author, posting.subject, posting.content, posting.uuid, uuid), new KeySelector("uuid", posting.uuid))
      _replies = posting :: _replies
      posting
    })
  }

  def edit(newContent: String) = peer.space.transaction()(tx => {
    _content = newContent
    val postingContainer = tx.container(_topic.containerId)
    postingContainer.takeOne[(String, String, String, String, String)](0, new KeySelector("uuid", uuid))
    postingContainer.write(0, (_author, _subject, _content, uuid, parentUUID), new KeySelector("uuid", uuid))
  })
}
