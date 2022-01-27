package com.Tools
import scala.collection.mutable.ListBuffer
import scala.io.Source
object failureReasonGenerator{
  private final val reasoncard = "clean_data/transaction_failure_reasons_card.txt"
  private final val reasonother = "clean_data/transaction_failure_reasons_other.txt"
  private final val lbcard = new ListBuffer[String]
  private final val lbother = new ListBuffer[String]
  private final val ran = scala.util.Random
  for(line <- Source.fromFile(reasoncard).getLines()){lbcard += line}
  for(line <- Source.fromFile(reasonother).getLines()){lbother += line}
  def genFailReason(payType:String,isFailed:String):String= {
    if (isFailed == "Y") {
      if (payType == "Card") {lbcard(ran.nextInt(lbcard.length))
      }else{lbother(ran.nextInt(lbother.length))}}
    else {"Payment Was Success"}
  }
}
