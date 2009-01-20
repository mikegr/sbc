package marmik.sbc.task2.peer.swt.model;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import java.util.List;

public class Session extends ModelObject {
  private marmik.sbc.task2.peer.Session backing;
  private WritableList peers;
  private WritableList sidebar;
  private Peer localPeer;
  private TableViewer sidebarViewer;

  public Session(marmik.sbc.task2.peer.Session backing, WritableList peers, Peer localPeer, WritableList sidebar) {
    this.backing = backing;
    this.peers = peers;
    this.localPeer = localPeer;
    this.sidebar = sidebar;
  }

  public marmik.sbc.task2.peer.Session getBacking() {
    return backing;
  }

  public WritableList getSidebarEntries() {
    return sidebar;
  }

  public WritableList getPeers() {
    return peers;
  }

  public Peer getLocalPeer() {
    return localPeer;
  }

  public void setSidebarViewer(TableViewer viewer) {
    this.sidebarViewer = viewer;
  }

  public void setWordlist(List<String> wordlist) {
    backing.setBadWordList(scalaz.javas.List.JavaList_ScalaList(wordlist));
  }

  public List<String> getWordlist() {
    return (List<String>) scalaz.javas.List.ScalaList_JavaList(backing.getBadWordList().toList());
  }

  public void firePostingCreated(Posting posting) {
    for(Object e : sidebar) {
      if(e instanceof Topic) {
        Topic t = (Topic) e;
        if(t.equals(posting.getTopic())) {
          t.newPostings += 1;
          Posting previous = t.getTopLevelPosting();
          t.getPostings().add(posting);
          t.fireTopLevelPostingChanged(previous);
          if(sidebarViewer != null) {
            Topic selected = (Topic) ((IStructuredSelection) sidebarViewer.getSelection()).getFirstElement();
            if(selected != null)
              selected.resetNewPostings();
            sidebarViewer.refresh(true);
          }
        }
      }
    }
    for(Object p : peers) {
      Peer peer = (Peer) p;
      for(Object t : peer.getTopics()) {
        Topic topic = (Topic) t;
        if(topic.equals(posting.getTopic())) {
          Posting previous = topic.getTopLevelPosting();
          topic.getPostings().add(posting);
          topic.fireTopLevelPostingChanged(previous);
        }
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Session)
      return ((Session) obj).backing == backing;
    else
      return false;
  }

  @Override
  public int hashCode() {
    return backing.hashCode();
  }
}
