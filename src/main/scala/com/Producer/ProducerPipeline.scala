package com.Producer

import java.time.{LocalDate, LocalDateTime}
import java.time.temporal.ChronoUnit
import scala.io.StdIn

/**
 * This object deals with creating a burst of orders for blocks of time,
 * assigning a country of origin and product category for each order
 * and fills out the rest of the info pseudo-randomly based off that starting info
 * It then sends it to a Kafka topic
 */
object ProducerPipeline {
  def main(args: Array[String]): Unit = {
    //TODO: For David to look at
    print("Insert desired date: ")
    val input = StdIn.readLine()
    val Date = """^(\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])""".r

    val startDay = input match {
      case Date(year, month, day) => LocalDate.of(year.toInt, month.toInt, day.toInt).atStartOfDay()
      case _ => println("Invalid Input, Please follow format YYYY-MM-DD")
    }

    println("start datetime is: " + startDay)


    val nowTime = LocalDate.now().atTime(0,0,0)
    val nowTime1 = LocalDate.now().atStartOfDay()
    println(nowTime.plus(15, ChronoUnit.MINUTES))
    println(nowTime1.plus(15, ChronoUnit.MINUTES))

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
