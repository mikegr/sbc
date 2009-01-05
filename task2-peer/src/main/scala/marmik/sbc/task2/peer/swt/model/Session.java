package marmik.sbc.task2.peer.swt.model;

import java.util.List;
import org.eclipse.core.databinding.observable.list.WritableList;

public class Session extends ModelObject {
  private marmik.sbc.task2.peer.Session backing;
  private WritableList peers;
  private WritableList sidebar;
  private Peer localPeer;

  public Session(marmik.sbc.task2.peer.Session backing, WritableList peers, Peer localPeer, WritableList sidebar) {
    this.backing = backing;
    this.peers = peers;
    this.localPeer = localPeer;
    this.sidebar = sidebar;
  }

  public marmik.sbc.task2.peer.Session getBacking() {
    return backing;
  }

  public WritableList getSidebarEntries() {
    return sidebar;
  }

  public WritableList getPeers() {
    return peers;
  }

  public Peer getLocalPeer() {
    return localPeer;
  }
}
