package marmik.sbc.task2.peer.swt.model

import org.eclipse.core.databinding.observable.list.WritableList;

import marmik.sbc.task2.peer.{Peer => ScalaPeer, Posting => ScalaPosting, Topic => ScalaTopic}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer, Topic => SwtTopic}

import scalaz.javas.List.ScalaList_JavaList
import marmik.sbc.task2.peer.swt.JFaceHelpers.{asRunnable, withRealm}
import marmik.sbc.task2.peer.swt.model.TopicAdapter.{toSwtTopic, toSwtTopicList}

object PeerAdapter {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  implicit def toSwtPeer(peer: ScalaPeer): SwtPeer = {
    val swtPeer = new SwtPeer(peer, scalaTopics2WritableList(peer.topics))

    peer.session.registerListener(new Listener() {
      def peerJoins(peer: ScalaPeer) = {}
      def peerLeaves(peer: ScalaPeer) = {}
      def postingCreated(posting: ScalaPosting) = {}
      def postingEdited(posting: ScalaPosting) = {}
      def topicCreated(eventPeer: ScalaPeer, topic: ScalaTopic) {
        if(peer==eventPeer)
          withRealm(() => swtPeer.getTopics.add(toSwtTopic(topic)): Unit)
      }
    })

    swtPeer
  }

  implicit def toSwtPeerList(peers: List[ScalaPeer]): List[SwtPeer] = peers.map(toSwtPeer(_))

  def scalaTopics2WritableList(topics: List[ScalaTopic]): WritableList =
    new WritableList(toSwtTopicList(topics), classOf[ScalaTopic])

  def swtTopics2WritableList(topics: List[SwtTopic]): WritableList =
    new WritableList(topics, classOf[SwtTopic])

}
