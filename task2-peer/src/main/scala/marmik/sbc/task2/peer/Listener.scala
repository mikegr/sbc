package marmik.sbc.task2.peer

trait Listener {

  def newPeer(peer:Peer);
  def vanishedPeer(peer:Peer);
  
  def newPost(posting:Posting);
  def editPost(posting:Posting);
  
}
