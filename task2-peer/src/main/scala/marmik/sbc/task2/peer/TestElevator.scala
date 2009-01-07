package marmik.sbc.task2.peer

import marmik.sbc.task2.peer.swt.{JFaceHelpers, LoginAction, PeerWindow}
import marmik.sbc.task2.peer.mock.MockSessionFactory
import marmik.sbc.task2.peer.xvsm.XVSMSessionFactory
import marmik.sbc.task2.peer.xvsm2.{XVSMSessionFactory => XVSM2SessionFactory}
import marmik.sbc.task2.peer.rmi.RmiSessionFactory

import org.eclipse.core.databinding.observable.Realm
import org.eclipse.swt.widgets.Display
import org.eclipse.jface.databinding.swt.SWTObservables

import java.lang.Runnable

import marmik.sbc.task2.peer.swt.model.SessionAdapter.toSwtSession
import marmik.sbc.task2.peer.swt.JFaceHelpers.asRunnable

import org.xvsm.coordinators._
import org.xvsm.selectors._

object TestElevator {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  def main(args: Array[String]) {
    println(marmik.xvsm.Container.toXVSMEntry(List(1)).toString)
    println(marmik.xvsm.Container.toXVSMEntry(List(1, 2)).toString)
    println(marmik.xvsm.Container.toXVSMEntry(List(1, 2, 3)).toString)
    val elevator = new marmik.xvsm.SpaceElevator()
    log info elevator.url.toString
    val c = elevator.container(null, null, "test", new FifoCoordinator(), new LifoCoordinator())
    c.write(null, 0, "Hallo")
    c.take[String](null, 0, new FifoSelector(1)).firstOption match {
      case Some(x) => println(x)
      case None => println("Nix")
    }
    c.takeOne[String](null, 0, new FifoSelector(1)) match {
      case Some(x) => println(x)
      case None => println("Nix")
    }
    c.write(null, 0, ("Martin", "bin ich"))
    c.takeOne[(String, String)](null, 0, new RandomSelector(1)) match {
      case Some((x, y)) => println("ES GEHT" + x + " " + y)
    }

    val c2 = elevator.container(null, null, "keys", new RandomCoordinator(), new KeyCoordinator(new KeyCoordinator.KeyType("Name", classOf[String])))
    c2.write(null, 0, ("Aha?", new KeySelector("Name", "u")))
    c2.write(null, 0, ("Böse!", new KeySelector("Name", "z")))
    c2.readOne[String](null, 0, new KeySelector("Name", "u")) match {
      case Some(x) => println(x)
    }

    val remoteSpace = elevator.remoteSpace(new java.net.URI("tcpjava://localhost:56473"))
    val tx = elevator.createTransaction(remoteSpace)
    val remoteC = elevator.container(tx, remoteSpace, "TestElevatorFifo", new FifoCoordinator())
    val newI = remoteC.takeOne[Int](tx, 0, new FifoSelector()) match {
      case Some(x: Int) => x + 1
      case None => 1
    }
    println("Write " + newI)
    remoteC.write(tx, 0, newI)
    tx.commit
  }
}