package com.Consumer

import com.ProductOrder
import com.Tools.{FunctionTiming, SparkHelper}
import org.apache.spark.sql.Dataset
import os.RelPath

import scala.collection.mutable.ListBuffer

object ConsumerParser {
  val doubleRegex = "[0-9]+([.][0-9]|[.][0-9]{2})?"
  val longRex = "[0-9]+"
  val dateRegex = "[0-9]{4}[-](0[1-9]|1[0-2])[-](0[1-9]|1[0-9]|2[0-9]|3[0-1])[T][0-9]{2}[:][0-9]{2}[:][0-9]{2}"
  val colNames = List("order_id", "customer_id", "customer_name", "product_id", "product_name", "product_category", "payment_type",
    "qty", "price", "datetime", "country", "city", "ecommerce_website_name", "payment_txn_id", "payment_txn_success", "failure_reason")

  var failCounts = ListBuffer(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
  var failReason = 0
  var errorReason = 0
  var nullCount = 0
  var longCount = 0
  var doubleCount = 0
  var dateCount = 0
  var anyReason = 0

  def main(args: Array[String]): Unit = {
    val folder = "team1"
    val filename  = "test"
    val fileType = ".csv"
    val start = FunctionTiming.start()
    startValidating(folder, filename, fileType, theirData = false)
    FunctionTiming.end(start)
  }

  def parseIntoDataSet(rawData: Seq[String], isTheirData: Boolean): Dataset[ProductOrder] = {
    val spark = SparkHelper.spark
    import spark.implicits._
    val l = ProductOrder.toString(ProductOrder.getSampleOrder()).split("\\|").length

    val validOrders = rawData
      .map(parseTheirProductOrder(_, isTheirData))
      .filter(_.isDefined)
      .map(_.get)

    validOrders.toDS()
  }


  def startValidating(folder: String, fileName: String, fType: String, theirData: Boolean): Unit = {
    val start = FunctionTiming.start()

    println("Starting validation")
    val path = s"$folder/$fileName$fType"
    val initialReadPath = os.pwd / RelPath(path)
    val validPath = os.pwd / RelPath(s"$folder/${fileName}-valid-data.csv")
    val invalidPath = os.pwd / RelPath(s"$folder/${fileName}-invalid-data.csv")
    println(s"Reading from $initialReadPath")
    println(s"Writing invalid files to $invalidPath")

    if (os.exists(validPath))
      os.remove(validPath)

    if (os.exists(invalidPath))
      os.remove(invalidPath)

    val validOrders = os
      .read
      .lines
      .stream(initialReadPath)
      .filter(_.nonEmpty)
      .map(_.trim)
      .map(parseTheirProductOrder(_, theirData, Some(invalidPath)))
      .filter(_.isDefined)
      .map(_.get)
      .map(ProductOrder.toString)
      .toList

    os.write(validPath, validOrders.map(_ + "\n"), createFolders = true)

    val validCnt = validOrders.length.toDouble
    println(s"\nTotal Valid orders: ${validCnt}")
    if (os.exists(invalidPath)) {
      val invalidCnt = os.read.lines.stream(invalidPath).toList.length.toDouble
      println(s"Total Invalid orders: ${invalidCnt}")
      println(s"Corruption rate: ${invalidCnt * 100.0 / (validCnt + invalidCnt)}")
    } else
      println("Corruption rate: 0%!")

    println("\nColumns - Number times failed: ")
    println("-------------------------------")
    failCounts.zip(colNames).foreach(f => println(f"  ${f._2}%-25s failed ${f._1}%-3d times"))

    println("\nFail: " + failReason)
    println("Null: " + nullCount)
    println("Error: " + errorReason)
    println("Long " + longCount)
    println("Double: ", + doubleCount)
    println("Total for any reason: " + anyReason)

    println(s"\nEnded validation, writing valid orders to $validPath")
    FunctionTiming.end(start)
  }

  def parseTheirProductOrder(po: String, isTheirData: Boolean, invalidPath: Option[os.pwd.ThisType] = None): Option[ProductOrder] = {
    val splitPO = po.split("\\|")
    if (!List(15, 16).contains(splitPO.length))
      return writeInvalid(s"${splitPO.length} columns|" + po, invalidPath)

    try {
      val order_id = getLong(splitPO(0))
      val customer_id = getLong(splitPO(1))
      val customer_name = getString(splitPO(2))
      val product_id = getLong(splitPO(3))
      val product_name = getString(splitPO(4))
      val product_category = getString(splitPO(5))
      // 6 7

      val (priceIdx, qtyIdx) = if (isTheirData) (6,7) else (8,7)
      val price = getPrice(splitPO(priceIdx))
      val qty = getLong(splitPO(qtyIdx))
      val payment_type = getString(splitPO(8))
      val datetime = getDate(splitPO(9))
      val country = getString(splitPO(10))
      val city = getString(splitPO(11))
      val ecommerce_website_name = getString(splitPO(12))
      val payment_txn_id = getLong(splitPO(13))
      val payment_txn_success = getString(splitPO(14))
      var failure_reason: Option[String] = Some("No Reason")
      val values = List(order_id, customer_id, customer_name, product_id, product_name, product_category, payment_type, qty,
        price, datetime, country, city, ecommerce_website_name, payment_txn_id, payment_txn_success)
      if (values.exists(_.isEmpty)) {
        values.zipWithIndex.filter(_._1.isEmpty).map(_._2).foreach(i => failCounts(i) += 1)
        return writeInvalid("Missing/Wrong type|" + po, invalidPath)
      }

      if (payment_txn_success.get == "N" && splitPO.length == 16) {
        failure_reason = getString(splitPO(15))
        if (failure_reason.isEmpty) {
          failReason += 1
          return writeInvalid("Missing failure reason|" + po, invalidPath)
        }

      }

      return Some(ProductOrder(order_id.get, customer_id.get, customer_name.get, product_id.get, product_name.get, product_category.get,
        payment_type.get, qty.get, price.get, datetime.get, country.get, city.get, ecommerce_website_name.get, payment_txn_id.get, payment_txn_success.get, failure_reason.getOrElse("")))
    } catch {
      case e: Throwable =>
        errorReason += 1
        return writeInvalid(s"Error - ${e.toString}|" + po, invalidPath)
    }
  }


  def writeInvalid(po: String, invalidPath: Option[os.pwd.ThisType]): Option[ProductOrder] = {
    anyReason += 1
    if (invalidPath.isDefined)
      os.write.append(invalidPath.get, po.replace("\n", "") + "\n", createFolders = true)
    None
  }

  def getLong(str: String): Option[Long] = {
    try {
      if (str.matches(longRex)) {
        val value = str.toLong
        if (value > 0)
          return Some(value)
      }
    } catch {
      case _: Throwable =>
        longCount += 1
        return None
    }
    None
  }
  def getPrice(str: String): Option[Double] = {
    try {
      if(str.matches(doubleRegex))
        Some(str.toDouble)
      else
        None
    } catch {
      case _: Throwable =>
        doubleCount += 1
        None
    }
  }

  def getString(str: String): Option[String] = {
    if (str.nonEmpty) {
      if (str == "null") {
        nullCount += 1
        return None
      } else
        return Some(str)
    } else
      None
  }

  def getDate(str: String): Option[String] = {
    val date = str.trim
    if(date.nonEmpty){
      if(str.matches(dateRegex))
        return Some(date)
      else
        dateCount += 1
    }
    None
  }
}
