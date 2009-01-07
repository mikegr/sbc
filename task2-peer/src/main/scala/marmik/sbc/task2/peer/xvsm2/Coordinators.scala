package marmik.sbc.task2.peer.xvsm2

import org.xvsm.coordinators._
import org.xvsm.selectors._

object Coordinators {
  def peers: Array[org.xvsm.interfaces.ICoordinator] =
    Array(new RandomCoordinator, new KeyCoordinator(new KeyCoordinator.KeyType("name", classOf[String])))
  def topics: Array[org.xvsm.interfaces.ICoordinator] =
    Array(new RandomCoordinator, new KeyCoordinator(new KeyCoordinator.KeyType("name", classOf[String])))
}
