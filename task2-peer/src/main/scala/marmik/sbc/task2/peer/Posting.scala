package marmik.sbc.task2.peer

import java.util.Date

trait Posting {

  def author():String;
  def subject():String;
  def createdAt():Date;
  def content():String;
  
  def replies():List[Posting];
  def reply(posting:Posting);
  
  def edit(newContent:String);
  
}
