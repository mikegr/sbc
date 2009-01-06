package marmik.sbc.task2.peer.rmi

@SerialVersionUID(8814687055329539494L)
class PeerInfo(val url:String, val name:String) extends java.io.Serializable{
  def this() = this("","");


  override
    def equals(o:Any):Boolean = {
      if (o.isInstanceOf[PeerInfo]) {
        val that = o.asInstanceOf[PeerInfo];
        if (that.url == this.url &&
              that.name == this.name) true
      }
      false
    }
}
