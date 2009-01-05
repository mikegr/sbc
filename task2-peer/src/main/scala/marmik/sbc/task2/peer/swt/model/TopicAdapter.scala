package marmik.sbc.task2.peer.swt.model

import marmik.sbc.task2.peer.{Peer => ScalaPeer, Topic => ScalaTopic}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer, Topic => SwtTopic}

import scalaz.javas.List.ScalaList_JavaList

object TopicAdapter {
  implicit def toSwtTopic(topic: ScalaTopic): SwtTopic = new SwtTopic(topic)

  implicit def toSwtTopicList(topics: List[ScalaTopic]): List[SwtTopic] = topics.map(new SwtTopic(_))

}
