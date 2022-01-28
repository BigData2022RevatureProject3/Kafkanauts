package com.Tools

import spire.compat.fractional

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.math.Fractional.Implicits.infixFractionalOps
import scala.util.Random
import com.ProductOrder

class medicineGenerator {
  private final var medicineFile = "data/medicine/medicine_2021.txt"
  def getMedicine(po: ProductOrder):ProductOrder = {
    var medicineList:ListBuffer[String] = ListBuffer()
    for (lines <- Source.fromFile(medicineFile).getLines()) {
      medicineList += lines
    }
    val ran = new Random()
    val rownum = ran.nextInt()
    val row = medicineList(rownum).split(",")
    val product = row(0)
    val price = row(2).toDouble
    val quantity = ran.nextInt()
    val totalPrice = (math floor price * quantity * 100) / 100
    val result = product + totalPrice
    po.product_name = product
    po.product_category = "Medicine"
    po.price = totalPrice
    po.qty = quantity
    po
    //medicineList.foreach(println)
    return po
  }
}
