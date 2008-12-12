package marmik.sbc.task2.peer

import marmik.sbc.task2.peer.swt.LoginDialog

object Application {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName);

  def main(args: Array[String]): Unit = {
    log.info("Starting Task2 Peer")

    val loginDialog = new LoginDialog(null)

    loginDialog.setBlockOnOpen(true)
    loginDialog.open

    log.info("Terminating Task2 Peer")

  }
}
