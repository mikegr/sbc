package marmik.sbc.task2.peer.rmi

class TopicInfo(val url:String, val name:String) extends java.io.Serializable {

  def this() = this("","");

  override
  def toString():String = {
	  url + " : " + name;
  }
}