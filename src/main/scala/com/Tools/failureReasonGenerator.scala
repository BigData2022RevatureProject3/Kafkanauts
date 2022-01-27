package com.Tools
import scala.collection.mutable.ListBuffer
import scala.io.Source
class failureReasonGenerator{
  private final val reasons = "clean_data/transaction_failure_reasons.txt"
  private final val lb = new ListBuffer[String]
  private final val ran = scala.util.Random
  for(line <- Source.fromFile(reasons).getLines()){
    lb += line
  }
  def genFailReason(isFailed:String):String={
      if(isFailed == "Y"){
          lb(ran.nextInt(lb.length+1))
      }else{
        "Payment Was Success"
      }
  }
}
