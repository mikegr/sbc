package marmik.sbc.task2.peer.xvsm;

import java.net.URI;
import java.util.Random;

import org.xvsm.coordinators.FifoCoordinator;
import org.xvsm.core.AtomicEntry;
import org.xvsm.core.Capi;
import org.xvsm.core.ContainerRef;
import org.xvsm.core.Entry;
import org.xvsm.core.notifications.Operation;
import org.xvsm.interfaces.ICapi;
import org.xvsm.interfaces.NotificationListener;
import org.xvsm.interfaces.container.IContainer;
import org.xvsm.internal.exceptions.InvalidContainerException;
import org.xvsm.selectors.FifoSelector;
import org.xvsm.selectors.Selector;
import org.xvsm.transactions.Transaction;

public class TestNotification implements NotificationListener {

	public static void main(String[] args) throws Exception {
		TestNotification tn = new TestNotification();
		Capi capi = new Capi();
		URI uri = new java.net.URI("tcpjava://localhost:56473");

        Transaction tx = capi.createTransaction(uri, ICapi.INFINITE_TIMEOUT);

        String selfUrl = "tcp://whatever" + new Random().nextInt();

        AtomicEntry<String> entry = new AtomicEntry<String>(selfUrl);


        ContainerRef ct = peerContainer(capi, tx, uri);
        capi.write(ct, 0, tx, entry);
        capi.commitTransaction(tx);

        capi.createNotification(ct, tn, Operation.Write, Operation.Shift);

        tx = capi.createTransaction(uri, ICapi.INFINITE_TIMEOUT);

        ContainerRef ct2 = peerContainer(capi, tx, uri);
        Entry[] entries = capi.read(ct2, 0, tx, new FifoSelector(Selector.CNT_ALL));
        capi.commitTransaction(tx);

        for (Entry e:entries) {
        	System.out.println(((AtomicEntry<String>)e).getValue());
        }

        tx = capi.createTransaction(uri, ICapi.INFINITE_TIMEOUT);
        ContainerRef ct3 = secondContainer(capi, tx, uri);
        AtomicEntry<URI> e3 = new AtomicEntry<URI>(new URI("http://irgendwo"));
        capi.write(ct3, 0, tx, e3);
        capi.commitTransaction(tx);


        System.out.println("Waiting for keystroke");
        System.in.read();

	}

	public void handleNotification(Operation operation, Entry... entries) {
		System.out.println("handleNotification called");
	}

    public static ContainerRef peerContainer(Capi capi, Transaction tx, URI uri) throws Exception {
    	return container(capi, tx, uri, "CONTAINER_PEER");
    }

    public static ContainerRef secondContainer(Capi capi, Transaction tx, URI uri) throws Exception {
    	return container(capi, tx, uri, "CONTAINER_SECOND");
    }

    public static ContainerRef container(Capi capi, Transaction tx, URI uri, String container) throws Exception {
      try {
        return capi.lookupContainer(tx, uri, container);
      }
      catch (InvalidContainerException e) {
          return capi.createContainer(tx, uri, container, IContainer.INFINITE_SIZE, new FifoCoordinator());
      }
    }


}
