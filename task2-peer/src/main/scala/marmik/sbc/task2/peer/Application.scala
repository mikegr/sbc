package marmik.sbc.task2.peer

import marmik.sbc.task2.peer.swt.{JFaceHelpers, LoginAction, PeerWindow}
import marmik.sbc.task2.peer.mock.MockSessionFactory
import marmik.sbc.task2.peer.xvsm.XVSMSessionFactory
import marmik.sbc.task2.peer.rmi.RmiSessionFactory

import org.eclipse.swt.widgets.Display

import java.lang.Runnable

import marmik.sbc.task2.peer.swt.model.SessionAdapter.toSwtSession

object Application {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  def main(args: Array[String]) {
    log.info("Starting Task2 Peer")
    val display = new Display();

    JFaceHelpers.withRealm( () => {
      val factories = List(new MockSessionFactory(), new XVSMSessionFactory(), new RmiSessionFactory())
      val loginAction = new LoginAction()
      loginAction.execute(factories) match {
        case Some(session) =>
          log.info("Connected")
          val mainWindow = new PeerWindow(session)
          mainWindow.setBlockOnOpen(true)
          mainWindow.open()
        case None => log.info("User aborted")
      }
      log.info("Terminating Task2 Peer")
    })
  }
}
