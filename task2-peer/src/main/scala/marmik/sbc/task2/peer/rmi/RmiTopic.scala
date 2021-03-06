package marmik.sbc.task2.peer.rmi

import marmik.sbc.task2.peer._
import scala.collection.mutable._
import scala.collection.jcl.Conversions._

class RmiTopic(val session:RmiSession, val peer:RmiPeer, val name:String) extends Topic {

  val url = peer.url;
  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiTopic])

  val cachedPosts = new HashMap[Integer, RmiPosting]();
  def postings(): List[Posting] = {
    logger debug ("Postings requested:" + url + "|" + name);
    val result = new ListBuffer[RmiPosting]();
    session.getRemotePeer(url).getPostings(name).foreach(postInfo => {
      val rmiPosting = new RmiPosting(session, this, null, postInfo);
      cachedPosts += (postInfo.id -> rmiPosting);
      result += rmiPosting
    });
    result.toList;
  }

  def addToCache(postInfo:PostingInfo):RmiPosting = {
    val rmiPosting = new RmiPosting(session, this, null, postInfo);
    cachedPosts += (postInfo.id -> rmiPosting);
    return rmiPosting;
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
  override
  def hashCode():Int = {
    peer.hashCode + name.hashCode
  }
}
