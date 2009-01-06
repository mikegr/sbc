package marmik.sbc.task2.peer.swt

import org.eclipse.core.databinding.observable.Realm
import org.eclipse.jface.databinding.swt.SWTObservables
import org.eclipse.swt.widgets.Display

import java.lang.Runnable

object JFaceHelpers {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass);

  // define conversion from any parameterless function
  // to java.lang.Runnable
  implicit def asRunnable(func: () => Unit): Runnable = {
    new Runnable() {
      def run() {
        func()
      }
    }
  }

  def withRealm(func: () => Unit): Unit = {
    val b1 = Display.getDefault
    val b2 = SWTObservables.getRealm(Display.getDefault)
    //log.info("" + b1 + " " + b2)
    //Realm.runWithDefault(SWTObservables.getRealm(Display.getDefault()), func)
    SWTObservables.getRealm(Display.getDefault).asyncExec(func)
  }
}
