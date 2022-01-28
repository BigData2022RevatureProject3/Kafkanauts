package com.Producer

import java.time.{LocalDate, LocalDateTime, ZoneOffset}
import java.time.temporal.ChronoUnit
import scala.io.StdIn
import scala.util.Random
import com.Tools.DateHelper._
import com.Producer.GenHelper._
import com.ProductOrder
import com.ProductOrder._
import com.Tools.DateHelper

/**
 * This object deals with creating a burst of orders for blocks of time,
 * assigning a country of origin and product category for each order
 * and fills out the rest of the info pseudo-randomly based off that starting info
 * It then sends it to a Kafka topic
 */
object ProducerPipeline {
  def main(args: Array[String]): Unit = {
    //TODO: For David to look at
    startProducing("2022-01-27")

//    print("Insert desired date: ")
//    val input = StdIn.readLine()
//    val Date = """^(\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])""".r
//
//    val startDay = input match {
//      case Date(year, month, day) => LocalDate.of(year.toInt, month.toInt, day.toInt).atStartOfDay()
//      case _ => println("Invalid Input, Please follow format YYYY-MM-DD")
//    }

  }

  def startProducing(startDateStr: String, minuteIncrements: Long = 60): Unit = {
    val startDate = strToLocalDate(startDateStr).atStartOfDay()//.plus(2, ChronoUnit.HOURS)
//    println(startDate)
//    println(getPercentThroughDay(startDate))

    val po = ProductOrder.getInitialOrder(startDate)
    println(po)
    println(ProductOrder.toString(po))
    println(ProductOrder.toString(ProductOrder.getSampleOrder()))
    println()

    Stream
      .from(1)
      .flatMap(i => {
        val batchDateTime = startDate.plus(minuteIncrements * i, ChronoUnit.MINUTES)
        val dayPercentage = getPercentThroughDay(batchDateTime)
        val batchSize = GenHelper.getGlobalBatchSize(dayPercentage)
        val countryProbs = GenHelper.getCountryProbabilities(dayPercentage)
        val dayOfWeek = DateHelper.getDayOfWeek(batchDateTime)
        println(batchDateTime)

        (1 to 1)
          .map(_ => ProductOrder.getInitialOrder(batchDateTime, countryProbs))
          .map(p => GenHelper.assignCategory(dayPercentage, 0, p))
      })
      .foreach(p => {
        println(ProductOrder.toString(p))
        Thread.sleep(10000)
      })

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
