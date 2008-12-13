package marmik.sbc.task2.peer.xvsm

import marmik.sbc.task2.peer._

class XVSMPeer(ec:EasyCapi, val url:String, val name:String) extends Peer {
  
  def newTopic(name:String):Topic  = {
    throw new UnsupportedOperationException("Not allowed on foreign peer");
  }
  
  def topics():List[Topic] = {
    ec.topics(url);
  }
  
  override def equals(o:Any):boolean = {
    if (! o.isInstanceOf[XVSMPeer] ) false 
    else if (o.asInstanceOf[XVSMPeer].url != this.url) false 
    else true;
  } 
}
