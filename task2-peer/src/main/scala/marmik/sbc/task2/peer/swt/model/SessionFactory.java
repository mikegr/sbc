package marmik.sbc.task2.peer.swt.model;

public class SessionFactory extends ModelObject {
  private marmik.sbc.task2.peer.SessionFactory backing;

  public SessionFactory(marmik.sbc.task2.peer.SessionFactory backing) {
    this.backing = backing;
  }

  public marmik.sbc.task2.peer.SessionFactory getBacking() {
    return backing;
  }

  public String getName() {
    return backing.name();
  }
}
