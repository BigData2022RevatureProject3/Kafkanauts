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
  val chinaScale = 1047
  val usScale = 344
  val spainScale = 47
  // TODO: Finish and make canonical
  val categories = List("Gas", "Medicine", "Music", "Produce")
  val corruptionChance: Double = 0.03
  // Access the map by country to get a list.
  // Access list by dayOfWeek to get category Map.
  // Access that map to get a function from dayPercentage to activity at that time
  val countryFunctions = CountryFunctions.getCountryFunctions()
//  val globalTotals =
// TODO Finish global Totals/test

  def getGlobalBatchSize(dayPercent: Double): Int = {
    // TODO: David-Carlson Make it query functions globally
    Math.ceil(1 - Math.abs(0.5 - dayPercent) * 50).toInt + 50
  }

  def getCountryProbabilities(dayPercent: Double): List[Double] = {
    // TODO: Change calculation
    List(1402, 329, 47)
  }

  def addCategory(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
    // TODO: Change to per-country/day/percent choice
    po.product_category = MathHelper.chooseFromList(GenHelper.categories)
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
