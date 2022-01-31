package com.Producer.Generators

import scala.util.Random
import com.ProductOrder

object MedicineGenerator {
  private final var medicineList = os.read.lines(os.pwd / "clean_data" /"medicine"/ "medicine_2021.txt").drop(1).toList

  // this fills in the product related fields of the ProductOrder object
  def getMedicine(po: ProductOrder):ProductOrder = {
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
