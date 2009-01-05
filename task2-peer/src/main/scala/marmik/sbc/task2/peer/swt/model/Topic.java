package marmik.sbc.task2.peer.swt.model;

public class Topic extends ModelObject {
  private marmik.sbc.task2.peer.Topic backing;

  public Topic(marmik.sbc.task2.peer.Topic backing) {
    this.backing = backing;
  }

  public marmik.sbc.task2.peer.Topic getBacking() {
    return backing;
  }

  public String getName() {
    return backing.name();
  }

  public Peer getPeer() {
    return PeerAdapter.toSwtPeer(backing.peer());
  }
}
