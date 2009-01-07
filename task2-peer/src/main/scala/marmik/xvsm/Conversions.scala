package marmik.xvsm

object Conversions {
  implicit def toXVSMTransaction(tx: Transaction): org.xvsm.transactions.Transaction = {
    tx match {
      case null => null
      case t: Transaction if t.space!=t.elevator.localSpace => tx.backing
      case t: Transaction if t.space==t.elevator.localSpace => null
      case _ => null
    }
  }
  implicit def toXVSMSite(space: Space): java.net.URI ={
    space match {
      case null => null
      case s: Space => s.url
    }
  }
}
