package marmik.sbc.task2.peer.xvsm2

import marmik.xvsm._
import marmik.sbc.task2.peer._
import org.xvsm.selectors._

class XVSMTopic(val elevator: SpaceElevator, val session: XVSMSession, val peer: XVSMPeer, val name: String, val postings: List[XVSMPosting]) extends Topic {

  def subscribe() { }
  def unsubscribe() { }

  def createPosting(author:String, subject:String, content:String): Posting = null

  override def equals(obj: Any) = obj match {
    case t: XVSMTopic => t.peer == this.peer && t.name == this.name
    case _ => false
  }

  override def hashCode = this.name.hashCode + this.peer.hashCode
}
