package marmik.sbc.task2.peer.swt.model;

import java.util.List;

public class Posting extends ModelObject {
  private marmik.sbc.task2.peer.Posting backing;

  Posting() {

  }

  public Posting(marmik.sbc.task2.peer.Posting backing) {
    this.backing = backing;
  }

  public marmik.sbc.task2.peer.Posting getBacking() {
    return backing;
  }

  public String getAuthor() {
    return backing.author();
  }

  public String getSubject() {
    return backing.subject();
  }

  public String getContent() {
    return backing.content();
  }

  public Topic getTopic() {
    return TopicAdapter.toSwtTopic(backing.topic());
  }

  public Posting getParent() {
    return PostingAdapter.toSwtPosting(backing.parent());
  }

  public List<Posting> getReplies() {
    return scalaz.javas.List.ScalaList_JavaList(PostingAdapter.toSwtPostingList(backing.replies()).toList());
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
