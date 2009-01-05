package marmik.sbc.task2.peer.swt.model

import org.eclipse.core.databinding.observable.list.WritableList;

import marmik.sbc.task2.peer.{Peer => ScalaPeer, Posting => ScalaPosting, Session => ScalaSession, Topic => ScalaTopic}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer, Posting => SwtPosting}
import marmik.sbc.task2.peer.swt.model.{Session => SwtSession, Topic => SwtTopic}
import marmik.sbc.task2.peer.swt.model.PeerAdapter.{toSwtPeer, toSwtPeerList}

import scalaz.javas.List.ScalaList_JavaList
import marmik.sbc.task2.peer.swt.JFaceHelpers.{asRunnable, withRealm}

object SessionAdapter {
  implicit def toSwtSession(session: ScalaSession): SwtSession = {
    val peers = scalaPeers2WritableList(session.peers)
    session.registerListener(new Listener() {
      def peerJoins(peer: ScalaPeer) = withRealm(() => peers.add(peer): Unit)
      def peerLeaves(peer: ScalaPeer) = withRealm(() => peers.remove(peer): Unit)

      def postingCreated(posting: ScalaPosting) {

      }
      def postingEdited(posting: ScalaPosting) {

      }
      def topicCreated(peer: ScalaPeer, topic: ScalaTopic) {
      }
    })
    new SwtSession(session, peers, session.localPeer)
  }

  def scalaPeers2WritableList(peers: List[ScalaPeer]): WritableList =
    new WritableList(toSwtPeerList(peers), classOf[SwtPeer])

  def swtPeers2WritableList(peers: List[SwtPeer]): WritableList =
    new WritableList(peers, classOf[SwtPeer])
}
