package marmik.sbc.task2.rmi

import marmik.sbc.task2.peer._

import java.rmi._

import scala.collection.jcl.Conversions._
import scala.collection.mutable._


class RmiSession(superPeer:RemoteSuperPeer, selfName:String, selfUrl:String) extends Session {

  val logger = org.slf4j.LoggerFactory.getLogger(classOf[RmiSession]);
  /**
   Registers a listener to get notification about new posts or edits.
   */
  def registerListener(l: Listener) {
    //TODO:implement
  }

  /** Returns list of available peers
   */
  def peers(): List[Peer] = {
    superPeer.peers.map(x => new RmiPeer(superPeer, this, x)).toList;
  }

  /** Returns local peer */
  val localPeer:Peer = new LocalPeer(superPeer, this, new PeerInfo(selfUrl, selfName));

  def logout() {
    superPeer.logout(selfUrl);
  }

  val remotePeers = new HashMap[String, RemotePeer]();

  def getRemotePeer(url:String):RemotePeer = {
    logger trace ("Lookup of " + url);
    remotePeers.getOrElseUpdate(url, Naming.lookup(url).asInstanceOf[RemotePeer]);
  }

}
