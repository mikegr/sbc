package marmik.sbc.task2.peer.swt.model;

public class Posting extends ModelObject {
  private marmik.sbc.task2.peer.Posting backing;

  public Posting(marmik.sbc.task2.peer.Posting backing) {
    this.backing = backing;
  }

  public marmik.sbc.task2.peer.Posting getBacking() {
    return backing;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Posting)
      return ((Posting)obj).backing == backing;
    else
      return false;
  }

  @Override
  public int hashCode() {
    return backing.hashCode();
  }
}
