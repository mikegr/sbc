package marmik.xvsm

import scala.collection.mutable.ListBuffer
import scalaz.javas.{List => ListZ}
import marmik.xvsm.Conversions._

object Container {
  def toXVSMEntry(entry: Any): org.xvsm.core.Entry = {
    entry match {
      case None => new org.xvsm.core.VoidEntry()
      case Some(x) => toXVSMEntry(x)
      case xs@Seq(_*) => toXVSMEntry(ListZ.ScalaList_JavaList(xs.toList))
      case (a, b) => new org.xvsm.core.Tuple(toXVSMEntry(a, b))
      case (a, b, c) => new org.xvsm.core.Tuple(toXVSMEntry(a, b, c))
      case (a, b, c, d) => new org.xvsm.core.Tuple(toXVSMEntry(a, b, c, d))
      case x => GenericHelper.createAtomicEntry(x)
    //case x => new org.xvsm.core.AtomicEntry[_](x, x.getClass.asInstanceOf[Class[_]])
    }
  }
  def fromXVSMEntry(entry: org.xvsm.core.Entry) = {
    entry match {
      case e: org.xvsm.core.VoidEntry => None
      case e: org.xvsm.core.AtomicEntry[_] => e.getValue
      case e: org.xvsm.core.Tuple if e.size == 2 => (e.getEntryAt(0), e.getEntryAt(1))
      case e: org.xvsm.core.Tuple if e.size == 3 => (e.getEntryAt(0), e.getEntryAt(1), e.getEntryAt(2))
      case e: org.xvsm.core.Tuple if e.size == 4 => (e.getEntryAt(0), e.getEntryAt(1), e.getEntryAt(2), e.getEntryAt(3))
    }
  }
}

class Container(val elevator: SpaceElevator, val backing: org.xvsm.core.ContainerRef) {
  def writeRaw(tx: Transaction, timeout: Int, entries: org.xvsm.core.Entry*) {
    elevator.capi.write(backing, timeout, tx, entries: _*)
  }

  def readRaw(tx: Transaction, timeout: Int, selectors: org.xvsm.selectors.Selector*): Seq[org.xvsm.core.Entry] = {
    elevator.capi.read(backing, timeout, tx, selectors: _*).toSeq
  }

  def takeRaw(tx: Transaction, timeout: Int, selectors: org.xvsm.selectors.Selector*): Seq[org.xvsm.core.Entry] = {
    elevator.capi.take(backing, timeout, tx, selectors: _*).toSeq
  }

  def write(tx: Transaction, timeout: Int, entries: Any*) {
    writeRaw(tx, timeout, entries.map(Container.toXVSMEntry(_)): _*)
  }

  def read[T](tx: Transaction, timeout: Int, selectors: org.xvsm.selectors.Selector*): Seq[T] = {
    readRaw(tx, timeout, selectors: _*).map(Container.fromXVSMEntry(_)).asInstanceOf[Seq[T]]
  }

  def take[T](tx: Transaction, timeout: Int, selectors: org.xvsm.selectors.Selector*): Seq[T] = {
    takeRaw(tx, timeout, selectors: _*).map(Container.fromXVSMEntry(_)).asInstanceOf[Seq[T]]
  }
}
