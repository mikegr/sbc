package marmik.sbc.task2.peer.xvsm2

import scala.collection.mutable.ListBuffer

import marmik.xvsm._
import marmik.sbc.task2.peer._
import org.xvsm.selectors._
import org.xvsm.core.notifications.Operation

class XVSMSession(val elevator: SpaceElevator, val superPeer: Space, val name: String) extends Session {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);
  var localPeer: XVSMPeer = new XVSMPeer(elevator, this, (name, elevator.localSpace.url))
  var listeners: ListBuffer[Listener] = new ListBuffer()
  var peers: ListBuffer[Peer] = new ListBuffer[Peer]()
  peers.appendAll(superPeer.transaction().apply(tx => {
    val peersContainer = tx.container("peers")
    peersContainer.read[(String, java.net.URI)](0, new RandomSelector(Selector.CNT_ALL))
  }).map(new XVSMPeer(elevator, this, _)))

  superPeer.registerNotification("peers", List(Operation.Write))(entry => {
    val peer = new XVSMPeer(elevator, this, entry.asInstanceOf[(String, java.net.URI)])
    peers += peer
    fire(l => l.peerJoins(peer))
  })
  superPeer.registerNotification("peers", List(Operation.Take))(entry => {
    log debug "Peer leaves"
    val info = entry.asInstanceOf[(String, java.net.URI)]
    val peer = new Peer() {
      override def isLocal() = false

      def session() = XVSMSession.this

      def name() = info._1

      def topics() = List()

      def newTopic(name: String) = null
    }
    fire(l => l.peerLeaves(peer))
  })
  log info "Connected"

  def registerListener(l: Listener) {listeners += l}

  def logout() {
    log debug "Logout"
    superPeer.transaction()(tx => {
      val peers = tx.container("peers")
      peers.takeOne[(String, java.net.URI)](0, new KeySelector("name", localPeer.name))
    })
    elevator.shutdown
  }

  def setBadWordList(seq: Seq[String]) {
    throw new UnsupportedOperationException("setBadWordList not implemented")
  }

  def getBadWordList(): Seq[String] = {
    throw new UnsupportedOperationException("getBadWordList not implemented")
  }

  override def hashCode = elevator.hashCode

  override def equals(other: Any) =
    other match {
      case s: XVSMSession => this.name == s.name && this.elevator == s.elevator
      case _ => false
    }

  private[xvsm2] def fire(func: Listener => Any) {
    log.debug("Fireing")
    for (listener <- listeners)
      func(listener)
  }

}
