package marmik.sbc.task2.peer.rmi

import java.rmi._
import java.rmi.server.UnicastRemoteObject
import java.util._

import scala.collection.jcl.Conversions._
import scala.actors.Actor._
import scalaz.javas.List.ScalaList_JavaList

@SerialVersionUID(-60061789656005512L)
class RemoteSuperPeerImpl @throws(classOf[java.rmi.RemoteException]) (selfUrl:String) extends UnicastRemoteObject with RemoteSuperPeer   {

    val logger = org.slf4j.LoggerFactory.getLogger(classOf[RemoteSuperPeerImpl]);
    val ts = new ArrayList[TopicInfo]();
    val peers = new ArrayList[PeerInfo]();

    // topics() and peers() automatically definied

    def topics(url: String): List[TopicInfo] = ts.filter(_.url == url).toList

    def login(url:String, name:String) {
      logger.debug(name + " logged in from " + url);

      peers.foreach(peer => {
        val notifier = actor {
          receive {
            case peer:PeerInfo => Naming.lookup(peer.url).asInstanceOf[RemotePeer].peerLoggedIn(url, name);
          }
        }
        notifier ! peer
      });

      peers.add(new PeerInfo(url,name));
    }

    def logout(url:String) {
       //TODO: inform asynchron peers;
      val idx = peers.findIndexOf(p => p.url == url);
      if (idx != -1) {
        val peerLoggingOut = peers.get(idx);
        logger.debug(peerLoggingOut.name + "logged out");
        peers.remove(idx);
        peers.foreach(peer => {
          val notifier = actor {
            receive {
              case peer:PeerInfo => Naming.lookup(peer.url).asInstanceOf[RemotePeer].peerLoggedOut(peerLoggingOut.url, peerLoggingOut.name);
            }
          };
          notifier ! peer
        })
      }
      else {
        logger.debug(url + " tries to log out , but not found");
      }
    }


  def newTopic(url:String, name:String) {
    logger.debug("new topic '" + name + "' for " + url);
    ts.add(new TopicInfo(url, name));
      peers.foreach(peer => { //filter(p=> p.url != url)
          val notifier = actor {
            receive {
              case peer:PeerInfo => {
                logger.debug("Notify " + peer.url + " of new topic "  + name);
                Naming.lookup(peer.url).asInstanceOf[RemotePeer].peerHasNewTopic(url, name);
              }
            }
          };
          notifier ! peer
      })
  }

}
