package marmik.sbc.task2.peer.swt.model

import org.eclipse.core.databinding.observable.list.WritableList;

import marmik.sbc.task2.peer.{Peer => ScalaPeer}
import marmik.sbc.task2.peer.swt.model.{Peer => SwtPeer}

import scalaz.javas.List.ScalaList_JavaList

object PeerAdapter {
  implicit def toSwtPeer(peer: ScalaPeer): SwtPeer = new SwtPeer(peer)

  implicit def toSwtPeerList(peers: List[ScalaPeer]): List[SwtPeer] = peers.map(new SwtPeer(_))

  def toWritablePeerList(peers: List[ScalaPeer]): WritableList =
    new WritableList(toSwtPeerList(peers), classOf[SwtPeer])

  // def toWritablePeerList(peers: List[SwtPeer]): WritableList =
  //   new WritableList(peers, classOf[SwtPeer])
}
