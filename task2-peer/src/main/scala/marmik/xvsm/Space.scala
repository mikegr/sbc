package marmik.xvsm

import java.net.URI

import org.xvsm.interfaces.ICapi

import marmik.xvsm.Conversions._

class Space(val elevator: SpaceElevator, val url: URI) {
  def transaction() = {
    new Transaction(elevator, this, elevator.capi.createTransaction(this, ICapi.INFINITE_TIMEOUT))
  }

  def implicitTransaction() = {
    new Transaction(elevator, this, null)
  }

  override def equals(that: Any) = {
    that match {
      case s: Space => s.elevator == elevator && s.url == url
      case _ => false
    }
  }
  override def hashCode() = { url.hashCode }
}
