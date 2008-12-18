package marmik.sbc.task2.rmi

import  scala.collection.jcl.Conversions._

object TestRmiSuperPeerMain {

  def main(args:Array[String]) {
    val selfUrl = "rmi://localhost/peer1";
    val superPeerUrl = "rmi://localhost/superpeer";
    val superPeer:ScalaSuperPeer = java.rmi.Naming.lookup(superPeerUrl).asInstanceOf[ScalaSuperPeer];

    superPeer.newTopic(selfUrl, "Topic OOP");
    superPeer.newTopic(selfUrl, "Topic Complang");
    val list:Seq[ScalaTopic] = superPeer.getTopcis();
    list.foreach {
      println
    }
  }

}