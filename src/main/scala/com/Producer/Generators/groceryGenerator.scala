package com.Producer.Generators

import com.ProductOrder
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Random

object groceryGenerator {
  private final val ran = new Random()
  private final val groceryUSFile = "clean_data/clean_us_grocery_data.csv"
  private final val groceryChinaFile = "clean_data/clean_china_grocery_data.csv"
  private final val grocerySpainFile = "clean_data/clean_china_grocery_data.csv"

    def genUsGrocery(po:ProductOrder): ProductOrder = {
      val groceryUSList: ListBuffer[String] = ListBuffer()
      for (line <- Source.fromFile(groceryUSFile).getLines.drop(1)) {
        groceryUSList += line
      }
      val groceryId = ran.nextInt()
      val grocery = groceryUSList(groceryId).split(",")
      val product = grocery(1)
      val price = grocery(5).toDouble
      val quantity = ran.nextInt()
      val totalPrice = (math floor price * quantity)
      po.product_name = product
      po.product_category = "Groceries"
      po.price = totalPrice
      po.qty = quantity
      return po
    }
  def genChinaGrocery(po:ProductOrder): ProductOrder = {
    val groceryChinaList: ListBuffer[String] = ListBuffer()
    for (line <- Source.fromFile(groceryChinaFile).getLines.drop(1)) {
      groceryChinaList += line
    }
    val groceryId = ran.nextInt()
    val grocery = groceryChinaList(groceryId).split(",")
    val product = grocery(1)
    val price = grocery(5).toDouble
    val quantity = ran.nextInt()
    val totalPrice = (math floor price * quantity)
    po.product_name = product
    po.product_category = "Groceries"
    po.price = totalPrice
    po.qty = quantity
    return po
  }
  def genSpainGrocery(po:ProductOrder): ProductOrder = {
    val grocerySpainList: ListBuffer[String] = ListBuffer()
    for (line <- Source.fromFile(grocerySpainFile).getLines.drop(1)) {
      grocerySpainList += line
    }
    val groceryId = ran.nextInt()
    val grocery = grocerySpainList(groceryId).split(",")
    val product = grocery(1)
    val price = grocery(5).toDouble
    val quantity = ran.nextInt()
    val totalPrice = (math floor price * quantity)
    po.product_name = product
    po.product_category = "Groceries"
    po.price = totalPrice
    po.qty = quantity
    return po
  }
}