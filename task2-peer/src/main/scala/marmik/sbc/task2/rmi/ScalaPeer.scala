package marmik.sbc.task2.rmi

import java.util.List
import java.lang.Integer

trait ScalaPeer extends java.rmi.Remote {

  @throws (classOf[java.rmi.RemoteException])
  def getPosting(name:String):List[Posting];

  @throws (classOf[java.rmi.RemoteException])
  def getPost(id:Integer):Posting;

  @throws (classOf[java.rmi.RemoteException])
  def getReplys(id:Integer):List[Posting];

  @throws (classOf[java.rmi.RemoteException])
  def post(topic:String, id:Integer, author:String, subject:String, content:String );

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
}

