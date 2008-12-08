package marmik.sbc.task2

import junit.framework._
import junit.framework.Assert._

import marmik.sbc.task2.peer.xvsm._

class TestXVSM extends TestCase {
  
  def testLogin() {
    Console println "Start Test"
    // start super peer
    //org.xvsm.server.Server.main(Array("/opt/mozartspaces/spaces.prop"));
    //login
    //Thread.sleep(1000);
    val superpeer = "tcpjava://localhost:4321";
    val session = new XVSMSessionFactory().login(superpeer,"Peer1","tcpjava://localhost:1234");
    val peers = session.peers;
    assertEquals("Count peers:", 1, peers.size);
    val local = session.localPeer;
    val ge = local.newTopic("General");
    val go = local.newTopic("Gossip");
    val topics = local.topics();
    assertEquals("Count topics:", 2, topics.size);
    val topic = topics(0);
    val post1 = topic.createPosting("Author", "Post1", "Ein wenig Inhalt");
    val post2 = topic.createPosting("Author", "Post2", "Ein wenig Inhalt");
    
    assertEquals("Count posts", 2, topic.postings().size);
    
    session.logout;
    
    
    
    Console println "End Test"
  }
  
}



