package marmik.sbc.task2.peer

import marmik.sbc.task2.peer.swt.LoginAction
import marmik.sbc.task2.peer.swt.JFaceHelpers
import marmik.sbc.task2.peer.mock.MockSessionFactory
import marmik.sbc.task2.peer.xvsm.XVSMSessionFactory

import org.eclipse.swt.widgets.Display

import java.lang.Runnable

object Application {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  def main(args: Array[String]): Unit = {
    log.info("Starting Task2 Peer")

    val factories = List(new MockSessionFactory(), new XVSMSessionFactory())

    val display = new Display();
    JFaceHelpers.withRealm( () => {
      val loginAction = new LoginAction()
      loginAction.execute(factories)
    }: Unit)

    log.info("Terminating Task2 Peer")
  }
}
