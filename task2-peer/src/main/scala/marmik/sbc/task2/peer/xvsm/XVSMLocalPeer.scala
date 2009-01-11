package marmik.sbc.task2.peer.xvsm

class XVSMLocalPeer(ec:EasyCapi, session:XVSMSession, url:String, name:String) extends XVSMPeer(ec, session, url, name) {

  override def newTopic(name:String):Topic  = {
    ec.newTopic(this, name);
  }

}
