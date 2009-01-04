package marmik.sbc.task2.peer.rmi

import  scala.collection.jcl.Conversions._

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


object TestRmiSuperPeerMain {

  val selfUrl = "rmi://localhost/peer1";
  val superPeerUrl = "rmi://localhost/superpeer";


  def main(args:Array[String]) {
    LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
    val rsp:RemoteSuperPeer = new RemoteSuperPeerImpl("TestRMI");
	Naming.rebind(superPeerUrl, rsp);

    0 until 2 foreach { x => check(x) };
    System.exit(0);
  }

  def check(x:Int) {

    val superPeer:RemoteSuperPeer = java.rmi.Naming.lookup(superPeerUrl).asInstanceOf[RemoteSuperPeer];
    superPeer.newTopic(selfUrl + x, "Topic OOP");
    superPeer.newTopic(selfUrl + x, "Topic Complang");
    val list:Seq[TopicInfo] = superPeer.topics();
    list.foreach {
      println
    }

  }

}