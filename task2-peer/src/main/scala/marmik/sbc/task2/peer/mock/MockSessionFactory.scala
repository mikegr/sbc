package marmik.sbc.task2.peer.mock

import marmik.sbc.task2.peer._

class MockSessionFactory extends SessionFactory {
  def name() = "Mock"
  def login(url: String, selfName: String): Session = new MockSession(url, selfName)
}
