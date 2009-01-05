package marmik.sbc.task2.peer.xvsm

import java.util.GregorianCalendar
import java.net._
import org.xvsm.core._
import org.xvsm.core.notifications._
import org.xvsm.core.aspect._



class XVSMTopic(ec:EasyCapi, val url:String, val name:String) extends Topic {

  def postings(): List[Posting] = {
    ec.postings(this);
  }
  def peer(): Peer = {
    // TODO: Implement
    throw new UnsupportedOperationException("Not implemented");
  }
  def subscribe() = {
    ec.subscribe(this);
  }
  def unsubscribe() = {
    ec.unsubscribe(this);
  }
  def createPosting(author:String, subject:String, content:String):Posting = {
    ec.createPosting(this, author, subject, content);
  }

  override
  def equals(o:Any):Boolean = {
    if (o.isInstanceOf[XVSMTopic]) {
      val that = o.asInstanceOf[XVSMTopic];
      if (that.url == this.url && that.name == this.name) true
    }
    false
  }

  var listener:URI = _;

}
