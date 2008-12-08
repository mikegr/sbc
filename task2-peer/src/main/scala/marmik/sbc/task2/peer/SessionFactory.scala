package marmik.sbc.task2.peer

object SessionFactory {
  val all =  {
    val mockFactory = new mock.MockSessionFactory()
    Map(mockFactory.name -> mockFactory)
  }
}

trait SessionFactory {
  def name();

  /** 
   * @param url: Url of super pper
   * @param selfName: Name of this peer
   * @param url: Url of this peer 
   */
  def login(url: String, selfName: String, selfUrl: String):Session;

}
