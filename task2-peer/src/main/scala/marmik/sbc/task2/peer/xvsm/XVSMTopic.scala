package marmik.sbc.task2.peer.xvsm

import java.util.GregorianCalendar

class XVSMTopic(ec:EasyCapi, val url:String, val name:String) extends Topic {
  
  def postings(): List[Posting] = {
    ec.postings(this);
  }
  def subscribe() = {}
  def unsubscribe() = {}
  def createPosting(author:String, subject:String, content:String):Posting = {
    ec.createPosting(this, author, subject, content);
  }
  
  override
  def equals(o:Any):boolean = {
    if (o.isInstanceOf[XVSMTopic]) {
      val that = o.asInstanceOf[XVSMTopic];
      if (that.url == this.url && that.name == this.name) true
    }
    false
  }
}
