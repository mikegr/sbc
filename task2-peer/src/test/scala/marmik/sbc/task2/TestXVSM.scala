package marmik.sbc.task2

import junit.framework._
import junit.framework.Assert._

import java.net.URI

import org.xvsm.configuration.ConfigurationManager
import org.xvsm.core._
import org.xvsm.interfaces._

import marmik.sbc.task2.peer.xvsm._

class TestXVSM extends TestCase {

  def testLogin() {
    Console println "Start Test"
    // start super peer
    //org.xvsm.server.Server.main(Array("/opt/mozartspaces/spaces.prop"));
    //login
    //Thread.sleep(1000);
    ConfigurationManager.init(null);
    var cmg = ConfigurationManager.getInstance();
    val uriSetting =  "TcpJava.uri";


    val superPeerUrl = "tcpjava://localhost:56471";
    cmg.setStringSetting(uriSetting, superPeerUrl);
    val superpeer = new Capi();


    val peerUrl = "tcpjava://localhost:56472";
    cmg = ConfigurationManager.getInstance();
    cmg.setStringSetting(uriSetting, peerUrl);
    val peer = new Capi();


    val myselfUrl = "tcpjava://localhost:56473";
    cmg = ConfigurationManager.getInstance();
    cmg.setStringSetting(uriSetting, myselfUrl);
    val myself = new Capi();

    myself.createTransaction(new URI(peerUrl), ICapi.INFINITE_TIMEOUT);

    val sessionFactory = new XVSMSessionFactory();

    val peerSession = sessionFactory.login(superPeerUrl, "Peer 1");
    peerSession.localPeer.newTopic("How to use OOP and Multithreading properly");
    assertEquals("1 peer (ourselves)", 1, peerSession.peers.size);

    assertEquals("1 Topic after newTopic", 1, peerSession.localPeer.topics.size);


    val mySelfSession = sessionFactory.login(superPeerUrl, "MySelf");
    assertEquals("2 peers including ourselves after login", 2 , mySelfSession.peers.size);

    mySelfSession.asInstanceOf[XVSMSession].dumpTopics;

    val peerFromSelf = mySelfSession.peers.find(_ != mySelfSession.localPeer).get

    val peerTopics = peerFromSelf.topics;

    assertEquals("Peer must have one topic", 1, peerTopics.size);



    peerTopics(0).createPosting("From myself", "Singletons are evil", "They are not testable!!!");
    assertEquals("1 posting after create Posting", 1, peerTopics(0).postings);

    val local = mySelfSession.localPeer;
    val ge = local.newTopic("General");
    val go = local.newTopic("Gossip");
    val topics = local.topics();
    assertEquals("Count topics:", 2, topics.size);
    val topic = topics(0);
    val post1 = topic.createPosting("Author", "Post1", "Ein wenig Inhalt");
    val post2 = topic.createPosting("Author", "Post2", "Ein wenig Inhalt");

    assertEquals("Count posts", 2, topic.postings().size);

    mySelfSession.logout; //shutdown space
    peerSession.logout; //shutdown space

    superpeer.shutdown(null, false);

    Console println "End Test"
  }

}



