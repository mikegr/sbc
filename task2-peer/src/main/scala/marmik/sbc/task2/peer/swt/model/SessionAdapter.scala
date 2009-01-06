package marmik.sbc.task2.peer.swt.model

import org.eclipse.core.databinding.observable.list.WritableList;

import marmik.sbc.task2.peer.{Peer => ScalaPeer, Posting => ScalaPosting, Session => ScalaSession, Topic => ScalaTopic}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer, Posting => SwtPosting}
import marmik.sbc.task2.peer.swt.model.{Session => SwtSession, Topic => SwtTopic}
import marmik.sbc.task2.peer.swt.model.PeerAdapter.{toSwtPeer, toSwtPeerList}
import marmik.sbc.task2.peer.swt.model.TopicAdapter.toSwtTopic

import scalaz.javas.List.ScalaList_JavaList
import marmik.sbc.task2.peer.swt.JFaceHelpers.{asRunnable, withRealm}

object SessionAdapter {
  implicit def toSwtSession(session: ScalaSession): SwtSession = {
    val sessionPeers = session.peers();
    val peers = scalaPeers2WritableList(sessionPeers);

    val topicEntries: List[SidebarEntry] = for(peer <- sessionPeers; topic <- peer.topics) yield topic
    val sidebarEntries = toSwtPeerList(sessionPeers).union(topicEntries)

    val swtSession = new SwtSession(session, peers, session.localPeer, new WritableList(sidebarEntries, classOf[SidebarEntry]))

    session.registerListener(new Listener() {
      def peerJoins(peer: ScalaPeer) = withRealm(() => {
        peers.add(peer)
        swtSession.getSidebarEntries.add(toSwtPeer(peer))
      }: Unit)
      def peerLeaves(peer: ScalaPeer) = withRealm(() => {
        peers.remove(peer)
        swtSession.getSidebarEntries.remove(toSwtPeer(peer))
      }: Unit)

      def postingCreated(posting: ScalaPosting) {

      }
      def postingEdited(posting: ScalaPosting) {

      }
      def topicCreated(peer: ScalaPeer, topic: ScalaTopic) {
          withRealm(() => swtSession.getSidebarEntries.add(toSwtTopic(topic)): Unit)
      }
    })

    swtSession
  }

  def scalaPeers2WritableList(peers: List[ScalaPeer]): WritableList =
    new WritableList(toSwtPeerList(peers), classOf[SwtPeer])

  def swtPeers2WritableList(peers: List[SwtPeer]): WritableList =
    new WritableList(peers, classOf[SwtPeer])
}
