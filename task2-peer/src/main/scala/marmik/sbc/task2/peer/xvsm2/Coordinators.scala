package marmik.sbc.task2.peer.xvsm2

import org.xvsm.coordinators._
import org.xvsm.selectors._

object Coordinators {
  def peers: Seq[org.xvsm.interfaces.ICoordinator] =
    List(new RandomCoordinator, new KeyCoordinator(new KeyCoordinator.KeyType("name", classOf[String])))
}
