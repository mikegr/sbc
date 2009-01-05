package marmik.sbc.task2.peer.swt.model;

import java.util.List;
import org.eclipse.core.databinding.observable.list.WritableList;

import marmik.sbc.task2.peer.Listener;

public class Session extends ModelObject {
  private marmik.sbc.task2.peer.Session backing;
  private WritableList peers;

  public Session(marmik.sbc.task2.peer.Session backing, WritableList peers) {
    this.backing = backing;
//    backing.registerListener(new Listener() {
//      public void peerJoins(marmik.sbc.task2.peer.Peer peer) {
//      }
//
//      public void peerLeaves(marmik.sbc.task2.peer.Peer peer) {
//      }
//
//      public void postingCreated(marmik.sbc.task2.peer.Posting posting) {
//      }
//
//      public void postingEdited(marmik.sbc.task2.peer.Posting posting) {
//      }
//    });
  }

  public marmik.sbc.task2.peer.Session getBacking() {
    return backing;
  }

  public void setPeers(WritableList peers) {
    firePropertyChange("peers", this.peers, this.peers = peers);
  }

  public WritableList getPeers() {
    return peers;
  }
}
