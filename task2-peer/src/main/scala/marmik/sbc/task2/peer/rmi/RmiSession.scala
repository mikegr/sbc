package marmik.sbc.task2.peer.rmi

import marmik.sbc.task2.peer._

import java.rmi._

import scala.collection.jcl.Conversions._
import scala.collection.mutable._

class RmiSession(val superPeer:RemoteSuperPeer, val selfName:String, val selfUrl:String) extends Session {

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiSession]);

  val listener = new ListBuffer[Listener]();
  /**
   Registers a listener to get notification about new posts or edits.
   */
  def registerListener(l: Listener) {
    listener += l;
  }

  val localPeer:RmiPeer = new LocalPeer(this, new PeerInfo(selfUrl, selfName));

  val cachedPeers = HashMap[String, RmiPeer](selfUrl -> localPeer);

  /** Returns list of available peers
   */
  def peers(): List[Peer] = {
    val result = new ListBuffer[RmiPeer];

    superPeer.peers.foreach(peerInfo => {
      val rmiPeer = new RmiPeer(this, peerInfo);
      cachedPeers += (peerInfo.url -> rmiPeer);
      result += rmiPeer;
    });
    result.toList;
  }

  def addToCache(url:String, name:String):RmiPeer= {
    val rmiPeer = new RmiPeer(this, new PeerInfo(url, name));
    cachedPeers += (url -> rmiPeer);
    rmiPeer;
  }

  /** Returns local peer */

  def logout() {
    superPeer.logout(selfUrl);
    Naming.unbind(selfUrl);
  }

  val remotePeers = new HashMap[String, RemotePeer]();

  def getRemotePeer(url:String):RemotePeer = {
    logger trace ("Lookup of " + url);
    remotePeers.getOrElseUpdate(url, Naming.lookup(url).asInstanceOf[RemotePeer]);
  }


  override
  def equals(o:Any):Boolean = {
    if (o.isInstanceOf[RmiSession]) {
      val rs = o.asInstanceOf[RmiSession]
      if (rs.selfUrl == this.selfUrl &&
            rs.selfName == this.selfName) true;
    }
    false
  }
  override
    def hashCode():Int = {
      superPeer.hashCode + selfName.hashCode + selfUrl.hashCode
    }
}
