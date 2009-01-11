package marmik.sbc.task2.peer.xvsm

import org.xvsm.interfaces._
import org.xvsm.core._
import org.xvsm.transactions._
import org.xvsm.coordinators._
import org.xvsm.selectors._
import marmik.sbc.task2.peer.xvsm.XVSMContants._

class XVSMSessionFactory extends SessionFactory {

  val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName);

  def name() = "XVSM"

  def login(superPeerUrl: String, selfName: String):Session =  {


    new XVSMSession(superPeerUrl, selfName).login

  }

}
