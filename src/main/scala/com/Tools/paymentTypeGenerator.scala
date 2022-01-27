package com.Tools

object paymentTypeGenerator{
  def genPaymentType():String={
    val pTypes = List("Card","Internet Banking","UPI","Wallet")
    val ran = scala.util.Random.nextInt(4)
    pTypes(ran)
  }
}
