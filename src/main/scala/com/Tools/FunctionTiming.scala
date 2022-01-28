package com.Tools

object FunctionTiming{
  def start(): Long ={System.currentTimeMillis()}
  def end(a:Long): Unit ={
    println(s"Function ran for ${System.currentTimeMillis() - a} milliseconds")
  }


//Usage
//  val s = start()
//  Some random function in the middle
//  end(s)


}
