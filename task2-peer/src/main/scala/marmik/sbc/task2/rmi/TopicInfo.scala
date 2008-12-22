package marmik.sbc.task2.rmi

class TopicInfo(var url:String, var name:String) extends java.io.Serializable {

  def this() = this("","");

  override
  def toString():String = {
	  url + " : " + name;
  }
}
