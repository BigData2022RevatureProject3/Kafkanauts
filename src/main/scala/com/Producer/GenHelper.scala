package com.Producer

import com.ProductOrder
import com.Tools.MathHelper

import java.time.LocalDateTime

/**
 * This object helps store the rate of production of various goods organized by country, day of week and product category
 */
object GenHelper {
  val countries = List("China", "U.S", "Spain")
  // TODO: Finish and make canonical
  val categories = List("Gas", "Medicine", "Music", "Produce")

  def getGlobalBatchSize(dayPercent: Double): Int = {
    Math.ceil(1 - Math.abs(0.5 - dayPercent) * 50).toInt + 50
  }

  def getCountryProbabilities(dayPercent: Double): List[Double]= {
    // TODO: Change calculation
    List(1402, 329, 47)
  }

  def assignCategory(dayPercent: Double, day: Int, po: ProductOrder): ProductOrder = {
    // TODO: Change to per-country/day/percent choice
    po.product_category = MathHelper.chooseFromList(GenHelper.categories)
    po
  }

}
