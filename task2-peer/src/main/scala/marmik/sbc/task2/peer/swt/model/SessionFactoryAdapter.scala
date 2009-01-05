package marmik.sbc.task2.peer.swt.model

import marmik.sbc.task2.peer.{SessionFactory => ScalaSessionFactory}
import marmik.sbc.task2.peer.swt.model.{SessionFactory => SwtSessionFactory}

object SessionFactoryAdapter {
  implicit def toSwtSessionFactory(factory: ScalaSessionFactory): SwtSessionFactory = new SwtSessionFactory(factory)

  implicit def scalaSessionFactories2SwtFactories(list: List[ScalaSessionFactory]): List[SwtSessionFactory] =
    list.map(new SwtSessionFactory(_))
}
