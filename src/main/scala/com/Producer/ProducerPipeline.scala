package com.Producer

import com.Producer.Generators.{CountryFunctions, GenHelper}
import java.time.temporal.ChronoUnit
import com.Tools.DateHelper._
import com.Producer.Generators.GenHelper._
import com.ProductOrder
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

  }

  def startProducing(startDateStr: String, minuteIncrements: Long = 60, processDelay: Long = 5000): Unit = {
    val startDate = strToLocalDate(startDateStr).atStartOfDay()

    println(s"Starting Production at ${DateHelper.print(startDate)} with $minuteIncrements minute increments, delayed by $processDelay")
    Stream
      .from(1)
      .map(i => {
        val batchDateTime = startDate.plus(minuteIncrements * i, ChronoUnit.MINUTES)
        val dayPercentage = getPercentThroughDay(batchDateTime)
        val dayOfWeek: Int = DateHelper.getDayOfWeek(batchDateTime)
        val countryProbs = GenHelper.getCountryProbabilities(dayPercentage, dayOfWeek)
        var batchSize: Int = Math.ceil(countryProbs.sum * globalScale).toInt
        batchSize = 2
        val chinaCats = CountryFunctions.getCategoryProbabilities("China", dayOfWeek, dayPercentage)
        val usCats    = CountryFunctions.getCategoryProbabilities("United States", dayOfWeek, dayPercentage)
        val spainCats = CountryFunctions.getCategoryProbabilities("Spain", dayOfWeek, dayPercentage)

        (1 to batchSize)
          .map(_ => ProductOrder.getInitialOrder(batchDateTime, countryProbs))
          .map(p => GenHelper.addCategory(p, chinaCats, usCats, spainCats))
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
  }
}
