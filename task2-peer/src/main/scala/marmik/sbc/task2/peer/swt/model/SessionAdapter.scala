package marmik.sbc.task2.peer.swt.model

import marmik.sbc.task2.peer.{Peer => ScalaPeer, Posting => ScalaPosting, Session => ScalaSession}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer, Posting => SwtPosting, Session => SwtSession}
import marmik.sbc.task2.peer.swt.model.PeerAdapter.toSwtPeer

import marmik.sbc.task2.peer.swt.JFaceHelpers.{asRunnable, withRealm}
import marmik.sbc.task2.peer.swt.model.PeerAdapter.toWritablePeerList

object SessionAdapter {
  implicit def toSwtSession(session: ScalaSession): SwtSession = {
    val peers = toWritablePeerList(session.peers)
    session.registerListener(new Listener() {
      def peerJoins(peer: ScalaPeer) = withRealm(() => peers.add(peer): Unit)
      def peerLeaves(peer: ScalaPeer) = withRealm(() => peers.remove(peer): Unit)

      def postingCreated(posting: ScalaPosting) {

      }
      def postingEdited(posting: ScalaPosting) {

      }
    })
    new SwtSession(session, peers, session.localPeer)
  }
}
