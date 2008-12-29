package marmik.sbc.task2.rmi

import marmik.sbc.task2.peer._
import scala.collection.jcl.Conversions._

class RmiTopic(session:RmiSession, val url:String, val name:String) extends Topic {

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiTopic])

  def postings(): List[Posting] = {
    logger debug ("Postings requested:" + url + "|" + name);
    session.getRemotePeer(url).getPostings(name).map(p=> new RmiPosting(session, this, null, p)).toList;
  }
  def subscribe() {
    logger debug ("Subscribe to " + name);
    session.getRemotePeer(url).subscribe(name, session.selfUrl);
  }
  def unsubscribe() {
    logger debug ("Unsubscribe to " + name);
    session.getRemotePeer(url).subscribe(name, session.selfUrl);
  }

  def createPosting(author:String, subject:String, content:String):Posting = {
    logger debug ("create Posting for " + url + "|" + name);
    val idx = session.getRemotePeer(url).post(name, null, author, subject, content);
    new RmiPosting(session, this, null, session.getRemotePeer(url).getPost(idx));
  }

  override
  def equals(o:Any):Boolean = {
    if (o.isInstanceOf[RmiTopic]) {
      val rt = o.asInstanceOf[RmiTopic];
      if (  rt.name == this.name &&
            rt.url == this.url
      ) true
    }
    false
  }
}
