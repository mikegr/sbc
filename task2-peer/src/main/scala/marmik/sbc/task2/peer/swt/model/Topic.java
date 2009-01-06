package marmik.sbc.task2.peer.swt.model;

import java.util.List;

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

  public List<Posting> getPostings() {
    return scalaz.javas.List.ScalaList_JavaList(PostingAdapter.toSwtPostingList(backing.postings()));
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
