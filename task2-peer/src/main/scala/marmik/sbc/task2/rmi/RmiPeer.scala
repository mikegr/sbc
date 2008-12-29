package marmik.sbc.task2.rmi

import marmik.sbc.task2.peer._
import scala.collection.jcl.Conversions._


class RmiPeer(session:RmiSession, peerInfo:PeerInfo) extends Peer {

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiPeer]);
  val name = peerInfo.name;
  val url = peerInfo.url;

  def topics(): List[Topic] = {
    logger debug("Topcics for " + url + " with name '" + name + "'")
    session.superPeer.topics.map(x => new RmiTopic(session, x.url, x.name)).toList;
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

}
