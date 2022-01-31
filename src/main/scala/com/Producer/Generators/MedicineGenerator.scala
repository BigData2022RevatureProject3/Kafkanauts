package com.Producer.Generators

import spire.compat.fractional

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.math.Fractional.Implicits.infixFractionalOps
import scala.util.Random
import com.ProductOrder

object MedicineGenerator{
  private final var medicineFile = "data/medicine/medicine_2021.txt"
  private var medicineList:ListBuffer[String] = ListBuffer() //object medicine list starts empty

  // call this function before ever calling getMedicine function so that medicineList variable is not empty. This way we only ever have read the file once
  def fillMedicineList():Unit = {
    for (lines <- Source.fromFile(medicineFile).getLines()) {
      medicineList += lines
    }
  }

  // this fills in the product related fields of the ProductOrder object
  def getMedicine(po: ProductOrder):ProductOrder = {
    fillMedicineList()
    val ran = scala.util.Random
    val rowNum = Math.abs(ran.nextInt(medicineList.length))
    val row = medicineList(rowNum).split(",")
    val product = row(0)
    val price = row(2).toDouble
    val quantity = Math.abs(ran.nextInt(100))
    val totalPrice = (math floor price * quantity * 100) / 100
    po.product_name = product
    po.product_category = "Medicine"
    po.price = totalPrice
    po.qty = quantity
    po
  }
}