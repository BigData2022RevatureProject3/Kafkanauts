package com.Consumer

import com.ProductOrder
import os.RelPath

// Meant for validating team2 data
object ConsumerDigesterTeam2 {
  val offlineConsumerData = os.pwd / RelPath("team2/tiffany.csv")
  val validPath = os.pwd / RelPath("team2/valid_products.csv")
  val invalidPath = os.pwd / RelPath("team2/invalid_products.csv")

  def startConsuming(): (List[ProductOrder], List[String]) = {
    println("Start validating")
    //Read information from producer
    val (validData, invalidData) = os
      .read
      .lines(offlineConsumerData)
      .partition(_ => true)
//      .partition(ProductOrder.isValidOrder)

    println(validData.length, "Valid data")
    val validOrdersList = validData
      .map(parseProductOrder)
      .filter(_.isDefined)
      .map(_.get)
      .toList



    val poStrs = validOrdersList.map(ProductOrder.toString).mkString("\n")

    val invalidOrdersList = invalidData.toList
    os.write.over(invalidPath, invalidOrdersList.mkString, createFolders = true)
    os.write.over(validPath, poStrs, createFolders = true)

    //    consumer.close()

    (validOrdersList, invalidOrdersList)
  }

  private def parseProductOrder(productOrder: String): Option[ProductOrder] = {
    val splitPO = productOrder.split("\\|")
    splitPO.map(_.trim)
    try {
      val order_id = if(splitPO(0).matches("^[0-9]+$")) Some(splitPO(0).toLong) else None
      val customer_id = if(splitPO(1).matches("^[0-9]+$")) Some(splitPO(1).toLong) else None
      val customer_name = if(splitPO(2).nonEmpty) Some(splitPO(2)) else None
      val product_id = if(splitPO(3).matches("^[0-9]+$")) Some(splitPO(3).toLong) else None
      val product_name = if(splitPO(4).nonEmpty) Some(splitPO(4)) else None
      val product_category = if(splitPO(5).nonEmpty) Some(splitPO(5)) else None
      val price = if(splitPO(6).matches("^[0-9]+.[0-9]{2}$")) Some(splitPO(8).toDouble) else None
      val qty = if(splitPO(7).matches("^[0-9]+$")) Some(splitPO(7).toLong) else None
      val payment_type = if(splitPO(8).nonEmpty) Some(splitPO(6)) else None
      val datetime = if(splitPO(9).nonEmpty) Some(splitPO(9)) else None
      val country = if(splitPO(10).nonEmpty) Some(splitPO(10)) else None
      val city = if(splitPO(11).nonEmpty) Some(splitPO(11)) else None
      val ecommerce_website_name = if(splitPO(12).nonEmpty) Some(splitPO(12)) else None
      val payment_txn_id = if(splitPO(13).matches("^[0-9]+$")) Some(splitPO(13).toLong) else None
      val payment_txn_success = if(splitPO(14).nonEmpty) Some(splitPO(14)) else None
      val failure_reason = Some(splitPO(15))
      val values = List(order_id, customer_id, customer_name, product_id, product_name, product_category, payment_type, qty,
        price, datetime, country, city, ecommerce_website_name, payment_txn_id, payment_txn_success)

      if (payment_txn_success.get == "N" && failure_reason.get.isEmpty){
        os.write.append(invalidPath, productOrder+"\n",createFolders = true)
        return None
      }
      if(values.exists(_.isEmpty)) {
        os.write.append(invalidPath, productOrder+"\n",createFolders = true)
        return None
      }
      // TODO Check with David to see if I should exclude failure_reason
      Some(ProductOrder(order_id.get, customer_id.get, customer_name.get, product_id.get, product_name.get,
        product_category.get, payment_type.get, qty.get, price.get, datetime.get, country.get, city.get,
        ecommerce_website_name.get, payment_txn_id.get, payment_txn_success.get, failure_reason.get))
    } catch {
      case e: Throwable =>
        os.write.append(invalidPath, productOrder+"\n",createFolders = true)
        None
    }
  }

  def main(args: Array[String]): Unit = {
    val (valid, invalid) = startConsuming()

    println("------------------------VALID----------------------")
    valid.foreach(ProductOrder.print)
    println("------------------------INVALID--------------------")
    invalid.foreach(println)
  }
}
