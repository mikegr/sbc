package marmik.sbc.task2.peer.swt.model;

public class Peer extends ModelObject {
  private marmik.sbc.task2.peer.Peer backing;

  public Peer(marmik.sbc.task2.peer.Peer backing) {
    this.backing = backing;
  }

  public marmik.sbc.task2.peer.Peer getBacking() {
    return backing;
  }


}
