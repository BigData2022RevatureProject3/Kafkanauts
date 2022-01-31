package com.Consumer

import com.ProductOrder
import com.ProductOrder.toString
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.util.Properties
import scala.collection.JavaConverters._

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

  def startConsuming(props: Properties, consumer: KafkaConsumer[Nothing, Nothing], topics: List[String], requiredFields: Int = 15): Unit = {
    //Read information from producer
    os
      .read
      .lines(offlineConsumerData)
      //.filter(productOrder => ProductOrder.isValidOrder(productOrder))
      .map(productOrder => productOrder.split("\\|"))
      .map(_.filter(_.nonEmpty))
      .filter(_.length == requiredFields) //Once website is done.... Increase to 16 an update values below making website productOrder(12) and so on
      .map(productOrder => {
        val order_id: Long = productOrder(0).toLong
        val customer_id: Long = productOrder(1).toLong
        val customer_name: String = productOrder(2)
        val product_id: Long = productOrder(3).toLong
        val product_name: String = productOrder(4)
        val product_category: String = productOrder(5)
        val payment_type: String = productOrder(6)
        val qty: Long = productOrder(7).toLong
        val price: Double = productOrder(8).toDouble
        val datetime: String = productOrder(9)
        val country: String = productOrder(10)
        val city: String = productOrder(11)
        val ecommerce_website_name: String = "www.example.com"
        val payment_txn_id: Long = productOrder(12).toLong
        val payment_txn_success: String = productOrder(13)
        val failure_reason: String = productOrder(14)
        ProductOrder(order_id, customer_id, customer_name, product_id, product_name, product_category, payment_type, qty, price, datetime, country, city, ecommerce_website_name, payment_txn_id, payment_txn_success, failure_reason)})
      .map(p => ProductOrder.toString(p))
      .foreach(println)

      consumer.close()
  }

  def main(args: Array[String]): Unit = {
    val (props, consumer, topics) = initializeSubscription()
    startConsuming(props, consumer, topics)
  }
}
