package marmik.sbc.task2.peer.rmi

import junit.framework._
import junit.framework.Assert._

import java.net.URI
import java.rmi.server._

import marmik.sbc.task2.peer._
import marmik.sbc.task2.peer.rmi._


class TestRmi extends TestCase {

   def testRmi() {

      //setup superPeer
      val superPeerUrl = "rmi://localhost/superpeer";
      val peer1Url = "rmi://localhost/peer1"
      val peer2Url = "rmi://localhost/peer2"
      val peer1Name = "Peer1"
      val peer2Name = "Peer2"

      val superPeer = SuperPeerMain.setup(superPeerUrl);

      val sf1:SessionFactory = new RmiSessionFactory();
      val session1 = sf1.login(superPeerUrl, peer1Name);
      val localPeer1 = session1.localPeer;
      val topic1 = localPeer1.newTopic("Peer1/Topic1");

      val sf2:SessionFactory = new RmiSessionFactory();
      val session2 = sf2.login(superPeerUrl, peer2Name);
      val localPeer2 = session2.localPeer;
      val topic2 = localPeer2.newTopic("Peer2/Topic1");


      assertEquals("2 peers", 2, session2.peers.size);
      val peer1From2 = session2.peers.find(x=> x != localPeer1).get;
      assertEquals("Check peer1 from peer2", peer1Name, peer1From2.name);

      assertEquals("Check Topic1", "Peer1/Topic1", peer1From2.topics.first.name);

      peer1From2.topics.first.createPosting("Peer2Author", "SubjectX", "ContentFrom2");

      val postCheckedBy1 = localPeer1.topics.first.postings.first;
      assertEquals("Check post from peer2 in peer1", "ContentFrom2",
                   postCheckedBy1.content);

      //check a reply

      postCheckedBy1.reply("Peer1Author", "SubjectReply", "ReplyFrom1" );

      assertEquals("Check reply of peer1 by peer2", "ReplyFrom1",
             peer1From2.topics.first.postings.first.replies.first.content);

      //check edit
      postCheckedBy1.replies.first.edit("NewContent");
      assertEquals("Check edit of peer1 by peer2", "NewContent",
             peer1From2.topics.first.postings.first.replies.first.content);


      //TODO: check listeners

      session1.logout;
      session2.logout;
      UnicastRemoteObject.unexportObject(superPeer, true);

   }
}
