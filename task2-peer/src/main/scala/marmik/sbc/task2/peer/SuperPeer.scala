package marmik.sbc.task2.peer

trait SuperPeer {
  
  def login(url:String, selfName:String, selfUrl:String);

  def register(l:Listener);

  def getPeers():List[Peer];
  
  def logout();  
  
}
