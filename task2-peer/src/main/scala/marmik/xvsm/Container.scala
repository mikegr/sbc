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
  def dynamicToXVSMEntry(entry: Any, selector: Selector): Entry = {
    selector match {
      case null => toXVSMEntry(entry)
      case s: Selector => toXVSMEntryWithSelector(entry, selector)
    }
  }
}

class Container(val elevator: SpaceElevator, val tx: Transaction, val backing: org.xvsm.core.ContainerRef) {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  def writeRaw(timeout: Int, entry: org.xvsm.core.Entry) {
    log debug "Write to" + backing.getSite + " " + backing.getId + " --> " + entry.toString
    log.debug((tx: org.xvsm.transactions.Transaction).toString)
    elevator.capi.write(backing, timeout, tx, entry)
  }

  def readRaw(timeout: Int, selector: org.xvsm.selectors.Selector): Seq[org.xvsm.core.Entry] = {
    elevator.capi.read(backing, timeout, tx, selector).toSeq
  }

  def takeRaw(timeout: Int, selector: org.xvsm.selectors.Selector): Seq[org.xvsm.core.Entry] = {
    elevator.capi.take(backing, timeout, tx, selector).toSeq
  }

  def write(timeout: Int, entry: Any, selector: org.xvsm.selectors.Selector): Unit = {
    writeRaw(timeout, Container.dynamicToXVSMEntry(entry, selector))
  }

  def write(timeout: Int, entry: Any): Unit = {
    writeRaw(timeout, Container.dynamicToXVSMEntry(entry, null))
  }


  def read[T](timeout: Int, selector: org.xvsm.selectors.Selector): Seq[T] = {
    readRaw(timeout, selector).map(Container.fromXVSMEntry[T](_))
  }

  def readOne[T](timeout: Int, selector: org.xvsm.selectors.Selector): Option[T] = {
    try {
      read[T](timeout, selector).firstOption
    } catch {
      case e: org.xvsm.internal.exceptions.CountNotMetException => None
    }
  }

  def take[T](timeout: Int, selector: org.xvsm.selectors.Selector): Seq[T] = {
    takeRaw(timeout, selector).map(Container.fromXVSMEntry[T](_))
  }

   def takeOne[T](timeout: Int, selector: org.xvsm.selectors.Selector): Option[T] = {
    try {
      take[T](timeout, selector).firstOption
    } catch {
      case e: org.xvsm.internal.exceptions.CountNotMetException => None
    }
  }
}
