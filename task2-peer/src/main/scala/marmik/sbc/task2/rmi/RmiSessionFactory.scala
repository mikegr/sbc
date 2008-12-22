package marmik.sbc.task2.rmi

import java.rmi._
import java.rmi.registry._

import marmik.sbc.task2.peer._

class RmiSessionFactory extends SessionFactory {

  def name() = "RmiSessionFactory"

  /**
   * @param url: Url of super pper
   * @param selfName: Name of this peer
   * @param url: Url of this peer
   */
  def login(superPeerUrl: String, selfName: String, selfUrl: String):Session = {
    //register local interface

    //LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
    val remotePeer = new RemotePeerImpl(selfUrl);

    Naming.rebind(selfUrl, remotePeer);

    val superPeer = Naming.lookup(superPeerUrl).asInstanceOf[RemoteSuperPeer];
    superPeer.login(selfUrl, selfName);
    //add existing topics...

    new RmiSession(superPeer, selfName, selfUrl);
  }

}
