package com.Producer.Generators

import com.ProductOrder
import com.Tools.MathHelper

import scala.util.Random

object GroceryGenerator {

  //Test generator
//  def main(args: Array[String]): Unit = {
//    val po = ProductOrder.getInitialOrder(LocalDateTime.now())
//  po.country = "United States"
//    generateGroceries(po, 2)
//  println(ProductOrder.toString(po))
//  }
//Created usGroceries List
  val usGroceries = os
    .read
    .lines(DataValidator.validatedData("clean_data/clean_us_grocery_data.csv"))
    .drop(1)
    .map(line => line.split("\\|"))
    .map(splitArray => {
      val name = splitArray(1)
      val price = splitArray(5).toDouble
      (name, price)
    })
    .toList
  //Created chinaGroceries List
  val chinaGroceries = os
    .read
    .lines(DataValidator.validatedData("clean_data/clean_china_grocery_data.csv"))
    .drop(1)
    .map(line => line.split("\\|"))
    .map(splitArray => {
      val name = splitArray(1)
      val price = splitArray(5).toDouble
      (name, price)
    })
    .toList
  //Created spainGroceries List
  val spainGroceries = os
    .read
    .lines(DataValidator.validatedData("clean_data/clean_spain_grocery_data.csv"))
    .drop(1)
    .map(line => line.split("\\|"))
    .map(splitArray => {
      val name = splitArray(1)
      val price = splitArray(5).toDouble
      (name, price)
    })
    .toList
  //Create tacoTuesday List
  val tacoTuesday = os
    .read
    .lines(DataValidator.validatedData("clean_data/taco_master.csv"))
    .drop(1)
    .map(line => line.split("\\|"))
    .map(splitArray => {
      val name = splitArray(1)
      val price = splitArray(2).toDouble
      (name, price)
    })
    .toList

  //Create Groceries generator
  def generateGroceries(po:ProductOrder, day:Int ):ProductOrder={
    po.country match{
      case "United States" => genUsGrocery(po, day == 2)
      case "China" => genChinaGrocery(po)
      case "Spain" => genSpainGrocery(po)
    }
  }
//Taco generator
  def makeTaco(po:ProductOrder):ProductOrder = {
    val (name, price) = MathHelper.chooseFromList(tacoTuesday)
    val quantity = Math.abs(Random.nextInt(10) + 1)
    po.product_name = name
    po.product_category = "Groceries"
    po.price = math.floor(quantity * price * 100) / 100
    po.qty = quantity
    po.product_id = Math.abs(("name" + price.toString).hashCode())
    return po
  }
  //UsGrocery generator
    def genUsGrocery(po:ProductOrder, isTacoDay: Boolean): ProductOrder = {
      val quantity = Math.abs(Random.nextInt(10) + 1)
      if (isTacoDay && Random.nextDouble() < 0.5) {
        makeTaco(po)
      } else {
        val (name, price) = MathHelper.chooseFromList(usGroceries)
        po.product_name = name
        po.product_category = "Groceries"
        po.price = math.floor(quantity * price * 100) / 100
        po.qty = quantity
        po.product_id = Math.abs(("name" + price.toString).hashCode())
      }
      return po
    }
  //ChinaGrocery generator
  def genChinaGrocery(po:ProductOrder): ProductOrder = {
    val (name, price) = MathHelper.chooseFromList(chinaGroceries)
    val quantity = Math.abs(Random.nextInt(10) + 1)
    po.product_name = name
    po.product_category = "Groceries"
    po.price = math.floor(quantity * price * 100) / 100
    po.qty = quantity
    po.product_id = Math.abs(("name" + price.toString).hashCode())
    return po
  }
  //SpainGrocery generator
  def genSpainGrocery(po:ProductOrder): ProductOrder = {
    val (name, price) = MathHelper.chooseFromList(spainGroceries)
    val quantity = Math.abs(Random.nextInt(10) + 1)
    po.product_name = name
    po.product_category = "Groceries"
    po.price = math.floor(quantity * price * 100) / 100
    po.qty = quantity
    po.product_id = Math.abs(("name" + price.toString).hashCode())
    return po
  }
}