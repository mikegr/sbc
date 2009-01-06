package marmik.sbc.task2.peer

object SessionFactory {
  val all =  {
    val mockFactory = new mock.MockSessionFactory()
    Map(mockFactory.name -> mockFactory)
  }
}

trait SessionFactory {
  def name(): String;

  /**
   * @param url: Url of super pper
   * @param selfName: Name of this peer
   * @param url: Url of this peer
   */
  def login(superPeerUrl: String, selfName: String):Session;

}
