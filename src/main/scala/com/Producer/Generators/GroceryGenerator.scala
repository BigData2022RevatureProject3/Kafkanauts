package com.Producer.Generators

import com.ProductOrder
import com.Tools.MathHelper

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Random

object GroceryGenerator {
  private final val ran = new Random()
  private final val groceryUSFile = "clean_data/clean_us_grocery_data.csv"
  private final val groceryChinaFile = "clean_data/clean_china_grocery_data.csv"
  private final val grocerySpainFile = "clean_data/clean_china_grocery_data.csv"
//Cleaned up code for US Generator
  val usGroceries = os
    .read
    .lines(os.pwd / "clean_data" / "clean_us_grocery_data.csv")
    .drop(1)
    .map(line => line.split("|"))
    .map(splitArray => {
      val name = splitArray(1)
      val price = splitArray(5).toDouble
      (name, price)
    })
    .toList

    def genUsGrocery(po:ProductOrder): ProductOrder = {

      val (name, price) = MathHelper.chooseFromList(usGroceries)
      val quantity = Random.nextInt(10) + 1
      po.product_name = name
      po.product_category = "Groceries"
      po.price = math.floor(quantity * price)
      po.qty = quantity
      po.product_id = ("name" + price.toString).hashCode()
      return po
    }
  //Still need to clean
  def genChinaGrocery(po:ProductOrder): ProductOrder = {

    val groceryChinaList: ListBuffer[String] = ListBuffer()
    for (line <- Source.fromFile(groceryChinaFile).getLines.drop(1)) {
      groceryChinaList += line
    }
    val groceryId = ran.nextInt()
    val grocery = groceryChinaList(groceryId).split("""\|""")
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
  //Still need to clean
  def genSpainGrocery(po:ProductOrder): ProductOrder = {
    val grocerySpainList: ListBuffer[String] = ListBuffer()
    for (line <- Source.fromFile(grocerySpainFile).getLines.drop(1)) {
      grocerySpainList += line
    }
    val groceryId = ran.nextInt()
    val grocery = grocerySpainList(groceryId).split("""\|""")
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