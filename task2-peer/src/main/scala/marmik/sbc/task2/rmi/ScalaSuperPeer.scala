package marmik.sbc.task2.rmi

import java.util._

trait ScalaSuperPeer extends java.rmi.Remote{

    @throws (classOf[java.rmi.RemoteException])
  	def getTopcis():List[ScalaTopic];

    @throws (classOf[java.rmi.RemoteException])
	def newTopic(url:String, name:String);
    
}
