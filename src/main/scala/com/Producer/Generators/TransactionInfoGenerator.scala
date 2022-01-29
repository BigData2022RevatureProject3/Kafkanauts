package com.Producer.Generators

import com.ProductOrder
import scala.util.Random

object TransactionInfoGenerator {
  def addTransactionInfo(po: ProductOrder): ProductOrder = {
    val r = new Random()

    //  Assigns a transaction ID to po object.
    po.payment_txn_id = Math.abs(r.nextLong)
    println(po.payment_txn_id)

    //  Calls Bao's paymentType function to set paymentType attribute of po object.
    PaymentTypeGenerator.genPaymentType(po)

    //   Assigns payment_txn_success status to po object.
    val ran2 = r.nextInt(10)
    ran2 match {
      case 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 => po.payment_txn_success = "Y"
      case _ => po.payment_txn_success = "N"
    }
    //   Calls Bao's genFailReason function to assign failure reason to po object.
    FailureReasonGenerator.genFailReason(po)

    return po

  }
}
