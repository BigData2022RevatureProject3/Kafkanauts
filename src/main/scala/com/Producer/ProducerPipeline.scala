package com.Producer

import java.time.temporal.ChronoUnit
import com.Tools.DateHelper._
import GenHelper._
import com.ProductOrder
import com.Tools.CountryFunctions.globalScale
import com.Tools.{CountryFunctions, DateHelper}

/**
 * This object deals with creating a burst of orders for blocks of time,
 * assigning a country of origin and product category for each order
 * and fills out the rest of the info pseudo-randomly based off that starting info
 * It then sends it to a Kafka topic
 */
object ProducerPipeline {
  val debugMode = true

  def main(args: Array[String]): Unit = {
    // get date, increments, delay, bool for produce/write from args?
    startProducing("2022-01-27")

  }

  def startProducing(startDateStr: String, minuteIncrements: Long = 12*60, processDelay: Long = 5000): Unit = {
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
        batchSize = 1
        val chinaCats = CountryFunctions.getCategoryProbabilities("China", dayOfWeek, dayPercentage)
        val usCats    = CountryFunctions.getCategoryProbabilities("United States", dayOfWeek, dayPercentage)
        val spainCats = CountryFunctions.getCategoryProbabilities("Spain", dayOfWeek, dayPercentage)

        (1 to batchSize)
          .map(_ => ProductOrder.getInitialOptOrder(batchDateTime, countryProbs))
          .map(p => GenHelper.addWebsiteInfo(p))
          .map(p => GenHelper.addCategory(p, chinaCats, usCats, spainCats))
          .map(p => GenHelper.addProduct(p, dayPercentage, dayOfWeek))
          .map(p => GenHelper.addCustomerInfo(p, dayPercentage, dayOfWeek))
          .map(p => GenHelper.addTransactionInfo(p))
          .map(toFinalString).toList
      })
      .foreach(pStrs => {
        println(pStrs)
        // TODO: Send to Producer
        // TODO: Log events
        //Producer.send(pStrs.toString())  //Uncomment this line when run as producer or consumer
        Thread.sleep(1000)
      })
  }
}
