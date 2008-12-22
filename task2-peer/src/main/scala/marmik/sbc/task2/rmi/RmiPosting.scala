package marmik.sbc.task2.rmi

import java.util.GregorianCalendar;
import marmik.sbc.task2.peer._
import scala.collection.jcl.Conversions._

class RmiPosting(rmiSession:RmiSession, url:String, topic:String, internal:PostingInfo) extends Posting {

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiPosting]);

  def author(): String = internal.author;
  def subject(): String = internal.subject;
  def createdAt(): GregorianCalendar = internal.date;
  def content(): String = internal.content;

  def replies(): List[Posting] = {
    logger debug ("Requested replies for post with id '" + internal.id + "'")
    rmiSession.getRemotePeer(url).getReplys(internal.id).map(r => new RmiPosting(rmiSession, url, topic, r)).toList;
  }
  def reply(author:String, subject:String, content:String):Posting = {
    logger debug ("Reply to post with id '" + internal.id + "'");
    val peer = rmiSession.getRemotePeer(url);
    val idx = peer.post(topic, internal.id, author, subject, content);

    new RmiPosting(rmiSession, url, topic, peer.getPost(idx));

  }

  def edit(newContent: String) {
    logger debug ("Edit post with id '" + internal.id + "'")
    val peer = rmiSession.getRemotePeer(url);
    peer.edit(internal.id, newContent);

  }

}
