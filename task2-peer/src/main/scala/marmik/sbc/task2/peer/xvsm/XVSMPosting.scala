package marmik.sbc.task2.peer.xvsm

import marmik.sbc.task2.peer._
import java.util.GregorianCalendar;


class XVSMPosting(ec:EasyCapi, val url:String, val topic:String, val author:String, val subject:String, val content:String, val createdAt:GregorianCalendar) extends Posting {

  def edit(newContent:String) {}
  def reply(author:String, subject:String, content:String):Posting = {
    null
  }
  def replies():List[Posting] = {
    null;
  }
}
