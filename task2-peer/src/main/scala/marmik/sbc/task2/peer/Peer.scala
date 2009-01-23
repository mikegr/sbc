package marmik.sbc.task2.peer

trait Peer {
  def session(): Session;
  def name():  String;
  def topics(): List[Topic];
  /**Usually it's only allowed on own peer a*/
  def newTopic(name:String): Topic;

  def isLocal(): Boolean = {
    this == session().localPeer
  }
  def hashCode(): Int;

  override def equals(other: Any) = other match {
    case p: Peer => p.name == this.name
    case _ => false
  }
}
