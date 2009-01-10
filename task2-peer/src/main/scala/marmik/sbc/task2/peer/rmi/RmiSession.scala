package marmik.sbc.task2.peer.rmi

import marmik.sbc.task2.peer._

import java.rmi._

import scala.collection.jcl.Conversions._
import scala.collection.mutable._

class RmiSession(val superPeerUrl:String, val selfName:String, val selfUrl:String) extends Session {

  val superPeer:RemoteSuperPeer = {
    val superPeer = Naming.lookup(superPeerUrl).asInstanceOf[RemoteSuperPeer];
    superPeer.login(selfUrl, selfName);
    superPeer;
  }

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiSession]);

  val listener = new ListBuffer[Listener]();

  val selfPeer:RemotePeerImpl = {
    val remotePeer = new RemotePeerImpl(selfUrl, this);
    Naming.rebind(selfUrl, remotePeer);
    remotePeer;
  }

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
  def peers(): Seq[Peer] = {
    val result = new ListBuffer[RmiPeer];

    superPeer.peers.foreach(peerInfo => {
      val rmiPeer = new RmiPeer(this, peerInfo);
      cachedPeers += (peerInfo.url -> rmiPeer);
      result += rmiPeer;
    });
    result.toSeq;
  }

  def addToCache(url:String, name:String):RmiPeer= {
    val rmiPeer = new RmiPeer(this, new PeerInfo(url, name));
    cachedPeers += (url -> rmiPeer);
    rmiPeer;
  }

  /** Returns local peer */

  def logout() {
    logger.debug("Logging out: " + selfUrl);
    superPeer.logout(selfUrl);
    Naming.unbind(selfUrl);
  }

  val remotePeers = new HashMap[String, RemotePeer]();

  def getRemotePeer(url:String):RemotePeer = {
    logger trace ("Lookup of " + url);
    remotePeers.getOrElseUpdate(url, Naming.lookup(url).asInstanceOf[RemotePeer]);
  }

  def url(): String = {
    selfUrl
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
