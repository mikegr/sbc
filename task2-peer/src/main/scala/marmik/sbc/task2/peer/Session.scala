package marmik.sbc.task2.peer

trait Session {

  /**
   Registers a listener to get notification about new posts or edits.
   */
  def registerListener(l: Listener);

  /** Returns list of available peers
   */
  def peers(): Seq[Peer];

  /** Returns local peer */
  def localPeer(): Peer;

  def logout();

  def setBadWordList(list:Seq[String]);

  def getBadWordList():Seq[String];
}
