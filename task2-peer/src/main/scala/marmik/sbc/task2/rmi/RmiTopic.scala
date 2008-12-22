package marmik.sbc.task2.rmi

import marmik.sbc.task2.peer._
import scala.collection.jcl.Conversions._

class RmiTopic(superPeer:RemoteSuperPeer, session:RmiSession, internal:TopicInfo) extends Topic {

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiTopic])
  val name:String = internal.name;
  val url:String = internal.url;

  def postings(): List[Posting] = {
    logger debug ("Postings requested:" + url + "|" + name);
    session.getRemotePeer(url).getPostings(name).map(p=> new RmiPosting(session, url, name, p)).toList;
  }
  def subscribe() { //TODO: I
  }
  def unsubscribe() {
    //TODO:i
  }

  def createPosting(author:String, subject:String, content:String):Posting = {
    logger debug ("create Posting for " + url + "|" + name);
    val idx = session.getRemotePeer(url).post(name, null, author, subject, content);

    new RmiPosting(session, url, name, session.getRemotePeer(url).getPost(idx));

  }

}
