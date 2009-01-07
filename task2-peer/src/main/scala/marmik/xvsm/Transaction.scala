package marmik.xvsm

class Transaction(val elevator: SpaceElevator, val space: Space, val backing: org.xvsm.transactions.Transaction) {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);
  log debug "New transaction " + backing.getSite + " " + backing.getId
  def commit() = elevator.capi.commitTransaction(backing)
  def rollback() = elevator.capi.rollbackTransaction(backing)
}
