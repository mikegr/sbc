package marmik.sbc.task2.peer.xvsm

import scala.collection.mutable._
import org.xvsm.interfaces._
import org.xvsm.core._
import org.xvsm.transactions._
import org.xvsm.coordinators._
import org.xvsm.selectors._
import marmik.sbc.task2.peer.xvsm.XVSMContants._

import marmik.sbc.task2.peer._

class XVSMSession(superPeerUrl:String, selfName:String) extends Session {

	val uri = new java.net.URI(superPeerUrl);
	val capi = new EasyCapi(new Capi(), this, uri, selfName);

	def login():XVSMSession = {
	  capi.writePeerInfo();
      this
	}

  val localPeer = new XVSMLocalPeer(capi, this, capi.selfUrl, selfName); //setting url to null, because of embedded space

  val listener = new ListBuffer[Listener]();

  def registerListener(l:Listener) = {
    listener + l;
  }

  val cachedPeers = new HashMap[String, XVSMPeer]();

  def peers():List[Peer] = {
    val peers = capi.readPeerInfo();
    peers.foreach(p=>
      cachedPeers += (p.url -> p)
    );
    peers;
  }

  def logout() {
    capi.logout;
  }
  def dumpTopics() {
     capi.dumpTopics();
  }



}
