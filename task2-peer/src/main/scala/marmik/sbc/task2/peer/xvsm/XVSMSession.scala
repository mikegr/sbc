package marmik.sbc.task2.peer.xvsm

import scala.collection.mutable._
import org.xvsm.interfaces._
import org.xvsm.core._
import org.xvsm.transactions._
import org.xvsm.coordinators._
import org.xvsm.selectors._
import marmik.sbc.task2.peer.xvsm.XVSMContants._

import marmik.sbc.task2.peer._

class XVSMSession(capi:EasyCapi, superpeer:String, selfUrl:String, selfName:String) extends Session {
  capi.session = this

  val localPeer = new XVSMLocalPeer(capi, selfUrl, selfName); //setting url to null, because of embedded space

  val listener = new ListBuffer[Listener]();

  def registerListener(l:Listener) = {
    listener + l;
  }
  def peers():List[Peer] = {
    capi.readPeerInfo();
  }

  def logout() {
    capi.logout;
  }
  def dumpTopics() {
     capi.dumpTopics();
  }

}
