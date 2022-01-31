package com.Producer.Generators

import com.ProductOrder
import com.Tools.MathHelper
import java.time.LocalDateTime
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

  var orderIDAccumulator = 1000 // A globally incremented value.


  def getCountryProbabilities(dayPercent: Double, day: Int): List[Double] = {
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
      case "Medicine" => MedicineGenerator.getMedicine(po)
      case "Music" => po // to be added
      case "Gas" => po // to be added
      case "Grocery" => po // to be added
      case "Misc." => po // to be added
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
    if ((Random.nextInt(max-min) + min) / 100.00 < GenHelper.corruptionChance) {
      TrashMaker5000.makeTrash(po)
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
        po.price = 6.89
        addTransactionInfo(po)
//        println(po.payment_type + " | " + po.payment_txn_success + " | " + po.failure_reason)
        PriceMultiplier.multiplyPrice(po)
        println(ProductOrder.toString(po))
        println(toFinalString(po))
      }
    }

  }

}
