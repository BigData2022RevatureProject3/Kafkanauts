package com.Producer

import com.Producer.Generators._
import com.ProductOrder
import com.Tools.CountryFunctions.{chinaScale, spainScale, usScale}
import com.Tools.{CountryFunctions, MathHelper}

import scala.util.Random

/**
 * This object helps store the rate of production of various goods organized by country, day of week and product category
 */
object GenHelper {
  val countries = List("China", "United States", "Spain")

  val chinaDaily: List[Double => Double] = CountryFunctions.getChineseDaily()
  val usDaily: List[Double => Double] = CountryFunctions.getUSDaily()
  val spainDaily: List[Double => Double] = CountryFunctions.getSpainDaily()

  // TODO: Finish and make canonical
  val categories = List("Gas", "Groceries", "Medicine")
  //  val categories = List("Gas", "Medicine")
  //  Future categories: E-Commerce
  val corruptionChance: Double = 0.03

  var orderIDAccumulator = 1000 // A globally incremented value.


  def getCountryProbabilities(dayPercent: Double, day: Int): List[Double] = {
    List(chinaDaily(day)(dayPercent) * chinaScale, usDaily(day)(dayPercent) * usScale, spainDaily(day)(dayPercent) * spainScale)
  }

  def addCategory(po: ProductOrder, chinaProbs: List[Double], usProbs: List[Double], spainProbs: List[Double]): ProductOrder = {
    po.product_category = po.country match {
      case "China" => MathHelper.chooseFromWeightedList(categories, chinaProbs)
      case "United States" => MathHelper.chooseFromWeightedList(categories, usProbs)
      case "Spain" => MathHelper.chooseFromWeightedList(categories, spainProbs)
    }
    po
  }

  def addProduct(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
    po.product_category match {
      case "Medicine" => MedicineGenerator.getMedicine(po)
      case "Music" => MusicGenerator.genMusic(po)
      case "Gas" => GasStationGenerator.generateStations(po)
      case "Grocery" => GroceryGenerator.generateGroceries(po, day)
      case _ => po.product_category = "Medicine"
        MedicineGenerator.getMedicine(po)
      //      case "Misc." => po // to be added
      case "Gas" => po // to be added
      case "Grocery" => po // to be added
      case "E-Commerce" => po // to be added
    }
  }

  def addCustomerInfo(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
    CustomerInfoGenerator.generateCustomer(po)
  }

  //  def addTransactionInfo(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
  def addTransactionInfo(po: ProductOrder): ProductOrder = {
    TransactionInfoGenerator.addTransactionInfo(po)
    po
  }

  def toFinalString(po: ProductOrder): String = {
    val min = 1
    val max = 100
    if ((Random.nextInt(max - min) + min) / 100.00 < GenHelper.corruptionChance) {
      TrashMaker5000.makeTrash(po)
    } else {
      ProductOrder.toString(po)
    }
  }

  def main(args: Array[String]): Unit = {
  }

}
