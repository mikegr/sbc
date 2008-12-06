package marmik.sbc.task2.peer

trait Topic {
  def name():String;
  
  def postings():List[Posting];
  def subscribe();
  def unsubsribe();
  def newpost(posting:Posting);
  
}
