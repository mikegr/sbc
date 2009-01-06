package marmik.sbc.task2.peer.swt.model

import marmik.sbc.task2.peer.{Peer => ScalaPeer, Posting => ScalaPosting}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer, Posting => SwtPosting}

import scalaz.javas.List.ScalaList_JavaList

object PostingAdapter {
  implicit def toSwtPosting(posting: ScalaPosting): SwtPosting = new SwtPosting(posting)

  implicit def toSwtPostingList(postings: List[ScalaPosting]): List[SwtPosting] = postings.map(new SwtPosting(_))
}
