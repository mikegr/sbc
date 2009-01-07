package marmik.sbc.task2.peer.xvsm2

import org.xvsm.coordinators._
import org.xvsm.selectors._

object Coordinators {
  def peers = { Array(new KeyCoordinator(new KeyCoordinator.KeyType("Name", classOf[String])), new FifoCoordinator()) }
}
