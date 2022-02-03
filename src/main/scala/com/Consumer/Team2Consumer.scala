package com.Consumer

import com.ProductOrder
import org.json4s.scalap.scalasig.ThisType
import os.RelPath

import scala.collection.mutable.ListBuffer

object Team2Consumer {
  val doubleRegex = "[0-9]+([.][0-9]|[.][0-9]{2})?"
  val longRex = "[0-9]+"
  val dateRegex = "[0-9]{4}[-](0[1-9]|1[0-2])[-](0[1-9]|1[0-9]|2[0-9]|3[0-1])[T][0-9]{2}[:][0-9]{2}[:][0-9]{2}"
  var failCounts = ListBuffer(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
  var failReason = 0
  var errorReason = 0
  var nullCount = 0
  var longCount = 0
  var doubleCount = 0
  var dateCount = 0

  def main(args: Array[String]): Unit = {
    val filename  = "tiffany"
    val fileType = ".csv"
    startValidating(filename, fileType)
  }

  def startValidating(fileName: String, fType: String): Unit = {
    println("Starting validation")
    val initialReadPath = os.pwd / RelPath("team2/" + fileName + fType)
    val validPath = os.pwd / RelPath(s"team2/${fileName}-valid-data.csv")
    val invalidPath = os.pwd / RelPath(s"team2/${fileName}-invalid-data.csv")
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
//    List("01052160|80000779|Sabiha Whelan|800002|Astro A50|Electronics|299.99|6|Wallet|2022-01-10T11:50:25|France|Marseille|ingen.com|1052160|Y|")
      .map(parseProductOrder(_, invalidPath))
      .filter(_.isDefined)
      .map(_.get)
      .map(ProductOrder.toString)
      .mkString("\n")
    failCounts.zipWithIndex.foreach(println)
    println("fail: ", failReason)
    println("null", nullCount)
    println("eror: ", errorReason)
    println("long ", longCount)
    println("double ", doubleCount)
    println(s"Ended validation, writing valid orders to $validPath")
    println("Total ", failCounts.sum + failReason + nullCount + errorReason + longCount + doubleCount + dateCount)
    os.write(validPath, validOrders, createFolders = true)
  }

  def parseProductOrder(po: String, invalidPath: os.pwd.ThisType): Option[ProductOrder] = {
    val splitPO = po.split("\\|")
    if (!List(14, 15).contains(splitPO.length))
      return writeInvalid(po, invalidPath)

    try {
      val order_id = getLong(splitPO(0))
      val customer_id = getLong(splitPO(1))
      val customer_name = getString(splitPO(2))
      val product_id = getLong(splitPO(3))
      val product_name = getString(splitPO(4))
      val product_category = getString(splitPO(5))
      val price = getPrice(splitPO(6))
      val qty = getLong(splitPO(7))
      val payment_type = getString(splitPO(8))
      val datetime = getString(splitPO(9)) // TODO: Change to date
      val country = getString(splitPO(10))
      val city = getString(splitPO(11))
      val ecommerce_website_name = getString(splitPO(12))
      val payment_txn_id = getLong(splitPO(13))
      val payment_txn_success = getString(splitPO(14))
      var failure_reason: Option[String] = Some("")
      val values = List(order_id, customer_id, customer_name, product_id, product_name, product_category, payment_type, qty,
        price, datetime, country, city, ecommerce_website_name, payment_txn_id, payment_txn_success)
      if (values.exists(_.isEmpty)) {
        values.zipWithIndex.filter(_._1.isEmpty).map(_._2).foreach(i => failCounts(i) += 1)
//        println(po)
//        println(values.zipWithIndex.filter(_._1.isEmpty).mkString("|"))
//        println(po)
        return writeInvalid(po, invalidPath)

      }
      if (payment_txn_success.get == "N") {
        failReason = failReason + 1
        failure_reason = getString(splitPO(15))
        if (failure_reason.isEmpty) {
          return writeInvalid("", invalidPath)
        }
      }
      return Some(ProductOrder(order_id.get, customer_id.get, customer_name.get, product_id.get, product_name.get, product_category.get,
        payment_type.get, qty.get, price.get, datetime.get, country.get, city.get, ecommerce_website_name.get, payment_txn_id.get, payment_txn_success.get, failure_reason.getOrElse("")))
    } catch {
      case e: Throwable =>
        errorReason = errorReason + 1
        return writeInvalid("", invalidPath)
    }
  }

  def writeInvalid(po: String, invalidPath: os.pwd.ThisType): Option[ProductOrder] = {
    os.write.append(invalidPath, po + "\n", createFolders = true)
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
//  def getLong2(str: String): Option[Long] = {
//    try {
//      if (str.matches(longRex)) {
//        val value = str.toLong
//        if (value > 0)
//          return Some(value)
//      }
//    } catch {
//      case _: Throwable => return None
//    }
//    None
//  }
//  def getPrice2(str: String): Option[Double] = {
//    try {
//      if(str.matches(doubleRegex))
//        Some(str.toDouble)
//      else
//        None
//    } catch {
//      case _: Throwable => None
//    }
//  }

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

  def isDate(str: String): Option[String] = {
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
