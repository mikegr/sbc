package marmik.sbc.task2.rmi

import java.util.List
import java.lang.Integer
import java.util.ArrayList
import java.util.GregorianCalendar
import scala.collection.jcl.Conversions._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import marmik.sbc.task2.peer._

class LocalPeer(sp:RemoteSuperPeer, rmis:RmiSession, p:PeerInfo) extends RmiPeer(sp, rmis, p) {

  override
  def newTopic(name:String): Topic = {
    sp.newTopic(url, name);
    new RmiTopic(sp, rmis, new TopicInfo(url, name));
  }

}

