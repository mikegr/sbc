package marmik.sbc.task2

object TestMain {
  def main(args:Array[String]) {
    println("Start")
    junit.textui.TestRunner.run(classOf[TestXVSM]);
    println("end")
  }
}


