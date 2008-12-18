package marmik.sbc.task2.rmi;

import java.util.List;

public interface SuperPeer extends java.rmi.Remote {
	public List<Topic> getTopcis() throws java.rmi.RemoteException;
	public void newTopic(String url, String name) throws java.rmi.RemoteException;
}
