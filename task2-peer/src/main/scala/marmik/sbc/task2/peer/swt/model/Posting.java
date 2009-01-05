package marmik.sbc.task2.peer.swt.model;

public class Posting extends ModelObject {
  private marmik.sbc.task2.peer.Posting backing;

  public Posting(marmik.sbc.task2.peer.Posting backing) {
    this.backing = backing;
  }

  public marmik.sbc.task2.peer.Posting getBacking() {
    return backing;
  }
}
