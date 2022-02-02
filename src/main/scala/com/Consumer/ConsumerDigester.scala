package com.Consumer

import com.ProductOrder
import org.apache.kafka.clients.consumer.KafkaConsumer
import os.RelPath

import java.util.Properties


object ConsumerDigester {
  val offlineConsumerData = os.pwd / RelPath("team2/products.csv")
  def initializeSubscription(): (Properties, KafkaConsumer[Nothing, Nothing], List[String]) = {
    val props:Properties = new Properties()
    props.put("group.id", "test")
    props.put("bootstrap.servers","[::1]:9092")
    props.put("key.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "1000")
    val consumer = new KafkaConsumer(props)
    val topics = List("text_topic")
    (props, consumer, topics)
  }

  def startConsuming(consumer: KafkaConsumer[Nothing, Nothing]): (List[ProductOrder], List[String]) = {
    //Read information from producer
    val (validData, invalidData) = os
      .read
      .lines(offlineConsumerData)
      .partition(ProductOrder.isValidOrder)

    val validOrdersList = validData
      .map(parseProductOrder)
      .filter(_.isDefined)
      .map(_.get)
      .toList

    val invalidOrdersList = invalidData.toList

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
      val payment_type = if(splitPO(6).nonEmpty) Some(splitPO(6)) else None
      val qty = if(splitPO(7).matches("^[0-9]+$")) Some(splitPO(7).toLong) else None
      val price = if(splitPO(8).matches("^[0-9]+.[0-9]{2}$")) Some(splitPO(8).toDouble) else None
      val datetime = if(splitPO(9).nonEmpty) Some(splitPO(9)) else None
      val country = if(splitPO(10).nonEmpty) Some(splitPO(10)) else None
      val city = if(splitPO(11).nonEmpty) Some(splitPO(11)) else None
      val ecommerce_website_name = if(splitPO(12).nonEmpty) Some(splitPO(12)) else None
      val payment_txn_id = if(splitPO(13).matches("^[0-9]+$")) Some(splitPO(13).toLong) else None
      val payment_txn_success = if(splitPO(14).nonEmpty) Some(splitPO(14)) else None
      val failure_reason = if(splitPO(15).nonEmpty) Some(splitPO(15)) else None
      val values = List(order_id, customer_id, customer_name, product_id, product_name, product_category, payment_type, qty,
        price, datetime, country, city, ecommerce_website_name, payment_txn_id, payment_txn_success, failure_reason)
      if(values.exists(_.isEmpty)) {
        os.write.append(os.pwd / RelPath("team2/invalid_products.csv"), productOrder+"\n",createFolders = true)
        return None
      }
      // TODO Check with David to see if I should exclude failure_reason
      Some(ProductOrder(order_id.get, customer_id.get, customer_name.get, product_id.get, product_name.get,
        product_category.get, payment_type.get, qty.get, price.get, datetime.get, country.get, city.get,
        ecommerce_website_name.get, payment_txn_id.get, payment_txn_success.get, failure_reason.get))
    }
  }

  def main(args: Array[String]): Unit = {
    val (props, consumer, topics) = initializeSubscription()
    val (valid, invalid) = startConsuming(consumer)

    println("------------------------VALID----------------------")
    valid.foreach(x => println(ProductOrder.toString(x)))
    println("------------------------INVALID--------------------")
    invalid.foreach(println)
  }
}
