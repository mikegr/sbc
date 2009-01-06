package marmik.sbc.task2.peer.xvsm

class XVSMLocalPeer(ec:EasyCapi, url:String, name:String) extends XVSMPeer(ec, url, name) {

  override def newTopic(name:String):Topic  = {
    ec.newTopic(this, name);
  }

}
