package marmik.sbc.task2.peer.swt

import marmik.sbc.task2.peer.{Session, SessionFactory}
import marmik.sbc.task2.peer.swt.model.{SessionFactory => SwtSessionFactory}
import scala.reflect.BeanProperty

import scalaz.javas.List.ScalaList_JavaList
import marmik.sbc.task2.peer.swt.model.SessionFactoryAdapter.scalaSessionFactories2SwtFactories

class LoginAction {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  def execute(factories: List[SessionFactory], args: Array[String]): Option[Session] = {
    if(args.isEmpty) {
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
      Some(factory.login(url, name, null))
    } else {
      factories.find(_.name == args(0)) match {
        case Some(factory) => {
          Some(factory.login(args(1), args(2), null))
        }
        case None => return None
      }
    }
  }

}
