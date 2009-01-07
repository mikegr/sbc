package marmik.sbc.task2.peer.xvsm2

import marmik.sbc.task2.peer._

class XVSMTopic extends Topic {
  def name(): String = null
  def peer(): Peer = null

  def postings(): List[Posting] = null
  def subscribe() { }
  def unsubscribe() { }

  def createPosting(author:String, subject:String, content:String): Posting = null
}
