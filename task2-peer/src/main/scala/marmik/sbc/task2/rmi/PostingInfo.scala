package marmik.sbc.task2.rmi

import java.lang.Integer;
import java.util._


@SerialVersionUID(3198400326138638466L)
class PostingInfo(var id:Integer, var parent:Integer,
              var author:String, var subject:String,
              var content:String, var date:GregorianCalendar) extends java.io.Serializable {

  def this() = this(-1,-1,"","","",null);

}
