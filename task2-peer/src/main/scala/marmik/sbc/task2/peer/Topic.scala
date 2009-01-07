package marmik.sbc.task2.peer

trait Topic {
  def name(): String;
  def peer(): Peer;

  def postings(): Seq[Posting];
  def subscribe();
  def unsubscribe();

  def createPosting(author:String, subject:String, content:String):Posting;

}
