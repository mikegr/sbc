package marmik.sbc.task2.peer.xvsm

import marmik.sbc.task2.peer._

class XVSMPeer(ec:EasyCapi, url:String, name:String) extends Peer {
  
  def name():String = name;
  def newTopic(name:String):Topic  = {
    throw new UnsupportedOperationException("Not allowed on foreign peer");
  }
  
  def topics():List[Topic] = {
    ec.topics(url);
  }
}
