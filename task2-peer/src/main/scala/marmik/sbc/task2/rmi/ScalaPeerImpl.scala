package marmik.sbc.task2.rmi

import java.util.List
import java.lang.Integer
import java.util.ArrayList
import java.util.GregorianCalendar
import scala.collection.jcl.Conversions._
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

import scala.actors.Actor._

class ScalaPeerImpl(selfUrl:String) extends ScalaPeer {

  var index:Int = 0;

  val postings = new HashMap[String, List[Posting]]();
  val children = new HashMap[Int, List[Posting]]();

  val indexPostings = new HashMap[Int, Posting]();
  val reverse = new HashMap[Int, String]();

  @throws (classOf[java.rmi.RemoteException])
  override def getPosting(name:String):List[Posting] = synchronized {
     postings.getOrElse(name, null);
  }

  @throws (classOf[java.rmi.RemoteException])
  override def getPost(id:Integer):Posting = synchronized {
    indexPostings.getOrElse(id.intValue, null);
  }

  @throws (classOf[java.rmi.RemoteException])
  override def getReplys(id:Integer):java.util.List[Posting] = synchronized {
    children.getOrElse(id.intValue(), null);
  }

  @throws (classOf[java.rmi.RemoteException])
  override def post(topic:String, id:Integer, author:String, subject:String, content:String ) = synchronized {
    index =  index.intValue + 1;
    val post = new Posting(index, null, author, subject, content, new GregorianCalendar());
    indexPostings += ((index, post));
    if (id == null) {
      postings.getOrElseUpdate(topic, new ArrayList[Posting]()) += post;

    }
    else {
      children.getOrElseUpdate(id.intValue(), new ArrayList[Posting]()) += post;
    }
    reverse += (index -> topic);
    subscriptions.get(topic).foreach{list =>
      list.foreach { url =>
        val notifier = actor {
          receive {
            case u:String => java.rmi.Naming.lookup(u).asInstanceOf[ScalaPeer].postCreated(selfUrl, topic, index);
          }
        }
        notifier ! url;
      }
    }
  }

  @throws (classOf[java.rmi.RemoteException])
  override def edit(id:Integer, content:String) {
    indexPostings.get(id.intValue).foreach{posting =>
      posting content_= content;
      reverse.get(id.intValue).foreach { topic =>
        subscriptions.get(topic).foreach{list =>
          list.foreach { url =>
            val notifier = actor {
              receive {
                case u:String => java.rmi.Naming.lookup(u).asInstanceOf[ScalaPeer].postEdited(selfUrl, topic, index);
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
  override def postCreated(url:String, topic:String, id:Integer) = synchronized {null}

  @throws (classOf[java.rmi.RemoteException])
  override def postEdited(url:String, topic:String, id:Integer) = synchronized {null}
}
