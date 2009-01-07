package marmik.xvsm

import org.xvsm.core._
import org.xvsm.core.notifications._
import org.xvsm.core.aspect._
import org.xvsm.transactions._
import org.xvsm.internal.exceptions._
import org.xvsm.coordinators._
import org.xvsm.selectors._
import org.xvsm.interfaces._
import org.xvsm.interfaces.container._

import org.xvsm.configuration.ConfigurationManager

import marmik.sbc.task2.peer.xvsm.XVSMContants._

object Space {
  private[xvsm] var instanceCount = 0
}

// We only support TcpJava
class Space(port: Int) {
  if(Space.instanceCount!=0)
    throw new UnsupportedOperationException("These unbelievable stupid XVSM devs make it impossible to have more than one Space per process. Sorry.")

  ConfigurationManager.init(null) // STUPID XVSM MUST CREATE spaces.prop...
  val cm = ConfigurationManager.getInstance()
  cm.setStringSetting("TcpJava.uri", "tcpjava://localhost:" + port.toString) // set the port. FUCK XVSM DEVS!

  var capi = new Capi() // BLACK MAGIC

  def this() = { this(0) }
}
