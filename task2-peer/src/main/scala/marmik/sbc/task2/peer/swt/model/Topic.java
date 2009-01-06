package marmik.sbc.task2.peer.swt.model;

import org.eclipse.core.databinding.observable.list.WritableList;

public class Topic extends ModelObject implements SidebarEntry {
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

  public WritableList getPostings() {
    return PostingAdapter.toSwtPostingList(backing.postings());
  }

  public Peer getPeer() {
    return PeerAdapter.toSwtPeer(backing.peer());
  }

  public Posting createPosting(String author, String subject, String content) {
    return PostingAdapter.toSwtPosting(backing.createPosting(author, subject, content));
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Topic)
      return ((Topic)obj).backing == backing;
    else
      return false;
  }

  @Override
  public int hashCode() {
    return backing.hashCode();
  }
}
