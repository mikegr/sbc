package marmik.sbc.task2.peer.swt.model;

public class SidebarEntry extends ModelObject {
  private Topic topic;
  private Peer peer;

  public SidebarEntry(Topic topic) {
    this.topic = topic;
  }
  public SidebarEntry(Peer peer) {
    this.peer = peer;
  }

  public boolean hasTopic() {
    return topic != null;
   }

  public boolean hasPeer() {
    return peer != null;
  }

  public String getName() {
    if(peer==null)
      return topic.getName();
    else
      return peer.getName();
  }

  public Topic getTopic() {
    if(hasTopic())
      return topic;
    else
      throw new IllegalStateException("Doesn't contain Topic");
  }

  public Peer getPeer() {
    if(hasPeer())
      return peer;
    else
      throw new IllegalStateException("Doesn't contain Peer");
  }
}
