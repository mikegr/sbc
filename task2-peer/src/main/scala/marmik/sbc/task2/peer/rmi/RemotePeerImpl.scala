package marmik.sbc.task2.peer.rmi

import java.util.List
import java.lang.Integer
import java.util.ArrayList
import java.util.GregorianCalendar
import scala.collection.jcl.Conversions._
import scala.collection.jcl._
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

  val postings = new HashMap[String, List[PostingInfo]](); // topic 2 postings
  val children = new HashMap[Int, List[PostingInfo]](); // posting 2 children
  val parents = new HashMap[Int, Int](); //posting 2 parent

  val indexPostings = new HashMap[Int, PostingInfo](); // id to posting
  val reverse = new HashMap[Int, String](); //posting to topic


  @throws (classOf[java.rmi.RemoteException])
  override def getPostings(name:String):List[PostingInfo] = synchronized {
     postings.getOrElse(name, new ArrayList[PostingInfo]());
  }

  @throws (classOf[java.rmi.RemoteException])
  override def getPost(id:Integer):PostingInfo = synchronized {
    indexPostings.getOrElse(id.intValue, null);
  }

  @throws (classOf[java.rmi.RemoteException])
  override def getReplys(id:Integer):java.util.List[PostingInfo] = synchronized {
    children.getOrElse(id.intValue(), new ArrayList[PostingInfo]());
  }

 @throws (classOf[java.rmi.RemoteException])
  override def getParents(id:Integer):java.util.List[PostingInfo] = synchronized {
    if (id == null) {
      null;
    }
    else {
      val parent = parents(id.intValue);
      getParents(parent) ++ Seq(indexPostings(parent));
    }
  }


  @throws (classOf[java.rmi.RemoteException])
  override def post(topic:String, parent:Integer, author:String, subject:String, content:String ):Integer = synchronized {
    index =  index.intValue + 1;
    val post = new PostingInfo(index, parent, author, subject, content, new GregorianCalendar());
    indexPostings += ((index, post));
    if (parent == null) {
      postings.getOrElseUpdate(topic, new ArrayList[PostingInfo]()) += post;

    }
    else {
      children.getOrElseUpdate(parent.intValue(), new ArrayList[PostingInfo]()) += post;
      parents += (index -> parent.intValue());
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
    val peer = session.cachedPeers(url);
    val rmiTopic = peer.cachedTopics(topic);
    val remotePeer = session.getRemotePeer(url);
    val post = rmiTopic.addToCache(remotePeer.getPost(id));

    session.listener.foreach(_.postingCreated(post));
  }

  @throws (classOf[java.rmi.RemoteException])
  override def postEdited(url:String, topic:String, id:Integer) = synchronized {
    logger info (selfUrl + ": at " + url + " for topic " + topic + " a post with id " + id + "has been edited");
    val peer = session.cachedPeers(url);
    val rmiTopic = peer.cachedTopics(topic);
    val post = rmiTopic.cachedPosts(id);
    val postInfo = session.getRemotePeer(url).getPost(id);
    post.content = postInfo.content;
    session.listener.foreach(_.postingEdited(post));
  }

  @throws (classOf[java.rmi.RemoteException])
  def peerLoggedIn(url:String, name:String) {
    logger info (selfUrl + ": peer logged in: " + url + " with name " + name)
    val newPeer = session.addToCache(url, name);
    session.listener.foreach(_.peerJoins(newPeer));
  }

  @throws (classOf[java.rmi.RemoteException])
  def peerLoggedOut(url:String, name:String) {
    logger info (selfUrl + ": peer logged out: " + url + " with name " + name)
    session.cachedPeers -= url;
    session.listener.foreach(_.peerLeaves(session.cachedPeers(url)));
  }

  @throws (classOf[java.rmi.RemoteException])
  def peerHasNewTopic(url:String, name:String) {
     logger info (selfUrl + ": peer " + url + " has new topic: " + name);
     val peer = session.cachedPeers(url);
     val topic = peer.addToCache(name);
     session.listener.foreach(_.topicCreated(peer, topic));
  }

  //parents list has to be in this order: parent, parent of parent, parent of parent of parent,...
  /*
  def getParent(topic:RmiTopic, parents:Seq[PostingInfo]):RmiPosting = {
    if (parents == null || parents.isEmpty) {
      null
    }
    new RmiPosting(session, topic, getParent(topic, parents.drop(1)), parents.first);
  }
  */
}

