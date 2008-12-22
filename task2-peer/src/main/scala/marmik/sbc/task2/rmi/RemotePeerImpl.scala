package marmik.sbc.task2.rmi

import java.util.List
import java.lang.Integer
import java.util.ArrayList
import java.util.GregorianCalendar
import scala.collection.jcl.Conversions._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import scala.actors.Actor._

import marmik.sbc.task2._
import org.slf4j._
import java.rmi._
import java.rmi.server._

@SerialVersionUID(-1234589656005512L)
class RemotePeerImpl @throws(classOf[java.rmi.RemoteException]) (selfUrl:String, session:RmiSession) extends UnicastRemoteObject with RemotePeer {

  val logger = LoggerFactory.getLogger(classOf[RemotePeerImpl]);

  var index:Int = 0;

  val postings = new HashMap[String, List[PostingInfo]]();
  val children = new HashMap[Int, List[PostingInfo]]();

  val indexPostings = new HashMap[Int, PostingInfo]();
  val reverse = new HashMap[Int, String]();

  @throws (classOf[java.rmi.RemoteException])
  override def getPostings(name:String):List[PostingInfo] = synchronized {
     postings.getOrElse(name, null);
  }

  @throws (classOf[java.rmi.RemoteException])
  override def getPost(id:Integer):PostingInfo = synchronized {
    indexPostings.getOrElse(id.intValue, null);
  }

  @throws (classOf[java.rmi.RemoteException])
  override def getReplys(id:Integer):java.util.List[PostingInfo] = synchronized {
    children.getOrElse(id.intValue(), null);
  }

  @throws (classOf[java.rmi.RemoteException])
  override def post(topic:String, id:Integer, author:String, subject:String, content:String ):Integer = synchronized {
    index =  index.intValue + 1;
    val post = new PostingInfo(index, null, author, subject, content, new GregorianCalendar());
    indexPostings += ((index, post));
    if (id == null) {
      postings.getOrElseUpdate(topic, new ArrayList[PostingInfo]()) += post;

    }
    else {
      children.getOrElseUpdate(id.intValue(), new ArrayList[PostingInfo]()) += post;
    }
    reverse += (index -> topic);
    subscriptions.get(topic).foreach{list =>
      list.foreach { url =>
        val notifier = actor {
          receive {
            case u:String => java.rmi.Naming.lookup(u).asInstanceOf[RemotePeer].postCreated(selfUrl, topic, index);
          }
        }
        notifier ! url;
      }
    }
    index
  }

  @throws (classOf[java.rmi.RemoteException])
  override def edit(id:Integer, content:String) = synchronized {
    indexPostings.get(id.intValue).foreach{posting =>
      posting content_= content;
      reverse.get(id.intValue).foreach { topic =>
        subscriptions.get(topic).foreach{list =>
          list.foreach { url =>
            val notifier = actor {
              receive {
                case u:String => java.rmi.Naming.lookup(u).asInstanceOf[RemotePeer].postEdited(selfUrl, topic, id);
              } //reverse
            }
            notifier ! url;
          }
        }
      }
    }
  }


  val subscriptions = new HashMap[String, ListBuffer[String]]();

  @throws (classOf[java.rmi.RemoteException])
  override def subscribe(topic:String, url:String) = synchronized {
    subscriptions.getOrElseUpdate(topic, new ListBuffer[String]()) += url;
  }

  @throws (classOf[java.rmi.RemoteException])
  override def unsubscribe(topic:String, url:String) = synchronized {
    subscriptions.getOrElseUpdate(topic, new ListBuffer[String]()) -= url;
  }

  @throws (classOf[java.rmi.RemoteException])
  override def postCreated(url:String, topic:String, id:Integer) = synchronized {
    logger info (selfUrl + ": at " + url + " for topic " + topic + " a post with id " + id + "has been created");
    session.listener.foreach(_.postingCreated(new RmiPosting(session, url, topic, session.getRemotePeer(url).getPost(id))));
  }

  @throws (classOf[java.rmi.RemoteException])
  override def postEdited(url:String, topic:String, id:Integer) = synchronized {
    logger info (selfUrl + ": at " + url + " for topic " + topic + " a post with id " + id + "has been edited");
    session.listener.foreach(_.postingEdited(new RmiPosting(session, url, topic, session.getRemotePeer(url).getPost(id))));
  }

  @throws (classOf[java.rmi.RemoteException])
  def peerLoggedIn(url:String, name:String) {
    logger info (selfUrl + ": peer logged in: " + url + " with name " + name)
    session.listener.foreach(_.peerJoins(new RmiPeer(session, new PeerInfo(url, name))));
  }

  @throws (classOf[java.rmi.RemoteException])
  def peerLoggedOut(url:String, name:String) {
    logger info (selfUrl + ": peer logged out: " + url + " with name " + name)
    session.listener.foreach(_.peerLeaves(new RmiPeer(session, new PeerInfo(url, name))));
  }

  @throws (classOf[java.rmi.RemoteException])
  def peerHasNewTopic(url:String, name:String) {
     logger info (selfUrl + ": peer " + url + " has new topic: " + name)
     //TODO: Add method to listener interface
     //session.listener.foreach(_.peerLeaves(new RmiPeer(session, new PeerInfo(url, name))));
  }

}
