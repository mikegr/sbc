package marmik.sbc.task2.peer

trait SessionFactory {

  /** 
   * @param url: Url of super pper
   * @param selfName: Name of this peer
   * @param url: Url of this peer 
   */
  def login(url: String, selfName: String, selfUrl: String);

}
