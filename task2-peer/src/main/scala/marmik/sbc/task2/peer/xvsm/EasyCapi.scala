package marmik.sbc.task2.peer.xvsm

import java.util.GregorianCalendar
import org.xvsm.interfaces._
import org.xvsm.core._
import org.xvsm.core.Tuple
import org.xvsm.transactions._
import org.xvsm.internal.exceptions._
import org.xvsm.coordinators._
import org.xvsm.selectors._
import marmik.sbc.task2.peer.xvsm.XVSMContants._

import scala.Null;

import java.net.URI

class EasyCapi(capi:ICapi, superPeer:URI, selfUrl:String, selfName:String) {

  val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName);

    /** Writes entry within a transaction*/
  def writePeerInfo() {
    log info "writing to " + superPeer;
    val entry = new AtomicEntry[SpacePeer](new SpacePeer(selfUrl, selfName));
    val coordinators = Array(new KeyCoordinator(new KeyCoordinator.KeyType("Url", classOf[String]) ), new FifoCoordinator());
    entry.addSelectors(new KeySelector[String]("Url", selfUrl));
    val tx = transaction(superPeer);
    val ct = container(tx, superPeer, CONTAINER_PEERS, coordinators);
    capi.shift(ct,tx, entry);
    //TODO: clear topics;
    capi.commitTransaction(tx);
  }


  def readPeerInfo():List[XVSMPeer] = {
    val tx = transaction(superPeer);
    val ct = container(tx, superPeer, CONTAINER_PEERS, Array(new FifoCoordinator()));
    val entries = capi.read(ct, 0, tx, new FifoSelector(Selector.CNT_ALL));
    capi.commitTransaction(tx);
    entries.map(x => {
      val value = x.asInstanceOf[AtomicEntry[SpacePeer]].getValue();
      new XVSMPeer(this,
                   value.url,
                   value.name)
    }).toList
  }

  def postings(topic:XVSMTopic):List[Posting] = {
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

  def entry2Posting(topic:XVSMTopic, parent:XVSMPosting, x:Entry):XVSMPosting = {
    val tuple = x.asInstanceOf[Tuple]
    val idx = tuple.getEntryAt(0).asInstanceOf[AtomicEntry[Long]].getValue;
    val entry = tuple.getEntryAt(3).asInstanceOf[AtomicEntry[SpacePosting]].getValue;
    new XVSMPosting(this, topic, parent, idx, entry.author, entry.subject, entry.content, entry.date);
  }


  def postingContainer(tx:Transaction, uri:URI):ContainerRef =  {
    container(tx, uri, CONTAINER_POSTINGS, Array(new LindaCoordinator(), new LifoCoordinator()));
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

  def constructAtomicEntry[A](clazz: Class[A], value: A): AtomicEntry[A] = {
    if(value == null)
      new AtomicEntry[A](clazz)
    else
      new AtomicEntry[A](value)
  }


  def constructReadingPostingTuple(id:java.lang.Long, topic:java.lang.String, parent:java.lang.Long, post:SpacePosting):Tuple = {
    new Tuple(if (id == null) null else new AtomicEntry[java.lang.Long](id), //ID
              if (topic == null) null else new AtomicEntry[java.lang.String](topic),  //topic
              if (parent == null) null else new AtomicEntry[java.lang.Long](parent), //Parent
              if (post == null) null else new AtomicEntry[SpacePosting](post));
  }

  def logout() {
    capi.shutdown(null, false)
  }

  /** Fetch list of topics from super peer
   * @param url URL for XVSMTopic. Should be null for local topic.
   */
  def topics(url:String):List[Topic] = {
    val tx = transaction(superPeer);
    val ct = container(tx, superPeer, CONTAINER_TOPICS, Array(new LindaCoordinator()));
    val template = new Tuple(new AtomicEntry[java.lang.String](url), null);

    val entries = capi.read(ct, 0, tx, new LindaSelector(Selector.CNT_ALL, template));
    capi.commitTransaction(tx);
    entries.map{ x =>
      val tuple = x.asInstanceOf[Tuple];
      val name = tuple.getEntryAt(1).asInstanceOf[AtomicEntry[String]].getValue;
      new XVSMTopic(this, url, name)
    }.toList
  }

  def dumpTopics(){
    val tx = transaction(superPeer);
    val ct = container(tx, superPeer, CONTAINER_TOPICS, Array(new LindaCoordinator()));
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

  /** @param url URL for new Topic. Should be null for local Topic.
   */

  def newTopic(url:String, name:String):Topic = {
    val tx = transaction(superPeer);
    val ct = container(tx, superPeer, CONTAINER_TOPICS, Array(new LindaCoordinator()));
    val st = new org.xvsm.core.Tuple(new AtomicEntry[String](selfUrl), new AtomicEntry[String](name));

    capi.write(ct, 0, tx, st);

    capi.commitTransaction(tx);

    new XVSMTopic(this, url, name);

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
        capi.createContainer(tx, uri, container, -1, coodinators: _*);
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
    val uri = getUri(posting.topic.url);
    val tx = transaction(uri);
    val ct = postingContainer(tx, uri);
    val template = constructReadingPostingTuple(posting.id, null, null, null);
    val entry = capi.take(ct, 0, tx, new LindaSelector(Selector.CNT_ALL, template)).first;
    val spacePosting = entry.asInstanceOf[Tuple].getEntryAt(3).asInstanceOf[AtomicEntry[SpacePosting]].getValue;
    spacePosting.content = newContent;
    capi.write(ct, 0, tx, entry);
    capi.commitTransaction(tx);
  }

}
