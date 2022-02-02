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
  val useEC2 = false
  val useKafka = false

  def main(args: Array[String]): Unit = {
    // get date, increments, delay, bool for produce/write from args?
    val increment = 15
    val days = 1440.0/increment * 7
    startProducing("2022-01-31", increment, 1, 5*288*7*2)

  }

  def startProducing(startDateStr: String, minuteIncrements: Long = 12*60, processDelay: Long = 5000, maxIterations: Int = Int.MaxValue): Unit = {
    val startDate = strToLocalDate(startDateStr).atStartOfDay()

    println(s"Starting Production at ${DateHelper.print(startDate)} with $minuteIncrements minute increments, delayed by $processDelay")
    (1 to maxIterations)
      .map(i => {
        val batchDateTime = startDate.plus(minuteIncrements * i, ChronoUnit.MINUTES)
        val dayPercentage = getPercentThroughDay(batchDateTime)
        val dayOfWeek: Int = DateHelper.getDayOfWeek(batchDateTime)
        val countryProbs = GenHelper.getCountryProbabilities(dayPercentage, dayOfWeek)
        val batchSize: Int = Math.ceil(countryProbs.sum * globalScale).toInt
        println(batchSize)
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

//        batchSize
      })
      .foreach(pStrs => {
        if (useKafka)
          pStrs.foreach(Producer.send)
        else
          pStrs.foreach(println)
        Thread.sleep(processDelay)
      })
  }
}
