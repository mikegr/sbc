package marmik.sbc.task2.peer.rmi

class TopicInfo(val url:String, val name:String) extends java.io.Serializable {

  def this() = this("","");

  override
  def toString():String = {
	  url + " : " + name;
  }

  override
  def equals(o:Any):Boolean = {
    if (o.isInstanceOf[TopicInfo]) {
      val that = o.asInstanceOf[TopicInfo];
      if (that.url == this.url &&
          that.name == this.url) true
    }
    false
  }
}
