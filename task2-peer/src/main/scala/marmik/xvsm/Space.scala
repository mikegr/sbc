package marmik.xvsm

import java.net.URI

class Space(val elevator: SpaceElevator, val url: URI) {
  override def equals(that: Any) = {
    that match {
      case s: Space => s.elevator == elevator && s.url == url
      case _ => false
    }
  }
  override def hashCode() = { url.hashCode }
}
