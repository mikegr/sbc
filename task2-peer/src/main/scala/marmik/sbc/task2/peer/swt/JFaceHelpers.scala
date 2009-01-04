package marmik.sbc.task2.peer.swt

import org.eclipse.core.databinding.observable.Realm
import org.eclipse.jface.databinding.swt.SWTObservables
import org.eclipse.swt.widgets.Display

import java.lang.Runnable

object JFaceHelpers {
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
    Realm.runWithDefault(SWTObservables.getRealm(Display.getCurrent()), func)
  }
}
