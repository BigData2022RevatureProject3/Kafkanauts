package com

class customerIDGenerator{
  def genCustomerID():String={
    val ran = scala.util.Random
    val code= (100000 + ran.nextInt(900000)).toString()
    code
  }
}
