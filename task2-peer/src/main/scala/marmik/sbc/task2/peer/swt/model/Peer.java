package marmik.sbc.task2.peer.swt.model;

import org.eclipse.core.databinding.observable.list.WritableList;

public class Peer extends ModelObject {
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
}
