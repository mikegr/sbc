package marmik.sbc.task2.rmi

import java.rmi._
import java.rmi.server.UnicastRemoteObject
import java.util._

import scala.collection.jcl.Conversions._

@SerialVersionUID(-60061789656005512L)
class RemoteSuperPeerImpl @throws(classOf[java.rmi.RemoteException]) (selfUrl:String) extends UnicastRemoteObject with RemoteSuperPeer   {

    val logger = org.slf4j.LoggerFactory.getLogger(classOf[RemoteSuperPeerImpl]);
    val topics = new ArrayList[TopicInfo]();
    val peers = new ArrayList[PeerInfo]();

    // topics() and peers() automatically definied


    def login(url:String, name:String) {
      logger.debug(name + " logged from " + url);
      peers.add(new PeerInfo(url,name));

    }

    def logout(url:String) {
      val idx = peers.findIndexOf(p => p.url == url);
      if (idx != -1) {
        logger.debug(peers.get(idx).name + "logged out");
        peers.remove(idx);
      }
      else {
        logger.debug(url + " tries to log out , but not found");
      }
    }


	def newTopic(url:String, name:String) {
	  logger.debug("new topic '" + name + "' for " + url);
	  topics.add(new TopicInfo(url, name));
	}

}
