package marmik.sbc.task2.peer.xvsm

import java.util.GregorianCalendar

class XVSMTopic(ec:EasyCapi, url:String, val name:String) extends Topic {
  
  def postings(): List[Posting] = {
    ec.postings(url, name);
  }
  def subscribe() = {}
  def unsubscribe() = {}
  def createPosting(author:String, subject:String, content:String):Posting = {
    ec.createPosting(url, name, author, subject, content);
  }
  
}
