package marmik.sbc.task2.peer.rmi

import marmik.sbc.task2.peer._
import scala.collection.mutable._
import scala.collection.jcl.Conversions._


class RmiPeer(val session:RmiSession, peerInfo:PeerInfo) extends Peer {

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiPeer]);
  val name = peerInfo.name;
  val url = peerInfo.url;

  val cachedTopics = new HashMap[String, RmiTopic]();
  def topics(): List[Topic] = {
    logger debug("Called topcics() for " + url + " with name '" + name + "'")

    val result = new ListBuffer[RmiTopic]();

    session.superPeer.topics.foreach(topicInfo => {
      val rmiTopic = new RmiTopic(session, this, topicInfo.name);
      cachedTopics += (name -> rmiTopic);
      result += rmiTopic;
    });

    result.toList;
  }

  def addToCache(name:String):RmiTopic = {
      val rmiTopic = new RmiTopic(session, this, name);
      cachedTopics += (name -> rmiTopic);
      return rmiTopic;
  }

  /**Usually it's only allowed on own peer a*/

  def newTopic(name:String): Topic = {
    throw new UnsupportedOperationException("only allowed on own peer");
  }

  override
  def equals(o:Any):Boolean = {
    if (o.isInstanceOf[RmiPeer])
      if (o.asInstanceOf[RmiPeer].url == url) true;
    false;
  }

  override
  def hashCode():Int = {
    url.hashCode
  }
}
