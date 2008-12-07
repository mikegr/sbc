package marmik.sbc.task2.peer

trait SuperPeer {
  
  def login(url: String, selfName: String, selfUrl: String);

  def registerListener(l: Listener);

  def peers(): List[Peer];
  
  def logout();  
  
}
