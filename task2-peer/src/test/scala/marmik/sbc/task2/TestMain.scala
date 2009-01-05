package marmik.sbc.task2

import marmik.sbc.task2.peer._
import marmik.sbc.task2.peer.xvsm._
import marmik.sbc.task2.peer.xvsm.XVSMSessionFactory

import junit.framework._
import junit.framework.Assert._

import java.net.URI
import java.rmi.server._

object TestMain {

  def testXVSM() {

    println("Start")
    org.xvsm.server.Server.main(Array("spaces1.prop")); //superpeer
    org.xvsm.server.Server.main(Array("spaces2.prop")); //peer1

    val peer1Name = "Peer1"
    val peer2Name = "Peer2"

    val superPeerUrl = "tcpjava://localhost:56473";
    val peer1Url = "tcpjava://localhost:56474";
    val peer2Url = "tcpjava://localhost:56475";

    val factory = new XVSMSessionFactory();

    val session1 = factory.login(superPeerUrl, peer1Name, peer1Url);
      val localPeer1 = session1.localPeer;
      val topic1 = localPeer1.newTopic("Peer1/Topic1");


      val session2 = factory.login(superPeerUrl, peer2Name, peer2Url);
      val localPeer2 = session2.localPeer;
      val topic2 = localPeer2.newTopic("Peer2/Topic1");


      val peersFrom2 = session2.peers;
      assertEquals("2 peers", 2, peersFrom2.size);
      val peer1From2 = peersFrom2.find(x=> x == localPeer1).get;
      assertEquals("Check peer1 from peer2", peer1Name, peer1From2.name);

      assertEquals("Check Topic1", "Peer1/Topic1", peer1From2.topics.first.name);

      var calls:Int = 0

      session2.registerListener(new Listener() {
                            def peerJoins(peer: Peer) {
                              calls += 1;
                            };
                            def peerLeaves(peer: Peer) {
                              calls += 1;
                            };
                            def postingCreated(posting: Posting) {
                              calls += 1;
                            };
                            def postingEdited(posting: Posting) {
                              calls += 1;
                            };
                            def topicCreated(peer: Peer, topic: Topic) {
                              // TODO: Implement
                            }
                        });

      peer1From2.topics.first.subscribe;

      peer1From2.topics.first.createPosting("Peer2Author", "SubjectX", "ContentFrom2");

      val postCheckedBy1 = localPeer1.topics.first.postings.first;
      assertEquals("Check post from peer2 in peer1", "ContentFrom2",
                   postCheckedBy1.content);

      //check a reply

      postCheckedBy1.reply("Peer1Author", "SubjectReply", "ReplyFrom1" );

      peer1From2.asInstanceOf[XVSMPeer].dumpPostings;

      assertEquals("Check reply of peer1 by peer2", "ReplyFrom1",
             peer1From2.topics.first.postings.first.replies.first.content);

      //check edit
      postCheckedBy1.replies.first.edit("NewContent");
      assertEquals("Check edit of peer1 by peer2", "NewContent",
             peer1From2.topics.first.postings.first.replies.first.content);



      session1.logout;
      session2.logout;



    //start local
    //junit.textui.TestRunner.run(classOf[TestXVSM]);
    println("end")
    System.exit(0)
  }


  def main(args:Array[String]) {
    testXVSM();
  }

}


