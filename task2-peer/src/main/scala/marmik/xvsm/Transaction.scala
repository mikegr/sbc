package marmik.xvsm

class Transaction(val elevator: SpaceElevator, val space: Space, val backing: org.xvsm.transactions.Transaction) {
  def commit() = elevator.capi.commitTransaction(backing)
  def rollback() = elevator.capi.rollbackTransaction(backing)
}
