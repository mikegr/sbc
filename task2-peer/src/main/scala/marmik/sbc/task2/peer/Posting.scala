package marmik.sbc.task2.peer

import java.util.GregorianCalendar

trait Posting {

  def topic():Topic;
  def parent():Posting;

  def author(): String;
  def subject(): String;
  def createdAt(): GregorianCalendar;
  def content(): String;

  def replies(): Seq[Posting];
  def reply(author:String, subject:String, content:String):Posting;

  def edit(newContent: String);

}
