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

class TopicListener(ec:EasyCapi, topic:XVSMTopic) extends NotificationListenerAdapter {

  val session = topic.peer.session;

  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  val subscriptionURI:java.net.URI = {
      //Create container in order to make notification possible
      val uri = ec.getUri(topic.url);
      val tx = ec.transaction(uri);
      val ct = ec.postingContainer(tx, uri);
      ec.capi.commitTransaction(tx);
      ec.capi.createNotification(ec.postingContainer(null, uri), this, Operation.Write);
  }


  def handleNotificationScala(op:Operation, entries:Array[Entry]):Unit = {
    log debug ("Posting Notification")
    entries.foreach(entry => {
      val tuple = entry.asInstanceOf[Tuple];
      val postId = tuple.getEntryAt(0).asInstanceOf[AtomicEntry[Long]].getValue;
      val topicId = tuple.getEntryAt(1).asInstanceOf[AtomicEntry[String]].getValue;
	  val parentId = tuple.getEntryAt(2).asInstanceOf[AtomicEntry[Long]].getValue;
      log debug ("ParentId is " + parentId);
      val parent:XVSMPosting = topic.cachedPosts.getOrElse(parentId, null);
      val post = topic.cachedPosts.getOrElse(postId, ec.entry2Posting(topic, parent, tuple));
      val method = if (topic.cachedPosts.contains(postId)) postEditedMethod (post) _
           else postCreatedMethod (post) _
      session.listener.foreach(method(_));
    });
  }

  def unsubscribe() {
      val uri = ec.getUri(topic.url);
      val tx = ec.transaction(uri);
      val ct = ec.postingContainer(tx, uri);
      ec.capi.removeAspect(ct, java.util.Arrays.asList(LocalIPoint.PostWrite), subscriptionURI);
      ec.capi.commitTransaction(tx);
  }

  def postCreatedMethod(post:XVSMPosting)(l:Listener):Unit = l.postingCreated(post);
  def postEditedMethod(post:XVSMPosting)(l:Listener):Unit = l.postingEdited(post);

}