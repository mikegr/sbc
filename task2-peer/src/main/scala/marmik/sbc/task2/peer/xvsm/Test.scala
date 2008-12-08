package marmik.sbc.task2.peer.xvsm

import org.xvsm.interfaces._
import org.xvsm.core._
import org.xvsm.transactions._
import org.xvsm.coordinators._
import org.xvsm.selectors._

import marmik.sbc.task2.peer.xvsm.IterableConversions._

object Test {
   def main(args:Array[String]) {
     val site = new java.net.URI("tcpJava://localhost:4321");
     
     val capi = new Capi();
     val tx = capi.createTransaction(site, ICapi.INFINITE_TIMEOUT);
     val ctr = capi.createContainer(tx, site, "Forum", -1, new LindaCoordinator());
     
     val topic1:Tuple = new Tuple(new AtomicEntry[Long](1), 
                                 new AtomicEntry[String]("General"));
     
     val topic2:Tuple = new Tuple(new AtomicEntry[Long](2), 
                                 new AtomicEntry[String]("Uni"));
     
     capi.write(ctr, 0, tx, topic1);
     capi.write(ctr, 0, tx, topic2);
     
     val sel = new LindaSelector(new Tuple(new AtomicEntry[Long](2),null))
     
     val entries = capi.read(ctr, 0, tx, sel);
     
     entries.foreach ( entry => { 
         Console.println(entry.getEntryType);
         val t = entry.asInstanceOf[Tuple];
         Console.println("Id: " + t.getEntryAt(0).asInstanceOf[AtomicEntry[Long]].getValue);
         Console.println("Name: " + t.getEntryAt(1).asInstanceOf[AtomicEntry[String]].getValue);
         val iter = entry.asInstanceOf[Tuple].iterator;
         while(iter.hasNext) {
           Console println ("HASNEXT")
           Console println (iter.next);
         }
       }) 
     
     //capi.createContainer(
     
     
   }
}
