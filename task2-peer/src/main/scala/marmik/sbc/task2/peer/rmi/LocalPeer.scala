package marmik.sbc.task2.peer.rmi

import java.util.List
import java.lang.Integer
import java.util.ArrayList
import java.util.GregorianCalendar
import scala.collection.jcl.Conversions._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import marmik.sbc.task2.peer.Topic
import marmik.sbc.task2.peer._

class LocalPeer(s:RmiSession, p:PeerInfo) extends RmiPeer(s, p) {

  override
  def newTopic(name:String): Topic = {
    s.superPeer.newTopic(url, name);
    new RmiTopic(s, this, name);
  }

}

