package marmik.sbc.task2.peer.swt.model;

import java.util.List;
import org.eclipse.core.databinding.observable.list.WritableList;

public class Session extends ModelObject {
  private marmik.sbc.task2.peer.Session backing;
  private WritableList peers;
  private Peer localPeer;

  public Session(marmik.sbc.task2.peer.Session backing, WritableList peers, Peer localPeer) {
    this.backing = backing;
    this.localPeer = localPeer;
  }

  public marmik.sbc.task2.peer.Session getBacking() {
    return backing;
  }

  public WritableList getPeers() {
    return peers;
  }

  public Peer getLocalPeer() {
    return localPeer;
  }
}
