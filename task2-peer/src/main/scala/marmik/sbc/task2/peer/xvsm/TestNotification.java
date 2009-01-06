package marmik.sbc.task2.peer.xvsm;


import java.net.URI;

import org.xvsm.coordinators.FifoCoordinator;
import org.xvsm.coordinators.KeyCoordinator;
import org.xvsm.core.Capi;
import org.xvsm.core.ContainerRef;
import org.xvsm.core.Entry;
import org.xvsm.core.notifications.Operation;
import org.xvsm.interfaces.ICapi;
import org.xvsm.interfaces.ICoordinator;
import org.xvsm.interfaces.NotificationListener;
import org.xvsm.transactions.Transaction;

public class TestNotification implements NotificationListener {

	public static void main(String[] args) throws Exception {
		TestNotification tn = new TestNotification();
		Capi capi = new Capi();
		URI uri = new java.net.URI("tcpjava://localhost:9876");
        Transaction tx = capi.createTransaction(uri, ICapi.INFINITE_TIMEOUT);
		ContainerRef ct =  capi.createContainer(tx, uri, "CONTAINER_PEER", -1, new KeyCoordinator(new KeyCoordinator.KeyType("Url", String.class)), new FifoCoordinator());
		capi.commitTransaction(tx);

		capi.createNotification(ct, tn, Operation.Write, Operation.Shift);

	}

	public void handleNotification(Operation operation, Entry... entries) {
		System.out.println("handleNotification called");
	}

}
