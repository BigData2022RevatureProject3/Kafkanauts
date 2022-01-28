package com.Producer

import com.Producer.Generators.GenHelper

import java.time.{LocalDate, LocalDateTime, ZoneOffset}
import java.time.temporal.ChronoUnit
import scala.io.StdIn
import scala.util.Random
import com.Tools.DateHelper._
import com.Producer.Generators.GenHelper._
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

  def startProducing(startDateStr: String, minuteIncrements: Long = 60, processDelay: Long = 5000): Unit = {
    val startDate = strToLocalDate(startDateStr).atStartOfDay()

    println(s"Starting Production at ${DateHelper.print(startDate)} with $minuteIncrements minute increments, delayed by $processDelay")
    Stream
      .from(1)
      .map(i => {
        val batchDateTime = startDate.plus(minuteIncrements * i, ChronoUnit.MINUTES)
        val dayPercentage = getPercentThroughDay(batchDateTime)
        var batchSize = GenHelper.getGlobalBatchSize(dayPercentage)
        batchSize = 2
        val countryProbs = GenHelper.getCountryProbabilities(dayPercentage)
        val dayOfWeek = DateHelper.getDayOfWeek(batchDateTime)

        (1 to batchSize)
          .map(_ => ProductOrder.getInitialOrder(batchDateTime, countryProbs))
          .map(p => GenHelper.addCategory(dayPercentage, dayOfWeek, p))
          .map(p => GenHelper.addProduct(dayPercentage, dayOfWeek, p))
          .map(p => GenHelper.addCustomerInfo(dayPercentage, dayOfWeek, p))
          .map(p => GenHelper.addTransactionInfo(dayPercentage, dayOfWeek, p))
          .map(toFinalString).toList
      })
      .foreach(pStrs => {
        println(pStrs)
        // TODO: Send to Producer
        // TODO: Log events
        Thread.sleep(5000)
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
