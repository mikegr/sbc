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

  def login(superPeerUrl: String, selfName: String, selfUrl: String):Session =  {
    val capi = new Capi();
    val uri = new java.net.URI(superPeerUrl);

    val easyCapi = new EasyCapi(capi, uri, selfName);
    
    log.info("selfUrl:" + easyCapi.selfUrl);
    
    easyCapi.registerPeerListener();

    //val tx = capi.createTransaction(uri, ICapi.INFINITE_TIMEOUT);
    //val superpeer = capi.lookupContainer(tx, uri, CONTAINER);

    easyCapi.writePeerInfo();

    new XVSMSession(easyCapi, superPeerUrl, selfName);

  }

}
