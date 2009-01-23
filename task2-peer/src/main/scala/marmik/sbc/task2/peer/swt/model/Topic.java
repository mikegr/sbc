package marmik.sbc.task2.peer.swt.model;

import org.eclipse.core.databinding.observable.list.WritableList;

import java.util.List;


public class Topic extends ModelObject implements SidebarEntry {
  private marmik.sbc.task2.peer.Topic backing;
  private WritableList postings;
  private boolean subscribed;
  int newPostings;

  public Topic(marmik.sbc.task2.peer.Topic backing) {
    this.backing = backing;
    refresh();
  }

  public marmik.sbc.task2.peer.Topic getBacking() {
    return backing;
  }

  public void refresh() {
    backing.refresh();
    this.postings = new WritableList(
        scalaz.javas.List.ScalaList_JavaList(PostingAdapter.toSwtPostingList(backing.postings()).toList()), Posting.class);
    fireTopLevelPostingChanged(null);
  }

  public String getName() {
    return backing.name();
  }

  public boolean isSubscribed() {
    return subscribed;
  }

  public int getNewPostings() {
    return newPostings;
  }

  public void resetNewPostings() {
    newPostings = 0;
  }

  public void setSubscribed(boolean subscribed) {
    if(subscribed != this.subscribed) {
      boolean oldValue = this.subscribed;
      this.subscribed = subscribed;
      firePropertyChange("subscribed", oldValue, subscribed);
      if(subscribed)
        backing.subscribe();
      else
        backing.unsubscribe();
    }
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Topic)) return false;

    Topic topic = (Topic) o;

    if (backing != null ? !backing.equals(topic.backing) : topic.backing != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return backing != null ? backing.hashCode() : 0;
  }

  public void fireTopLevelPostingChanged(Posting previous) {
    firePropertyChange("topLevelPosting", previous, getTopLevelPosting());
  }
}
