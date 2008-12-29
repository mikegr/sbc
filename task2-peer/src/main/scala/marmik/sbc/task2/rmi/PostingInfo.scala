package marmik.sbc.task2.rmi

import java.lang.Integer;
import java.util._


@SerialVersionUID(3198400326138638466L)
class PostingInfo(var id:Integer, var parent:Integer,
              var author:String, var subject:String,
              var content:String, var date:GregorianCalendar) extends java.io.Serializable {

  def this() = this(-1,-1,"","","",null);

  override
  def equals(o:Any):Boolean = {
    if (o.isInstanceOf[PostingInfo]) {
      val pi = o.asInstanceOf[PostingInfo]
      if (pi.id == this.id &&
            pi.parent == this.parent &&
            pi.author == this.author &&
            pi.subject == this.subject &&
            pi.content == this.content &&
            pi.date == this.date) true;
    }
    false
  }

}
