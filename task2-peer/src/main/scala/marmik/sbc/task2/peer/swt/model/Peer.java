package marmik.sbc.task2.peer.swt.model;

import org.eclipse.core.databinding.observable.list.WritableList;

public class Peer extends ModelObject implements SidebarEntry {
  private marmik.sbc.task2.peer.Peer backing;
  private WritableList topics;

  public Peer(marmik.sbc.task2.peer.Peer backing, WritableList topics) {
    this.backing = backing;
    this.topics = topics;
  }

  public marmik.sbc.task2.peer.Peer getBacking() {
    return backing;
  }

  public String getName() {
    return backing.name();
  }

  public WritableList getTopics() {
    return topics;
  }

  public boolean isLocal() {
    return backing.isLocal();
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Peer)
      return ((Peer)obj).backing == backing;
    else
      return false;
  }

  @Override
  public int hashCode() {
    return backing.hashCode();
  }
}
