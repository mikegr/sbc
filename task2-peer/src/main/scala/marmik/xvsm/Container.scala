package marmik.xvsm

import java.io.Serializable
import scala.collection.mutable.ListBuffer
import scalaz.javas.{List => ListZ}
import marmik.xvsm.Conversions._

import org.xvsm.selectors.Selector
import org.xvsm.core.Entry

object Container {
  def toXVSMEntry(entry: Any): org.xvsm.core.Entry = {
    entry match {
      case None => new org.xvsm.core.VoidEntry()
      case Some(x: Serializable) => toXVSMEntry(x)
      case xs@Seq(_*) => toXVSMEntry(ListZ.ScalaList_JavaList(xs.toList))
      case (a: Serializable, b: Serializable) => new org.xvsm.core.Tuple(toXVSMEntry(a), toXVSMEntry(b))
      case (a: Serializable, b: Serializable, c: Serializable) => new org.xvsm.core.Tuple(toXVSMEntry(a), toXVSMEntry(b), toXVSMEntry(c))
      case (a: Serializable, b: Serializable, c: Serializable, d: Serializable) => new org.xvsm.core.Tuple(toXVSMEntry(a), toXVSMEntry(b), toXVSMEntry(c), toXVSMEntry(d))
      case x: Serializable => new org.xvsm.core.AtomicEntry[Serializable](x, x.getClass.asInstanceOf[Class[Serializable]])
    }
  }
  def fromXVSMEntry[T](entry: org.xvsm.core.Entry): T = {
    entry match {
      case e: org.xvsm.core.VoidEntry => None.asInstanceOf[T]
      case e: org.xvsm.core.AtomicEntry[T] => e.getValue
      case e: org.xvsm.core.Tuple if e.size == 2 => (fromXVSMEntry[Serializable](e.getEntryAt(0)), fromXVSMEntry[Serializable](e.getEntryAt(1))).asInstanceOf[T]
      case e: org.xvsm.core.Tuple if e.size == 3 => (fromXVSMEntry[Serializable](e.getEntryAt(0)), fromXVSMEntry[Serializable](e.getEntryAt(1)), fromXVSMEntry[Serializable](e.getEntryAt(2))).asInstanceOf[T]
      case e: org.xvsm.core.Tuple if e.size == 4 => (fromXVSMEntry[Serializable](e.getEntryAt(0)), fromXVSMEntry[Serializable](e.getEntryAt(1)), fromXVSMEntry[Serializable](e.getEntryAt(2)), fromXVSMEntry[Serializable](e.getEntryAt(3))).asInstanceOf[T]
    }
  }
  def toXVSMEntryWithSelector(rawEntry: Any, selector: Selector): org.xvsm.core.Entry = {
    val entry = toXVSMEntry(rawEntry)
    entry.setSelectors(ListZ.ScalaList_JavaList(List(selector)))
    entry
  }
  def dynamicToXVSMEntry(entry: Any): Entry = {
    entry match {
      case (value: Any, selector: org.xvsm.selectors.Selector) => toXVSMEntryWithSelector(value, selector)
      case value: Any => toXVSMEntry(value)
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

  /**
   * entries:
   *   Either one or more instances of Serializable
   *   Or one or more (Serializable, Selector)
   */
  def write(tx: Transaction, timeout: Int, entries: Any*) {
    writeRaw(tx, timeout, entries.map(Container.dynamicToXVSMEntry(_)): _*)
  }

  def read[T](tx: Transaction, timeout: Int, selectors: org.xvsm.selectors.Selector*): Seq[T] = {
    readRaw(tx, timeout, selectors: _*).map(Container.fromXVSMEntry[T](_))
  }

  def readOne[T](tx: Transaction, timeout: Int, selectors: org.xvsm.selectors.Selector*): Option[T] = {
    try {
      read[T](tx, timeout, selectors: _*).firstOption
    } catch {
      case e: org.xvsm.internal.exceptions.CountNotMetException => None
    }
  }

  def take[T](tx: Transaction, timeout: Int, selectors: org.xvsm.selectors.Selector*): Seq[T] = {
    takeRaw(tx, timeout, selectors: _*).map(Container.fromXVSMEntry[T](_))
  }

   def takeOne[T](tx: Transaction, timeout: Int, selectors: org.xvsm.selectors.Selector*): Option[T] = {
    try {
      take[T](tx, timeout, selectors: _*).firstOption
    } catch {
      case e: org.xvsm.internal.exceptions.CountNotMetException => None
    }
  }
}
