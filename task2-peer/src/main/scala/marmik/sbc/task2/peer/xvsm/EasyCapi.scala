package marmik.sbc.task2.peer.xvsm

import java.util.GregorianCalendar
import org.xvsm.interfaces._
import org.xvsm.core._
import org.xvsm.core.Tuple
import org.xvsm.core._
import org.xvsm.core.notifications._
import org.xvsm.core.aspect._
import org.xvsm.core.aspect.LocalIPoint

import org.xvsm.transactions._
import org.xvsm.internal.exceptions._
import org.xvsm.coordinators._
import org.xvsm.selectors._
import org.xvsm.interfaces._
import org.xvsm.interfaces.container._

import marmik.sbc.task2.peer.xvsm.XVSMContants._


import scala.collection.mutable._

import java.net.URI

class EasyCapi(val capi:ICapi, val session:XVSMSession, superPeer:URI, selfName:String) extends NotificationListenerAdapter {

  val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName);

  val selfUrl = { val tmpURL = postingContainer(null, null).asURI;
                  "tcpjava://" + tmpURL.getHost() + ":" + tmpURL.getPort();}

  /** Writes entry within a transaction*/
  def writePeerInfo() {
    dumpPeers()
    dumpTopics()
    log info "writing to " + superPeer;
    val entry = new AtomicEntry[SpacePeer](new SpacePeer(selfUrl, selfName));
    entry.addSelectors(new KeySelector[String]("Url", selfUrl));

    val tx = transaction(superPeer);
    val ct = peerContainer(tx);
    capi.write(ct, 0, tx, entry);
    //TODO: clear topics;
    capi.commitTransaction(tx);

    log info ("Register notification for " + selfUrl);

    capi.createNotification(peerContainer(null), this, Operation.Write, Operation.Shift, Operation.Take, Operation.Destroy);
    capi.createNotification(topicContainer(null), this, Operation.Write, Operation.Shift);

    log debug ("Registration finished for " + selfUrl);
  }

  def readPeerInfo():List[XVSMPeer] = {
    val tx = transaction(superPeer);
    val ct = peerContainer(tx);
    val entries = capi.read(ct, 0, tx, new FifoSelector(Selector.CNT_ALL));
    capi.commitTransaction(tx);
    entries.map(x => {
      val value = x.asInstanceOf[AtomicEntry[SpacePeer]].getValue();
      new XVSMPeer(this, session,
                   value.url,
                   value.name)
    }).toList
  }

  def logout() {
    val tx = transaction(superPeer);
    val ct = peerContainer(tx);
    capi.destroy(ct, 0, tx, new KeySelector("Url", selfUrl));
    capi.commitTransaction(tx);

    capi.shutdown(null, false)
  }

  def postings(topic:XVSMTopic):List[XVSMPosting] = {
    postingsInternal(topic, null);
  }

  def getUri(url:String):URI = if (url == selfUrl) null else new URI(url);

  def postingsInternal(topic:XVSMTopic, posting:XVSMPosting):List[XVSMPosting] = {
    val url = topic.url;
    val topicName = topic.name;
    val postingId:java.lang.Long = if (posting != null) posting.id else null;
    val uri = getUri(url);
    val tx = transaction(uri);
    val ct = postingContainer(tx, uri);
    val template = constructReadingPostingTuple(null, topicName, postingId, null);
    val entries = capi.read(ct, 0, tx, new LindaSelector(Selector.CNT_ALL, template))
    capi.commitTransaction(tx);
    entries.map(x => entry2Posting(topic, posting, x)).toList;
  }


  def entry2Posting(topic:XVSMTopic, parent:XVSMPosting, x:Entry):XVSMPosting = {
    val tuple = x.asInstanceOf[Tuple]
    val idx = tuple.getEntryAt(0).asInstanceOf[AtomicEntry[Long]].getValue;
    val entry = tuple.getEntryAt(3).asInstanceOf[AtomicEntry[SpacePosting]].getValue;
    new XVSMPosting(this, topic, parent, idx, entry.author, entry.subject, entry.content, entry.date);
  }


  def postingContainer(tx:Transaction, uri:URI):ContainerRef =  {
    container(tx, uri, CONTAINER_POSTINGS, Array(new LindaCoordinator(), new LifoCoordinator()));
  }

  def peerContainer(tx:Transaction):ContainerRef =  {
    container(tx, superPeer, CONTAINER_PEERS, Array(new KeyCoordinator(new KeyCoordinator.KeyType("Url", classOf[String]) ), new FifoCoordinator()))
  }
  def topicContainer(tx:Transaction):ContainerRef = {
    container(tx, superPeer, CONTAINER_TOPICS, Array(new LindaCoordinator()));
  }

  def createPosting(topic:XVSMTopic, author:String, subject:String, content:String):Posting =
    createPostingInternal(topic, null, author,subject, content);

  def createPostingInternal (topic:XVSMTopic, parent:XVSMPosting, author:String, subject:String, content:String):XVSMPosting = {
    val url = topic.url;
    val name = topic.name;
    //use LIFO for getting last post id;
    //use Linda for getting postings;
    val uri = if (url == selfUrl) null else new URI(url);
    val tx = transaction(uri);
    val ct = postingContainer(tx, uri);
    val parentId:java.lang.Long = if (parent != null) parent.id else null;

    val date = new GregorianCalendar();

    val newIndex:Long =
    try {
      val lastEntries = capi.read(ct, 0 , tx, new LifoSelector());
      lastEntries(0).asInstanceOf[Tuple].getEntryAt(0).asInstanceOf[AtomicEntry[Long]].getValue() + 1
    }
    catch {
      case e:CountNotMetException => 0;
    }

    val tuple = constructWritingPostingTuple(newIndex, name, parentId, new SpacePosting(author, subject, content, date));
    capi.write(ct, 0, tx, tuple);

    capi.commitTransaction(tx);
    new XVSMPosting(this, topic, parent, newIndex, author, subject, content, date)
  }

  def constructWritingPostingTuple(id:java.lang.Long, topic:java.lang.String, parent:java.lang.Long, post:SpacePosting):Tuple = {
    new Tuple(if (id == null) new AtomicEntry[java.lang.Long](classOf[java.lang.Long])
                else new AtomicEntry[java.lang.Long](id), //ID
              if (topic == null) new AtomicEntry[java.lang.String](classOf[java.lang.String])
                else new AtomicEntry[java.lang.String](topic),  //topic
              if (parent == null) new AtomicEntry[java.lang.Long](classOf[java.lang.Long])
                else new AtomicEntry[java.lang.Long](parent), //Parent
              if (post == null) new AtomicEntry[SpacePosting](classOf[SpacePosting])
                else new AtomicEntry[SpacePosting](post)); //Posting
  }


  def constructReadingPostingTuple(id:java.lang.Long, topic:java.lang.String, parent:java.lang.Long, post:SpacePosting):Tuple = {
    new Tuple(constructAtomicEntry(classOf[java.lang.Long], id), //ID
              constructAtomicEntry(classOf[java.lang.String], topic),  //topic
              constructAtomicEntry(classOf[java.lang.Long], parent), //Parent
              constructAtomicEntry(classOf[SpacePosting], post));
  }

  def constructAtomicEntry[A](clazz: Class[A], value: A): AtomicEntry[A] = {
    if(value == null)
      new AtomicEntry[A](clazz)
    else
      new AtomicEntry[A](value)
  }



  /** Fetch list of topics from super peer
   * @param url URL for XVSMTopic. Should be null for local topic.
   */
  def topics(peer:XVSMPeer):List[XVSMTopic] = {
    val url = peer.url;
    val tx = transaction(superPeer);
    val ct = topicContainer(tx);
    val template = new Tuple(new AtomicEntry[java.lang.String](url), null);

    val entries = capi.read(ct, 0, tx, new LindaSelector(Selector.CNT_ALL, template));
    capi.commitTransaction(tx);
    entries.map{ x =>
      val tuple = x.asInstanceOf[Tuple];
      val name = tuple.getEntryAt(1).asInstanceOf[AtomicEntry[String]].getValue;
      new XVSMTopic(this, peer, name)
    }.toList
  }

  def dumpPeers(){
    Console println ("dumpPeers");
    val tx = transaction(superPeer);
    val ct = peerContainer(tx);

    val entries = capi.read(ct, 0, tx, new FifoSelector(Selector.CNT_ALL));

    capi.commitTransaction(tx);
    entries.foreach{ x:Entry =>
      log debug (x.getClass.toString + x.toString);
      val value:SpacePeer = x.asInstanceOf[AtomicEntry[SpacePeer]].getValue();
      Console println(value.url + " - " + value.name);
    }
  }


  def dumpTopics(){
    Console println ("dumpTopics");
    val tx = transaction(superPeer);
    val ct = topicContainer(tx);
    val template = new Tuple(null, null);

    val entries = capi.read(ct, 0, tx, new LindaSelector(Selector.CNT_ALL, template));
    capi.commitTransaction(tx);
    entries.foreach{ x =>
      val tuple = x.asInstanceOf[Tuple]
      val url = tuple.getEntryAt(0).asInstanceOf[AtomicEntry[String]].getValue;
      val name = tuple.getEntryAt(1).asInstanceOf[AtomicEntry[String]].getValue;
      Console println(url + " - " + name);
    }
  }

  def dumpPostings(url:String) {
    val uri = getUri(url);
    val tx = transaction(uri);
    val ct = postingContainer(tx, uri);
    val template = constructReadingPostingTuple(null, null, null, null);
    val entries = capi.read(ct, 0, tx, new LindaSelector(Selector.CNT_ALL, template))
    capi.commitTransaction(tx);
    entries.foreach{x =>
      val tuple = x.asInstanceOf[Tuple];
      val output:String = tuple.getEntryAt(0).asInstanceOf[AtomicEntry[Long]].getValue +
        tuple.getEntryAt(1).asInstanceOf[AtomicEntry[String]].getValue +
        //tuple.getEntryAt(2).asInstanceOf[AtomicEntry[Long]].getValue +
        tuple.getEntryAt(3).asInstanceOf[AtomicEntry[SpacePosting]].getValue;
      Console.println(output);
    }
  }


  /** @param url URL for new Topic. Should be null for local Topic.
   */

  def newTopic(peer:XVSMPeer, name:String):Topic = {
    val url = peer.url;
    val tx = transaction(superPeer);
    val ct = topicContainer(tx);
    val st = new org.xvsm.core.Tuple(new AtomicEntry[String](selfUrl), new AtomicEntry[String](name));

    capi.write(ct, 0, tx, st);

    capi.commitTransaction(tx);

    new XVSMTopic(this, peer, name);

  }

  def transaction(uri:URI):Transaction = {
    capi.createTransaction(uri, ICapi.INFINITE_TIMEOUT);
  }

  def container(tx:Transaction, uri:URI, container:String, coodinators:Array[ICoordinator]):ContainerRef = (
    try {
      capi.lookupContainer(tx, uri, container);
    }
    catch {
      case e:InvalidContainerException => { //ContainerNameOccupiedException
        //e.printStackTrace();
        capi.createContainer(tx, uri, container, IContainer.INFINITE_SIZE, coodinators: _*);
      };
    }
  )

  def reply(posting:XVSMPosting, author:String, subject:String, content:String):XVSMPosting = {
    createPostingInternal(posting.topic, posting, author, subject, content)
  }

  def replies(posting:XVSMPosting):List[XVSMPosting] = {
    postingsInternal(posting.topic, posting);
  }

  def editPosting(posting:XVSMPosting, newContent:String) {
    val uri = getUri(posting.topic.peer.url);
    val tx = transaction(uri);
    val ct = postingContainer(tx, uri);
    val template = constructReadingPostingTuple(posting.id, null, null, null);
    val entry = capi.take(ct, 0, tx, new LindaSelector(Selector.CNT_ALL, template)).first;
    val spacePosting = entry.asInstanceOf[Tuple].getEntryAt(3).asInstanceOf[AtomicEntry[SpacePosting]].getValue;
    spacePosting.content = newContent;
    capi.write(ct, 0, tx, entry);
    capi.commitTransaction(tx);
  }

  def subscribe(topic: XVSMTopic) {
      if (topic.listener != null) {
        topic.listener.unsubscribe();
      }
      topic.listener = new TopicListener(this, topic);

  }

  def unsubscribe(topic: XVSMTopic) {
    if (topic.listener != null) {
      topic.listener.unsubscribe();
    }
    else log info "Unsubscribe to not subscribed topic"
  }

  def handleNotificationScala(op:Operation, entries:Array[Entry]):Unit = {
     entries.foreach(entry => {
       log debug ("handleNotification called");
       entry match {
         case tuple:Tuple => {
           log debug ("Tuple:" + tuple)
           tuple.size match {
             case 2 => {
               log debug ("Topic Notification")
               val url = tuple.getEntryAt(0).asInstanceOf[AtomicEntry[String]].getValue;
               val peer = session.cachedPeers.get(url).get;
               val topicName = tuple.getEntryAt(1).asInstanceOf[AtomicEntry[String]].getValue;
               val topic = peer.addInternalTopic(topicName);
               notify(topicCreatedMethod (topic) _)
             }
           }
         }

         case a:AtomicEntry[SpacePeer] => {
           val peer = convertPeer(a.getValue());
           log debug ("SpacePeer:" + peer.url); log debug ("Operation:" + op);

           val method = op match {
             case Operation.Write => joinMethod (peer) _;
             case Operation.Shift => joinMethod (peer) _;
             case Operation.Take => leaveMethod (peer) _;
             case Operation.Destroy => leaveMethod (peer) _;
           }
           notify(method);
         }
         case _ =>  log debug ("Unknown entry:" + entry.getClass)
       }
     });
  }

  def convertPeer(sp:SpacePeer):XVSMPeer = {
     new XVSMPeer(this, session, sp.url, sp.name);
  }

  def joinMethod(peer:XVSMPeer)(l:Listener) :Unit = l.peerJoins(peer);
  def leaveMethod(peer:XVSMPeer)(l:Listener):Unit = l.peerLeaves(peer);

  def topicCreatedMethod(topic:XVSMTopic)(l:Listener):Unit = l.topicCreated(topic);

  def notify(call:((Listener)=>Unit))  {
    session.listener.foreach(call(_));
  }
}
