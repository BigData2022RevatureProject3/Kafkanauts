package com.Tools

/**
 * Usage:
 * Val s = start()
 * Call a function
 * end(s)
 */
object FunctionTiming{
  def start(): Long = { System.currentTimeMillis() }
  def end(a:Long): Unit ={
    println(s"Function ran for ${System.currentTimeMillis() - a} milliseconds")
  }
}
