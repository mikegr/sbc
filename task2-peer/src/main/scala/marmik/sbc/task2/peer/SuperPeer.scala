package marmik.sbc.task2.peer

trait SuperPeer {
  
  /** 
   * @param url: Url of super pper
   * @param selfName: Name of this peer
   * @param url: Url of this peer 
   */
  def login(url:String, selfName:String, selfUrl:String);

  /**
   Registers a listener to get notification about new posts or edits.
   */
  def register(l:Listener);

  /** Returns list of available peers
   */
  def getPeers():List[Peer];
  
  def logout();  
  
}
