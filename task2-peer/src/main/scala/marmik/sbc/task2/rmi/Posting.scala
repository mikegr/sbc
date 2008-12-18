package marmik.sbc.task2.rmi

import java.lang.Integer;
import java.util._


class Posting(var id:Integer, var parent:Integer,
              var author:String, var subject:String,
              var content:String, var date:GregorianCalendar) extends java.io.Serializable {

  val serialVersionUID:long = 3198400326138638466L;

  def this() = this(-1,-1,"","","",null);

}
