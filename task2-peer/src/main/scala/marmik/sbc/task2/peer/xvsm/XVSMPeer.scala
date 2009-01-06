package marmik.sbc.task2.peer.xvsm

import marmik.sbc.task2.peer._

class XVSMPeer(ec:EasyCapi, val url:String, val name:String) extends Peer {

  def session(): Session = ec.session

  def newTopic(name:String):Topic  = {
    throw new UnsupportedOperationException("Not allowed on foreign peer");
  }

  def topics():List[Topic] = {
    ec.topics(this);
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
