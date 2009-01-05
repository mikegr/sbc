package marmik.sbc.task2.peer

trait Listener {

  def peerJoins(peer: Peer);
  def peerLeaves(peer: Peer);

  def postingCreated(posting: Posting);
  def postingEdited(posting: Posting);

  def topicCreated(peer: Peer, topic: Topic);

}
