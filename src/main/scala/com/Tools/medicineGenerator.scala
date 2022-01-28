package com.Tools

import spire.compat.fractional

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.math.Fractional.Implicits.infixFractionalOps
import scala.util.Random

class medicineGenerator {
  private final var medicineFile = "data/medicine/medicine_2021.txt"
  def getMedicine():String = {
    var medicineList:ListBuffer[String] = ListBuffer()
    for (lines <- Source.fromFile(medicineFile).getLines()) {
      medicineList += lines
    }
    val ran = new Random()
    val rownum = ran.nextInt(5)
    val row = medicineList(rownum).split(",")
    val result1 = row(0) + ","
    val price = row(2).toDouble
    val result2 = (math floor price * 100) / 100
    val result = result1 + result2
    //medicineList.foreach(println)
    return result
  }
}

object test extends App {
  val test1 = new medicineGenerator
  println(test1.getMedicine)
}