package com.Producer.Generators

import spire.compat.fractional

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.math.Fractional.Implicits.infixFractionalOps
import scala.util.Random
import com.ProductOrder

object MedicineGenerator {
  private final var medicineFile = "data/medicine/medicine_2021.txt"
  private var medicineList:ListBuffer[String] = ListBuffer() //object medicine list starts empty
  var isListFilled:Boolean = false

  // call this function before ever calling getMedicine function so that medicineList variable is not empty. This way we only ever have read the file once
  def fillMedicineList():Unit = {
    if (!isListFilled) {
      for (lines <- Source.fromFile(medicineFile).getLines()) {
        medicineList += lines
      }
    }
    isListFilled = true
  }

  // this fills in the product related fields of the ProductOrder object
  def getMedicine(po: ProductOrder):ProductOrder = {
    fillMedicineList()
    val ran = new Random()
    val rownum = Math.abs(ran.nextInt(medicineList.length))
    val row = medicineList(Math.abs(rownum)).split(",")
    val product = row(0)
    val price = row(2).toDouble
    val quantity = Math.abs(ran.nextInt(10))
    val totalPrice = (math floor price * quantity * 100) / 100
    val result = product + totalPrice
    po.product_name = product
    po.product_category = "Medicine"
    po.price = Math.abs(totalPrice)
    po.qty = quantity
    return po
  }
}