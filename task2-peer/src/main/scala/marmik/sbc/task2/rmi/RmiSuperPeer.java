package marmik.sbc.task2.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiSuperPeer extends UnicastRemoteObject implements SuperPeer {

	/**
	 *
	 */
	private static final long serialVersionUID = -60061789656005512L;

	private ArrayList<Topic> list = new ArrayList<Topic>();

	public RmiSuperPeer() throws RemoteException {
	}

	public List<Topic> getTopcis() throws RemoteException {
		return list;
	}

	public void newTopic(String url, String name) throws RemoteException {
		list.add(new Topic(url, name));
	}
}
