package marmik.sbc.task2.peer.swt

import marmik.sbc.task2.peer.{Session, SessionFactory}
import scala.reflect.BeanProperty

class LoginAction {
  def execute(factories: List[SessionFactory]): Session = {
    val dialog = new LoginDialog(null)

    dialog.setBlockOnOpen(true)
    dialog.open
    null
  }
}
