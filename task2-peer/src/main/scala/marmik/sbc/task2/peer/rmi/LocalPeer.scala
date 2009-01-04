package marmik.sbc.task2.peer.rmi

import java.util.List
import java.lang.Integer
import java.util.ArrayList
import java.util.GregorianCalendar
import scala.collection.jcl.Conversions._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import marmik.sbc.task2.peer._

class LocalPeer(session:RmiSession, p:PeerInfo) extends RmiPeer(session, p) {

  override
  def newTopic(name:String): Topic = {
    session.superPeer.newTopic(url, name);
    new RmiTopic(session, url, name);
  }

}
