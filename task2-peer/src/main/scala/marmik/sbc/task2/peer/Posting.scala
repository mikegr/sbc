package marmik.sbc.task2.peer

import java.util.GregorianCalendar

trait Posting {

  def author(): String;
  def subject(): String;
  def createdAt(): GregorianCalendar;
  def content(): String;
  
  def replies(): List[Posting];
  def reply(posting: Posting);
  
  def edit(newContent: String);
  
}
