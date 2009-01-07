package marmik.xvsm

import org.xvsm.interfaces.ICoordinator
import org.xvsm.interfaces.container.IContainer
import org.xvsm.internal.exceptions.InvalidContainerException

import marmik.xvsm.Conversions._

class Transaction(val elevator: SpaceElevator, val space: Space, val backing: org.xvsm.transactions.Transaction) {
  def commit() = elevator.capi.commitTransaction(backing)
  def rollback() = elevator.capi.rollbackTransaction(backing)

  // lookup or create container
  def container(name: String, coordinators: ICoordinator*) = {
    new Container(elevator, this,
      try {
        elevator.capi.lookupContainer(backing, space, name);
      }
      catch {
        case e: InvalidContainerException =>
          elevator.capi.createContainer(backing, space, name, IContainer.INFINITE_SIZE, coordinators: _*)
      })
  }

  def apply[T](func: Transaction => T): T = {
    try {
      val returnValue = func(this)
      this.commit
      return returnValue
    } catch {
      case e => {
        this.rollback
        throw e
      }
    }
  }
}
