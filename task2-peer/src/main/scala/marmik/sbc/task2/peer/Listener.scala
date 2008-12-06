package marmik.sbc.task2.peer

trait Listener {

  def newPost(posting:Posting);
  def editPost(posting:Posting);
  
}
