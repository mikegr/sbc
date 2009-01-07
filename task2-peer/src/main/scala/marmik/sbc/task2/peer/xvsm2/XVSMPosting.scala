package marmik.sbc.task2.peer.xvsm2

import marmik.sbc.task2.peer._

import java.util.GregorianCalendar

class XVSMPosting extends Posting {

  def topic(): Topic = null
  def parent(): Posting = null

  def author(): String = null
  def subject(): String = null
  def createdAt(): GregorianCalendar = null
  def content(): String = null

  def replies(): List[Posting] = null
  def reply(author:String, subject:String, content:String):Posting = null

  def edit(newContent: String) = null
}
