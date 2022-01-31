package com.Producer.Generators

import com.ProductOrder
import com.Tools.MathHelper

import scala.collection.{Map, mutable}
import scala.util.Random

/**
 * This object helps store the rate of production of various goods organized by country, day of week and product category
 */
object GenHelper {
  val countries = List("China", "United States", "Spain")
  val globalScale = 5
  val chinaScale  = 1047
  val usScale     = 344
  val spainScale  = 47
  val chinaDaily: List[Double => Double] = CountryFunctions.getChineseDaily()
  val usDaily:    List[Double => Double] = CountryFunctions.getUSDaily()
  val spainDaily: List[Double => Double] = CountryFunctions.getSpainDaily()

  // TODO: Finish and make canonical
  val categories = List("Gas", "Medicine")
  val corruptionChance: Double = 0.03


  def getCountryProbabilities(dayPercent: Double, day: Int): List[Double] = {
    //    List(1402, 329, 47)
    List(chinaDaily(day)(dayPercent) * chinaScale, usDaily(day)(dayPercent) * usScale, spainDaily(day)(dayPercent) * spainScale)
  }

  def addCategory(po: ProductOrder, chinaProbs: List[Double], usProbs: List[Double], spainProbs: List[Double]): ProductOrder = {
    po.product_category = po.country match {
      case "China"         => MathHelper.chooseFromWeightedList(categories, chinaProbs)
      case "United States" => MathHelper.chooseFromWeightedList(categories, usProbs)
      case "Spain"         => MathHelper.chooseFromWeightedList(categories, spainProbs)
    }
    po
  }

  def addProduct(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
    po.country match {
      case "United States" => {
        po
      }
      case "China" => {
        po
      }
      case "Spain" => {
        po
      }
    }
  }

  def addCustomerInfo(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
    CustomerInfoGenerator.generateCustomer(po)
  }

  def addTransactionInfo(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
    po
  }

  def toFinalString(po: ProductOrder): String = {
    if (Random.nextDouble() < GenHelper.corruptionChance) {
      // TODO: Maybe outsource to dedicated corruption function?
      "lasdkfjlasdkfj"
    } else {
      ProductOrder.toString(po)
    }
  }

}
