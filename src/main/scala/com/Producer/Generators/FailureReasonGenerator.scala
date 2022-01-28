package com.Producer.Generators

import scala.collection.mutable.ListBuffer
import scala.io.Source
import com.ProductOrder

object FailureReasonGenerator {
  private final val reasoncard = "clean_data/transaction_failure_reasons_card.txt"
  private final val reasonother = "clean_data/transaction_failure_reasons_other.txt"
  private final val lbcard = new ListBuffer[String]
  private final val lbother = new ListBuffer[String]
  private final val ran = scala.util.Random
  for (line <- Source.fromFile(reasoncard).getLines()) {
    lbcard += line
  }
  for (line <- Source.fromFile(reasonother).getLines()) {
    lbother += line
  }

  def genFailReason(po: ProductOrder): Unit = {
    if (po.payment_txn_success == "Y") {
      if (po.payment_type == "Card") {
        po.failure_reason = lbcard(ran.nextInt(lbcard.length))
      } else {
        po.failure_reason = lbother(ran.nextInt(lbother.length))
      }
    }
    else {
      po.failure_reason = "Payment Was Success"
    }
  }
}
