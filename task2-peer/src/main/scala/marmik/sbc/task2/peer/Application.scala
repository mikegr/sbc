package marmik.sbc.task2.peer

import marmik.sbc.task2.peer.swt.LoginAction
import marmik.sbc.task2.peer.mock.MockSessionFactory
import marmik.sbc.task2.peer.xvsm.XVSMSessionFactory

import org.eclipse.core.databinding.observable.Realm
import org.eclipse.jface.databinding.swt.SWTObservables
import org.eclipse.swt.widgets.Display

import java.lang.Runnable

object Application {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  // define conversion from any parameterless function
  // to java.lang.Runnable
  implicit def asRunnable(func: ()=>unit): Runnable = {
    new Runnable() {
      def run() {
        func()
      }
    }
  }

  def main(args: Array[String]): Unit = {
    log.info("Starting Task2 Peer")

    val factories = List(new MockSessionFactory(), new XVSMSessionFactory())

    val display = new Display();
    Realm.runWithDefault(SWTObservables.getRealm(display), () => {
      val loginAction = new LoginAction()
      loginAction.execute(factories)
    }: Unit)

    log.info("Terminating Task2 Peer")
  }
}
