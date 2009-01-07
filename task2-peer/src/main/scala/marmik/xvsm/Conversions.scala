package marmik.xvsm

object Conversions {
  implicit def toXVSMTransaction(tx: Transaction): org.xvsm.transactions.Transaction = {
    tx match {
      case null => null
      case t: Transaction => tx.backing
    }
  }
  implicit def toXVSMSite(space: Space): java.net.URI ={
    space match {
      case null => null
      case s: Space => s.url
    }
  }
}
