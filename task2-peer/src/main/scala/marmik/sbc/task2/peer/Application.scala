package marmik.sbc.task2.peer

object Application {
  val log = org.slf4j.LoggerFactory.getLogger(this.getClass.getName);

  def main(args: Array[String]): Unit = {
    log.info("Starting Peer") 
  }
}
