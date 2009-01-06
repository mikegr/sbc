package marmik.sbc.task2.peer.rmi

import java.rmi._
import java.rmi.registry._

import marmik.sbc.task2.peer._

class RmiSessionFactory extends SessionFactory {

  def name() = "RMI"

  /**
   * @param url: Url of super pper
   * @param selfName: Name of this peer
   * @param url: Url of this peer
   */
  def login(superPeerUrl: String, selfName: String):Session = {
    val selfUrl = "rmi://localhost/" + selfName;
    //Expect created registry
    //LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

    val superPeer = Naming.lookup(superPeerUrl).asInstanceOf[RemoteSuperPeer];

    //add existing topics...
    val session = new RmiSession(superPeer, selfName, selfUrl);
    val remotePeer = new RemotePeerImpl(selfUrl, session);



    Naming.rebind(selfUrl, remotePeer);

    superPeer.login(selfUrl, selfName);

    session;
  }


}
