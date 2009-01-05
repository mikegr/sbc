package marmik.sbc.task2.peer.swt.model

import marmik.sbc.task2.peer.{Peer => ScalaPeer, Session => ScalaSession}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer, Session => SwtSession}
import scalaz.javas.List.ScalaList_JavaList

import org.eclipse.core.databinding.observable.list.WritableList;

class SessionAdapter(session: ScalaSession) {
  def toSwtModel(): SwtSession = {
    new SwtSession(session, scalaPeersToWritableList(session.peers))
  }

  protected def scalaPeersToSwtPeers(peers: List[ScalaPeer]): List[SwtPeer] = peers.map(new SwtPeer(_))

  protected def scalaPeersToWritableList(peers: List[ScalaPeer]): WritableList =
    new WritableList(scalaPeersToSwtPeers(peers), classOf[SwtPeer])
}
