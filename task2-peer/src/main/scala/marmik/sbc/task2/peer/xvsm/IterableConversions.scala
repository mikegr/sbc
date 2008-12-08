package marmik.sbc.task2.peer.xvsm

/**
 * General Java Iterable converters, allowing for comprehensions.
 *
 * Based on suggestions from Jamie Webb (http://scala.sygneca.com/)
 */
object IterableConversions {

    case class JavaIteratorIterator[A](itr : java.util.Iterator[A] )
extends Iterator[A] {
       def hasNext = itr.hasNext
       def next() = itr.next
    }

    case class JavaIteratableIterable[A](iterable :
java.lang.Iterable[A]) extends Iterable[A] {
      def elements = JavaIteratorIterator(iterable.iterator)
    }

    implicit def implicitJavaIterableToScalaIterable[A]( iterable :
java.lang.Iterable[A]) : Iterable[A] =
      JavaIteratableIterable(iterable)

    case class JavaEnumerationIterator[A](itr :
java.util.Enumeration[A] ) extends Iterator[A] {
       def hasNext = itr.hasMoreElements
       def next() = itr.nextElement
    }

    case class JavaEnumerationIterable[A](iterable :
java.util.Enumeration[A]) extends Iterable[A] {
      def elements = JavaEnumerationIterator(iterable)
    }

    implicit def implicitJavaEnumerationToScalaIterable[A]( iterable :
java.util.Enumeration[A]) : Iterable[A] =
      JavaEnumerationIterable(iterable)

} 