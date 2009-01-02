package marmik.sbc.task2.rmi

import java.lang.Integer
import java.util.GregorianCalendar;
import marmik.sbc.task2.peer._
import scala.collection.jcl.Conversions._

class RmiPosting(val session:RmiSession, val topic:RmiTopic, val parent:RmiPosting, val internal:PostingInfo) extends Posting {

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiPosting]);

  val id:Integer = internal.id;
  val url:String = topic.url;

  def author(): String = internal.author;
  def subject(): String = internal.subject;
  def createdAt(): GregorianCalendar = internal.date;
  def content(): String = internal.content;

  def replies(): List[Posting] = {
    logger debug ("Requested replies for post with id '" + internal.id + "'")
    session.getRemotePeer(url).getReplys(id).map(r => new RmiPosting(session, topic, this, r)).toList;
  }

  def reply(author:String, subject:String, content:String):Posting = {
    logger debug ("Reply to post with id '" + internal.id + "'");
    val peer = session.getRemotePeer(url);
    val idx = peer.post(topic.name, id, author, subject, content);

    new RmiPosting(session, topic, this, peer.getPost(idx));

  }

  def edit(newContent: String) {
    logger debug ("Edit post with id '" + internal.id + "'")
    val peer = session.getRemotePeer(url);
    peer.edit(internal.id, newContent);

  }

  override
  def equals(o:Any):Boolean = {
    if (o.isInstanceOf[RmiPosting]) {
      val rp = o.asInstanceOf[RmiPosting];
      if ( rp.url == this.url &&
           rp.topic == this.topic &&
           rp.internal == this.internal) true
    }
    false;
  }
}
