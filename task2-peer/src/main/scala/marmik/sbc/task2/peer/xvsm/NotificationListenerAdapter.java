package marmik.sbc.task2.peer.xvsm;

import org.xvsm.core.Entry;
import org.xvsm.core.notifications.Operation;
import org.xvsm.interfaces.NotificationListener;

public abstract class NotificationListenerAdapter implements NotificationListener {

	public void handleNotification(Operation arg0, Entry... arg1) {
		handleNotificationScala(arg0, arg1);
	}

	public abstract void handleNotificationScala(Operation arg0, Entry[] arg1);

}
