package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._
import org.xvsm.selectors._

class XVSMPeer(elevator: SpaceElevator, session: XVSMSession, nameUrl: (String, java.net.URI)) extends Peer {
  val url = nameUrl._2.toString
  def session(): Session = session
  def name():  String = nameUrl._1
  def topics(): List[Topic] = List()
  def newTopic(name:String): Topic = null

  override def equals(other: Any) =
    other match {
      case p: XVSMPeer => p.url==this.url && p.session==this.session
      case _ => false
    }
}
