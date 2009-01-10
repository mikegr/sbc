package marmik.sbc.task2.peer.swt.model;

import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;


public class Topic extends ModelObject implements SidebarEntry {
  private marmik.sbc.task2.peer.Topic backing;
  private WritableList postings;

  public Topic(marmik.sbc.task2.peer.Topic backing) {
    this.backing = backing;
    this.postings = new WritableList(
        scalaz.javas.List.ScalaList_JavaList(PostingAdapter.toSwtPostingList(backing.postings()).toList()), Posting.class);
  }

  public marmik.sbc.task2.peer.Topic getBacking() {
    return backing;
  }

  public String getName() {
    return backing.name();
  }

  public WritableList getPostings() {
    return postings;
  }

  public Posting getTopLevelPosting() {
    return new Posting(null) {

      @Override
      public boolean equals(Object obj) {
        return this == obj;
      }

      @Override
      public String getAuthor() {
        return "TOP LEVEL";
      }

      @Override
      public String getContent() {
        return "TOP LEVEL";
      }

      @Override
      public Posting getParent() {
        return null;
      }

      @Override
      public List<Posting> getReplies() {
        return Topic.this.getPostings();
      }

      @Override
      public String getSubject() {
        return "TOP LEVEL";
      }

      @Override
      public int hashCode() {
        return Topic.this.hashCode();
      }

    };
  }

  public Peer getPeer() {
    return PeerAdapter.toSwtPeer(backing.peer());
  }

  public Posting createPosting(String subject, String content) {
    return PostingAdapter.toSwtPosting(backing.createPosting(backing.peer().name(), subject, content));
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

  public void fireTopLevelPostingChanged(Posting previous) {
    firePropertyChange("topLevelPosting", previous, getTopLevelPosting());
  }
}
