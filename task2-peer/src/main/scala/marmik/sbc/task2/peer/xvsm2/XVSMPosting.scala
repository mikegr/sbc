package marmik.sbc.task2.peer.xvsm2

import _root_.marmik.xvsm.Space
import _root_.marmik.xvsm.SpaceElevator
import marmik.sbc.task2.peer._

import java.util.GregorianCalendar

class XVSMPosting(elevator: SpaceElevator, peer: XVSMPeer, val _topic: XVSMTopic, var _author: String, var _subject: String, var _content: String, var _replies: List[Posting], val uuid: String) extends Posting {

  def topic(): Topic = _topic
  def parent(): Posting = null

  def author(): String = _author
  def subject(): String = _subject
  def createdAt(): GregorianCalendar = null
  def content(): String = _content

  def replies(): List[Posting] = _replies
  def reply(author:String, subject:String, content:String):Posting = null

  def edit(newContent: String) = null
}
