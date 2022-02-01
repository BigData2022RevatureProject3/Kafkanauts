package com.Consumer

import com.ProductOrder
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties


object ConsumerDigester {
  val offlineConsumerData = os.pwd / "src" / "main" / "scala" / "com" / "Consumer" / "consumer_data.csv"
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
      .map(_.split("\\|"))
      .map(parseProductOrder)
      .map(_.get)
      .toList
    val invalidOrdersList = invalidData.toList

    consumer.close()

    (validOrdersList, invalidOrdersList)
  }

  private def parseProductOrder(productOrder: Array[String]): Option[ProductOrder] = {
    productOrder.map(_.trim)
    try {
      val order_id = if(productOrder(0).matches("^[0-9]+$")) Some(productOrder(0).toLong) else None
      val customer_id = if(productOrder(1).matches("^[0-9]+$")) Some(productOrder(1).toLong) else None
      val customer_name = if(productOrder(2).nonEmpty) Some(productOrder(2)) else None
      val product_id = if(productOrder(3).matches("^[0-9]+$")) Some(productOrder(3).toLong) else None
      val product_name = if(productOrder(4).nonEmpty) Some(productOrder(4)) else None
      val product_category = if(productOrder(5).nonEmpty) Some(productOrder(5)) else None
      val payment_type = if(productOrder(6).nonEmpty) Some(productOrder(6)) else None
      val qty = if(productOrder(7).matches("^[0-9]+$")) Some(productOrder(7).toLong) else None
      val price = if(productOrder(8).matches("^[0-9]+$")) Some(productOrder(8).toDouble) else None
      val datetime = if(productOrder(9).nonEmpty) Some(productOrder(9)) else None
      val country = if(productOrder(10).nonEmpty) Some(productOrder(10)) else None
      val city = if(productOrder(11).nonEmpty) Some(productOrder(11)) else None
      val ecommerce_website_name = if(productOrder(12).nonEmpty) Some(productOrder(12)) else None
      val payment_txn_id = if(productOrder(13).matches("^[0-9]+$")) Some(productOrder(13).toLong) else None
      val payment_txn_success = if(productOrder(14).nonEmpty) Some(productOrder(14)) else None
      val failure_reason = if(productOrder(15).nonEmpty) Some(productOrder(15)) else None
      val values = List(order_id, customer_id, customer_name, product_id, product_name, product_category, payment_type, qty,
        price, datetime, country, city, ecommerce_website_name, payment_txn_id, payment_txn_success, failure_reason)
      if(values.exists(_.isEmpty)) {
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
    valid.foreach(ProductOrder.toString(_))
    println("------------------------INVALID--------------------")
    invalid.foreach(println)
  }
}
