package marmik.sbc.task2.rmi

import java.rmi._
import java.rmi.server.UnicastRemoteObject
import java.util._

@SerialVersionUID(-60061789656005512L)
class RmiScalaSuperPeer @throws(classOf[java.rmi.RemoteException]) extends UnicastRemoteObject with ScalaSuperPeer   {

	val list = new ArrayList[ScalaTopic]();

    def getTopcis():List[ScalaTopic] = list;

	def newTopic(url:String, name:String) {
		list.add(new ScalaTopic(url, name));
	}

}
