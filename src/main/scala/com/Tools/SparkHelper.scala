package com.Tools

import com.ProductOrder
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkHelper {

  val spark: SparkSession = {
    suppressLogs(List("org", "akka", "org.apache.spark", "org.spark-project"))
//    System.setProperty("hadoop.home.dir", "C:\\hadoop")
    val sparkSession = SparkSession
      .builder
      .appName("Spark Pipeline")
      .config("spark.master", "local")
      .enableHiveSupport()
      .getOrCreate()
    sparkSession.conf.set("hive.exec.dynamic.partition.mode", "nonstrict")
    sparkSession.sparkContext.setLogLevel("ERROR")
//    sparkSession.sql("CREATE DATABASE IF NOT EXISTS kafkanauts").queryExecution
//    sparkSession.sql("USE kafkanauts").queryExecution
    sparkSession
  }


  //spark.sql("CREATE TABLE IF NOT EXISTS consumedDataValid(order_id Long, customer_id Long, customer_name String, product_id Long, product_name String, product_category String, payment_type String, qty Long, price Double, datetime String, country String, city String, ecommerce_website_name String, payment_txn_id Long, payment_txn_success String, failure_reason String)").queryExecution
//  spark.sql("CREATE TABLE IF NOT EXISTS consumedDataInvalid(order_id String, customer_id String, " +
//    "customer_name String, product_id String, product_name String, product_category String, payment_type String, " +
//    "qty String, price String, datetime String, country String, city String, ecommerce_website_name String, " +
//    "payment_txn_id String, payment_txn_success String, failure_reason String)").queryExecution

  /**
   * This method performs the following data manipulation language stored in query.
   * @param spark The spark session
   * @param query This match criteria that is ran against the database.
   */
  private def executeDML(spark: SparkSession, query : String): Unit = {
    spark.sql(query).queryExecution
  }

  /**
   * This method returns the records that match the query information as a DataFrame.
   * @param spark The spark session
   * @param query This match criteria that is ran against the database.
   * @return returns the matched criteria as the instance of a DataFrame
   */
  private def executeQuery(spark: SparkSession, query: String): DataFrame = {
    spark.sql(query)
  }

  def addProductOrderToTable(productOrder: ProductOrder): Unit = {
    val order_id: Long = productOrder.order_id
    val customer_id: Long = productOrder.customer_id
    val customer_name: String = productOrder.customer_name
    val product_id: Long = productOrder.product_id
    val product_name: String = productOrder.product_name
    val product_category: String = productOrder.product_category
    val payment_type: String = productOrder.payment_type
    val qty: Long = productOrder.qty
    val price: Double = productOrder.price
    val datetime: String = productOrder.datetime
    val country: String = productOrder.country
    val city: String = productOrder.city
    val ecommerce_website_name: String = productOrder.ecommerce_website_name
    val payment_txn_id: Long = productOrder.payment_txn_id
    val payment_txn_success: String = productOrder.payment_txn_success
    val failure_reason: String = productOrder.failure_reason
      executeDML(spark, s"insert into consumedDataValid values($order_id, $customer_id, '$customer_name', " +
        s"$product_id, '$product_name', '$product_category', '$payment_type', $qty, $price, '$datetime', '$country'," +
        s"'$city', '$ecommerce_website_name', $payment_txn_id, '$payment_txn_success', '$failure_reason')")
  }

  def addProductOrderToTable(productOrderString: String): Unit = {
    val productOrder = productOrderString.split("\\|")
    val order_id = productOrder(0)
    val customer_id = productOrder(1)
    val customer_name = productOrder(2)
    val product_id = productOrder(3)
    val product_name = productOrder(4)
    val product_category = productOrder(5)
    val payment_type = productOrder(6)
    val qty = productOrder(7)
    val price = productOrder(8)
    val datetime = productOrder(9)
    val country = productOrder(10)
    val city = productOrder(11)
    val ecommerce_website_name = productOrder(12)
    val payment_txn_id = productOrder(13)
    val payment_txn_success = productOrder(14)
    val failure_reason = productOrder(15)
    executeDML(spark, s"insert into consumedDataValid values('$order_id', '$customer_id', '$customer_name', " +
      s"$product_id, '$product_name', '$product_category', '$payment_type', '$qty', '$price', '$datetime', '$country'," +
      s"'$city', '$ecommerce_website_name', '$payment_txn_id', '$payment_txn_success', '$failure_reason')")
  }

  /**
   * This methods stops the specified warnings from being printed to standard output.
   * @param params The list of dependency string names desired to be ignored.
   */
  def suppressLogs(params: List[String]): Unit = {
    // Levels: all, debug, error, fatal, info, off, trace, trace_int, warn
    import org.apache.log4j.{Level, Logger}
    params.foreach(Logger.getLogger(_).setLevel(Level.OFF))
  }
}
