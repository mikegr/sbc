package marmik.sbc.task2.peer.xvsm

import java.util.regex._

import org.xvsm.core._
import org.xvsm.core.aspect._
import org.xvsm.interfaces._
import org.xvsm.transactions._

import scala.collection.jcl.Conversions._
import scala.collection.jcl._

class BadWordFilterAspect(regex:Seq[String]) extends LocalAspect {

  val pattern = Pattern.compile(regex.mkString("","|",""));

  override
  def preWrite(cref:ContainerRef, tx:Transaction, entries:java.util.List[Entry], retrycount:Int, contextProperties:java.util.Properties ) = replace (cref, tx, entries, contextProperties);

  override
  def preShift(cref:ContainerRef, tx:Transaction, entries:java.util.List[Entry], contextProperties:java.util.Properties) = replace (cref, tx, entries, contextProperties);


  def replace(cref:ContainerRef, tx:Transaction, entries:java.util.List[Entry], contextProperties:java.util.Properties ) {
    entries.foreach(entry=> {
      if (entry.isInstanceOf[Tuple]) {
        val tuple = entry.asInstanceOf[Tuple]
        if (tuple.size == 4) {
          val spacePosting = tuple.getEntryAt(3).asInstanceOf[AtomicEntry[SpacePosting]].getValue;
          val oldContent = spacePosting.content;
          System.out.println("BadWordFilterAspect:  OldContent: " + oldContent);
          val matcher = pattern.matcher(oldContent);
          val newContent = matcher.replaceAll("***");
          System.out.println("BadWordFilterAspect:  NewContent: " + newContent);
          spacePosting.content = newContent;
        }
        else
          System.out.println("BadWordFilterAspect:  Tuple has not 5 entries");
      }
      else
            System.out.println("BadWordFilterAspect: got non tuple")

    });
  }

  def points():java.util.List[LocalIPoint] = {
    val list = new java.util.ArrayList[LocalIPoint]();
    list += LocalIPoint.PreWrite;
    list += LocalIPoint.PreShift;
    list;
  }

}
