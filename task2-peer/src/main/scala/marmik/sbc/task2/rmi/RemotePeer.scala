package marmik.sbc.task2.rmi

import java.util.List
import java.lang.Integer

trait RemotePeer extends java.rmi.Remote {

  @throws (classOf[java.rmi.RemoteException])
  def getPostings(name:String):List[PostingInfo];

  @throws (classOf[java.rmi.RemoteException])
  def getPost(id:Integer):PostingInfo;

  @throws (classOf[java.rmi.RemoteException])
  def getReplys(id:Integer):List[PostingInfo];

  @throws (classOf[java.rmi.RemoteException])
  def post(topic:String, parent:Integer, author:String, subject:String, content:String ):Integer;

  @throws (classOf[java.rmi.RemoteException])
  def edit(id:Integer, content:String);

  @throws (classOf[java.rmi.RemoteException])
  def subscribe(topic:String, url:String);

  @throws (classOf[java.rmi.RemoteException])
  def unsubscribe(topic:String, url:String);

  @throws (classOf[java.rmi.RemoteException])
  def postCreated(url:String, topic:String, id:Integer);

  @throws (classOf[java.rmi.RemoteException])
  def postEdited(url:String, topic:String, id:Integer);

  @throws (classOf[java.rmi.RemoteException])
  def peerLoggedIn(url:String, name:String);

  @throws (classOf[java.rmi.RemoteException])
  def peerLoggedOut(url:String, name:String);

  @throws (classOf[java.rmi.RemoteException])
  def peerHasNewTopic(url:String, name:String);
}

