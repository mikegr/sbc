package marmik.sbc.task2.peer.swt

import marmik.sbc.task2.peer.{Session, SessionFactory}
import marmik.sbc.task2.peer.swt.model.{SessionFactory => SwtSessionFactory}
import scala.reflect.BeanProperty

import scalaz.javas.List.ScalaList_JavaList
import marmik.sbc.task2.peer.swt.model.SessionFactoryAdapter.scalaSessionFactories2SwtFactories

class LoginAction {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  def execute(factories: List[SessionFactory]): Option[Session] = {
    val dialog = new LoginDialog(null, scalaSessionFactories2SwtFactories(factories))

    dialog.setBlockOnOpen(true)
    dialog.open
    val model = dialog.getModel

    if(model.getFactory == null || model.getUrl == null || model.getName == null || model.getName == "")
      return None

    val factory: SessionFactory = model.getFactory().getBacking
    val url = model.getUrl()
    val name = model.getName()
    log.info("Creating " + factory.name + " session named '" + name + "' (" + url + ")")
    val session = factory.login(url, name, null);
    session.localPeer.newTopic("Common Topic");
    Some(session);

  }

}
