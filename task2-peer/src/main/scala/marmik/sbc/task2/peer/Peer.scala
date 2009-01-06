package marmik.sbc.task2.peer

trait Peer {
  def session(): Session;
  def name():  String;
  def topics(): List[Topic];
  /**Usually it's only allowed on own peer a*/
  def newTopic(name:String): Topic;

  def hashCode(): Int;
}
