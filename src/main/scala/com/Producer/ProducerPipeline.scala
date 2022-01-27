package com.Producer

/**
 * This object deals with creating a burst of orders for blocks of time,
 * assigning a country of origin and product category for each order
 * and fills out the rest of the info pseudo-randomly based off that starting info
 * It then sends it to a Kafka topic
 */
object ProducerPipeline {
  def main(args: Array[String]): Unit = {

  }

  def startProducing(startDate: String, minuteIncrements: Long = 15): Unit = {
    // Get start time in ms
    // start Iterator/loop based on that start time
      // for each iteration, calculate day start/end time in ms
      // calculate day of week
      // Calculate how many orders to make
      // Assign each order a country/date
      // Then assign each order a product_category based on country of origin/date

      // fill in product info
      // fill in customer info
      // fill in order info (txn number, order status/failure reason)
      // convert to strings, making some strings corrupt
      // send all strings off to Producer

      // Log event, sleep computer
  }
}
