package com.Producer.Generators

import com.ProductOrder
import com.Tools.MathHelper
import com.Producer.Generators._

import java.time.LocalDateTime
import scala.collection.{Map, mutable}
import scala.util.Random

/**
 * This object helps store the rate of production of various goods organized by country, day of week and product category
 */
object GenHelper {
  val countries = List("China", "United States", "Spain")
  // TODO: Finish and make canonical
  val categories = List("Gas", "Medicine", "Music", "Produce")
  val corruptionChance: Double = 0.03
  var orderIDAccumulator = 1000 // A globally incremented value.
  // Access the map by country to get a list.
  // Access list by dayOfWeek to get category Map.
  // Access that map to get a function from dayPercentage to activity at that time
  val prodFunctions: mutable.Map[String, List[Map[String, (Double) => Double]]] = mutable.Map()

  def getProductionFunctions(): Any = {
    var funcs = mutable.Map[String, List[Map[String, (Double) => Double]]]()
    val chinaMap = List(
      //      Map("Gas" -> ((percent: Double) => getChineseFunc())
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
    )

    val usMap = List(
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
    )

    val spainMap = List(
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
      (perc: Double) => 20.34,
    )

  }


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

//  def addTransactionInfo(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
  def addTransactionInfo(po: ProductOrder): ProductOrder = {
    TransactionInfoGenerator.addTransactionInfo(po)
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

  def main(args: Array[String]): Unit = {
    val input = scala.io.StdIn.readLine("Enter Int: ").toInt
    input match {
      case 1 => addTransactionInfoDemo
      case 2 =>
      case 3 =>
      case 4 =>
      case 5 =>
      case 6 =>
      case _ =>
    }
    def addTransactionInfoDemo: Unit = {
      val po = ProductOrder.getInitialOrder(LocalDateTime.now())
      println(ProductOrder.toString(po))
      for (i <- 1 to 1000) {
        addTransactionInfo(po)
//        println(po.payment_type + " | " + po.payment_txn_success + " | " + po.failure_reason)
        println(ProductOrder.toString(po))
      }
    }

  }

}
