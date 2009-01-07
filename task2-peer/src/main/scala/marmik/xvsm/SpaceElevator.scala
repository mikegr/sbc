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
import org.xvsm.remote.TransportHandler

import java.net.URI

import marmik.sbc.task2.peer.xvsm.XVSMContants._

import marmik.xvsm.Conversions._

object SpaceElevator {
  private[xvsm] var instanceCount = 0
}

// We only support TcpJava
sealed class SpaceElevator(private val initialPort: Int) {
  def this() = { this(0) }
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  if(SpaceElevator.instanceCount!=0)
    throw new UnsupportedOperationException("These unbelievable stupid XVSM devs make it impossible to have more than one Space per process. Sorry.")

  SpaceElevator.instanceCount += 1

  ConfigurationManager.init(null) // STUPID XVSM MUST CREATE spaces.prop...
  val cm = ConfigurationManager.getInstance()
  cm.setStringSetting("TcpJava.uri", "tcpjava://localhost:" + initialPort) // set the port. FUCK XVSM DEVS!
  log info "Created XVSM configuration with port " + initialPort

  private[xvsm] val capi = new Capi() // BLACK MAGIC
  private val transportHandler = TransportHandler.getInstance
  log info "Created Capi, available at " + url


  def url = transportHandler.getListener("TcpJava").getUri
  def port = url.getPort
  def localSpace() = new Space(this, url)

  def remoteSpace(url: URI) = {
    new Space(this, url)
  }

  def shutdown() = {
    // copied from org.xvsm.internal.tasks.ShutdownTask
    org.xvsm.internal.EventProcessingPool.getInstance.shutdown
    org.xvsm.internal.TimeoutSchedulerPool.shutdown
    org.xvsm.internal.ReplySenderPool.shutdown
    TransportHandler.getInstance().shutdown();
  }
}
