package marmik.sbc.task2.peer.xvsm

import marmik.sbc.task2.peer._
import scala.collection.mutable._

class XVSMPeer(ec:EasyCapi, val session:XVSMSession, val url:String, val name:String) extends Peer {


  def newTopic(name:String):Topic  = {
    throw new UnsupportedOperationException("Not allowed on foreign peer");
  }

  val cachedTopics = new HashMap[String, XVSMTopic]();

  def topics():List[Topic] = {
    val topics = ec.topics(this);
    topics.foreach(t =>
       cachedTopics += (t.name -> t)
    );
    topics;
  }

  def addInternalTopic(name:String):XVSMTopic = {
    val internal = new XVSMTopic(ec, this, name);
    cachedTopics += (name -> internal);
    internal;
  }

  override def equals(o:Any):Boolean = {
    if (! o.isInstanceOf[XVSMPeer] ) false
    else if (o.asInstanceOf[XVSMPeer].url != this.url) false
    else true;
  }

  def dumpPostings() {
    ec.dumpPostings(url);
  }
}
