package marmik.sbc.task2.peer

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell

import marmik.sbc.task2.peer.swt.LoginDialog

object Application {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName);

  def main(args: Array[String]): Unit = {
    log.info("Starting Task2 Peer")
    
    val display = Display.getDefault
    val shell = new Shell(display)
    val loginDialog = new LoginDialog(shell)

    loginDialog.setBlockOnOpen(true)
    loginDialog.open

    log.info("Terminating Task2 Peer")

  }
}
