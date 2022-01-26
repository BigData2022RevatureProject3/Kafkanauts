package com.Tools

class paymentTypeGenerator{
  def genPaymentType():String={
    val pTypes = List("card","Internet Banking","UPI","Wallet")
    val ran = scala.util.Random.nextInt(4)
    pTypes(ran)
  }

}
