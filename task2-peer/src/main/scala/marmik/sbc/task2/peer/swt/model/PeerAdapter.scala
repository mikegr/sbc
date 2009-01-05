package marmik.sbc.task2.peer.swt.model

import org.eclipse.core.databinding.observable.list.WritableList;

import marmik.sbc.task2.peer.{Peer => ScalaPeer, Topic => ScalaTopic}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer, Topic => SwtTopic}

import scalaz.javas.List.ScalaList_JavaList
import marmik.sbc.task2.peer.swt.model.TopicAdapter.toSwtTopicList

object PeerAdapter {
  implicit def toSwtPeer(peer: ScalaPeer): SwtPeer = new SwtPeer(peer, scalaTopics2WritableList(peer.topics))

  implicit def toSwtPeerList(peers: List[ScalaPeer]): List[SwtPeer] = peers.map(toSwtPeer(_))

  def scalaTopics2WritableList(topics: List[ScalaTopic]): WritableList =
    new WritableList(toSwtTopicList(topics), classOf[ScalaTopic])

  def swtTopics2WritableList(topics: List[SwtTopic]): WritableList =
    new WritableList(topics, classOf[SwtTopic])

}
