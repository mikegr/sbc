package marmik.sbc.task2.rmi

@SerialVersionUID(8814687055329539494L)
class PeerInfo(var url:String, var name:String) extends java.io.Serializable{
  def this() = this("","");
}
