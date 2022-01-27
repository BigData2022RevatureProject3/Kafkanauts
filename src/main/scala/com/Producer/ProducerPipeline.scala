package com.Producer

import java.time.{LocalDate, LocalDateTime}
import java.time.temporal.ChronoUnit

/**
 * This object deals with creating a burst of orders for blocks of time,
 * assigning a country of origin and product category for each order
 * and fills out the rest of the info pseudo-randomly based off that starting info
 * It then sends it to a Kafka topic
 */
object ProducerPipeline {
  def main(args: Array[String]): Unit = {
//    val date: LocalDateTime = LocalDateTime.parse(LocalDate.now().toString)
//    println(date)
//
//    val newDate = date.plus(20, ChronoUnit.MINUTES)
//
//    println(newDate)

    val nowTime = LocalDate.now()
    nowTime.plus(15L, ChronoUnit.MINUTES)

    val dateTime = LocalDateTime.now()
    val datePlus = dateTime.plus(15, ChronoUnit.MINUTES)
    println(dateTime)
    println(datePlus)
  }

  def startProducing(startDate: String, minuteIncrements: Long = 15): Unit = {
    val timeIncrementMS = minuteIncrements * 60 * 1000
    Iterator
    // Get start time in ms
    // start Iterator/loop based on that start time
      // for each iteration, calculate day start/end time in ms
        // Calculate percent through the current day
        // calculate day of week
      // Calculate how many orders to make for this batch
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
