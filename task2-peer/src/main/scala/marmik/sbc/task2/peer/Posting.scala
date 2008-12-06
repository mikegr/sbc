package marmik.sbc.task2.peer

import java.util.Date

trait Posting {

  def author():String;
  def subject():String;
  def creation():Date;
  def content():String;
  
  def responses():List[Posting];
  def reply(posting:Posting);
  
  def edit(content:String);
  
}
