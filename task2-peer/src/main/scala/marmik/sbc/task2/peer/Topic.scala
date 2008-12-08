package marmik.sbc.task2.peer

trait Topic {
  def id():Long;
  def name():  String;
  
  def postings(): List[Posting];
  def subscribe();
  def unsubscribe();
  
  def createPosting(author:String, subject:String, content:String):Posting;
  
}
