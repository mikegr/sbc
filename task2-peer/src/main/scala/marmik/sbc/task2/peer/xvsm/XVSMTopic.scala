package marmik.sbc.task2.peer.xvsm

import java.util.GregorianCalendar
import java.net._
import org.xvsm.core._
import org.xvsm.core.notifications._
import org.xvsm.core.aspect._



class XVSMTopic(ec:EasyCapi, val peer:XVSMPeer, val name:String) extends Topic {

  val url = peer.url;

  def postings(): List[Posting] = {
    ec.postings(this);
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
      if (that.peer == this.peer && that.name == this.name) true
    }
    false
  }

  var listener:URI = _;

}
