package marmik.sbc.task2.peer.xvsm

import marmik.sbc.task2.peer._
import java.util.GregorianCalendar;


class XVSMPosting(ec:EasyCapi, val topic:XVSMTopic, val parent:XVSMPosting, val id:Long, val author:String, val subject:String, val content:String, val createdAt:GregorianCalendar) extends Posting {

  def edit(newContent:String) {
    ec.editPosting(this, newContent);
  }
  def reply(author:String, subject:String, content:String):Posting = {
    ec.reply(this, author, subject, content);
  }
  def replies():List[Posting] = {
    ec.replies(this);
  }
  
  override
  def equals(o:Any):Boolean = {
    if (o.isInstanceOf[XVSMPosting]) {
      val that = o.asInstanceOf[XVSMPosting];
      if (that.topic == this.topic && 
          that.id == this.id && 
          that.content == this.content
      ) true
    }
    false
  }
  override 
  def toString():String = {
    topic.name + "|" + parent.id + "|" + id + "|" + author + "|" + subject + "|" + content + "|" + createdAt; 
  }
}
