package marmik.sbc.task2.peer.xvsm

import java.util.GregorianCalendar
import java.net._

import org.apache.commons.lang.builder._
import org.xvsm.core._
import org.xvsm.core.notifications._
import org.xvsm.core.aspect._

import scala.collection.mutable._


class XVSMTopic(ec:EasyCapi, val peer:XVSMPeer, val name:String) extends Topic {

  val logger = org.slf4j.LoggerFactory.getLogger(this.getClass);
  val url = peer.url;

  val cachedPosts = new HashMap[Long, XVSMPosting]();

  def postings(): List[Posting] = {
    logger debug ("read postings() from " + name)
    val posts = ec.postings(this);

    posts.foreach(p =>
       cachedPosts += (p.id -> p)
    );
    posts
  }

  def subscribe() = {
    logger debug ("subscribe to " + name)
    ec.subscribe(this);
  }
  def unsubscribe() = {
    logger debug ("unsubscribe to " + name)
    ec.unsubscribe(this);
  }
  def createPosting(author:String, subject:String, content:String):Posting = {
    logger debug ("createPosting for topic '" + name + "' with subject '"  + subject + "'")
    ec.createPosting(this, author, subject, content);
  }

  override
  def equals(o:Any):Boolean = o match {
    case that:XVSMTopic => { that.peer == this.peer && that.name == this.name }
    case _ => false
  }

  override
    def hashCode():Int =
      new HashCodeBuilder(17,37)
      .append(peer)
      .append(name)
      .toHashCode

  var listener:TopicListener = _;

}
