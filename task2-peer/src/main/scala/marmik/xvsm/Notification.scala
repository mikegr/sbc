package marmik.xvsm
/**
 * Created by IntelliJ IDEA.
 * User: msch
 * Date: 22.01.2009
 * Time: 23:17:41
 * To change this template use File | Settings | File Templates.
 */
import org.xvsm.core.aspect.{IPoint, LocalIPoint}
import org.xvsm.core.ContainerRef
import marmik.xvsm.Conversions._
import org.xvsm.core.notifications.Operation
import scalaz.javas.List.{JavaList_ScalaList, ScalaList_JavaList}

class Notification(elevator: SpaceElevator, val space: Space, val container: String, val url: java.net.URI, operations: Seq[Operation], val cref: ContainerRef) {
  elevator.notifications = this :: elevator.notifications
  def remove() {
    elevator.capi.removeAspect(cref, operations.map(toLocalIPoint _).toList, url)
  }
  def toLocalIPoint(op: Operation) = op match {
    case Operation.Write =>  LocalIPoint.PostWrite
  }
}
