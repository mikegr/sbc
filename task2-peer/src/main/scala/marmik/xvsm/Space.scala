package marmik.xvsm

import java.net.URI

import org.xvsm.interfaces.ICapi
import org.xvsm.interfaces.NotificationListener
import org.xvsm.core.ContainerRef
import org.xvsm.core.Entry
import org.xvsm.core.notifications.Operation

import marmik.xvsm.Conversions._

class Space(val elevator: SpaceElevator, val url: URI) {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  def transaction() = {
    new Transaction(elevator, this, elevator.capi.createTransaction(this, ICapi.INFINITE_TIMEOUT))
  }

  def implicitTransaction() = {
    new Transaction(elevator, this, null)
  }

  def registerNotification(container: String, operations: Seq[Operation])(func: Any => Any): Notification = {
    Thread.sleep(500)
    val listener = new NotificationListenerAdapter() {
      def handleNotificationScala(operation: Operation, entries: Array[Entry]) {
        log debug "Received notification"
        for (entry <- entries)
          func(Container.fromXVSMEntry(entry))
      }
    }
    val cref = elevator.capi.lookupContainer(null, url, container);
    val notificationUrl = elevator.capi.createNotification(cref, listener, operations.toArray: _*)
    log debug "Created notification"
    return new Notification(elevator, this, container, notificationUrl, operations, cref)
  }

  override def equals(that: Any) = {
    that match {
      case s: Space => s.elevator == elevator && s.url == url
      case _ => false
    }
  }

  override def hashCode() = {url.hashCode}
}
