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

  def addCategory(poOpt: Option[ProductOrder], chinaProbs: List[Double], usProbs: List[Double], spainProbs: List[Double]): Option[ProductOrder] = {
    if (poOpt.isDefined) {
      val po = poOpt.get
      po.product_category = po.country match {
        case "China" => MathHelper.chooseFromWeightedList(categories, chinaProbs)
        case "United States" => MathHelper.chooseFromWeightedList(categories, usProbs)
        case "Spain" => MathHelper.chooseFromWeightedList(categories, spainProbs)
      }
      Some(po)
    } else None
  }

  def addProduct(poOpt: Option[ProductOrder], dayPercent: Double, day: Int): Option[ProductOrder] = {
    if (poOpt.isDefined) {
      val po = poOpt.get
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
        case "E-Commerce" => ECommGenerator.genECommOrder(po)
      }
      Some(po)
    } else None
  }

  def addCustomerInfo(poOpt: Option[ProductOrder], dayPercent: Double, day: Int): Option[ProductOrder] = {
    if (poOpt.isDefined) {
      val po = poOpt.get
      CustomerInfoGenerator.generateCustomer(po)
      Some(po)
    } else None
  }

  //  def addTransactionInfo(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
  def addTransactionInfo(poOpt: Option[ProductOrder]): Option[ProductOrder] = {
    if (poOpt.isDefined) {
      val po = poOpt.get
      TransactionInfoGenerator.addTransactionInfo(po)
      Some(po)
    } else None
  }

  def toFinalString(poOpt: Option[ProductOrder]): String = {
    if (poOpt.isDefined) {
      val po = poOpt.get
      if (Random.nextDouble() > GenHelper.corruptionChance)
        return ProductOrder.toString(po)
    }
    return TrashMaker5000.makeTrash(poOpt)
  }

  def main(args: Array[String]): Unit = {
  }

}
