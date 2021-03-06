package marmik.sbc.task2.peer.rmi

import java.util._

trait RemoteSuperPeer extends java.rmi.Remote {

    @throws (classOf[java.rmi.RemoteException])
    def login(url:String, name:String);

    @throws (classOf[java.rmi.RemoteException])
    def peers():List[PeerInfo];

    @throws (classOf[java.rmi.RemoteException])
    def logout(url:String);

    @throws (classOf[java.rmi.RemoteException])
    def topics(url: String):List[TopicInfo];

    @throws (classOf[java.rmi.RemoteException])
  def newTopic(url:String, name:String);
}
