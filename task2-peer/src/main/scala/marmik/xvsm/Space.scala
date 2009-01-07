package marmik.xvsm

import java.net.URI

import org.xvsm.interfaces.ICapi
import org.xvsm.interfaces.NotificationListener
import org.xvsm.core.ContainerRef
import org.xvsm.core.Entry
import org.xvsm.core.notifications.Operation

import marmik.xvsm.Conversions._

class Space(val elevator: SpaceElevator, val url: URI) {
  def transaction() = {
    new Transaction(elevator, this, elevator.capi.createTransaction(this, ICapi.INFINITE_TIMEOUT))
  }

  def implicitTransaction() = {
    new Transaction(elevator, this, null)
  }

  def registerNotification(container: String, operations: Seq[Operation])(func: Seq[Any] => Any) = {
    val listener = new NotificationListenerAdapter() {
      def handleNotificationScala(operation: Operation, entries: Array[Entry]) {
        func(entries)
      }
    }
    val cref = elevator.capi.lookupContainer(null, url, container);
    elevator.capi.createNotification(cref, listener, operations.toArray: _*)
  }

  override def equals(that: Any) = {
    that match {
      case s: Space => s.elevator == elevator && s.url == url
      case _ => false
    }
  }
  override def hashCode() = { url.hashCode }
}
