package com.Producer.Generators

import com.ProductOrder
import com.Tools.MathHelper

import scala.util.Random

object ECommGenerator {

  private final val r = new Random()
//  private final val eCommerceFile = "clean_data/ecommerce_cleaned_teddy.txt"
//  private final val quantities = List(1,2,3,4,5)
  var baseQuant = 2
  val eCommerceList = os
    .read
    .lines(os.pwd / "clean_data" / "ecommerce_cleaned_teddy.txt" )
    .drop(1)
    .map(line => line.split(","))
    .map(splitArray => {
      val name = splitArray(3)
      val price = splitArray(2).toDouble
      val site = splitArray(1)
      (name, price, site)
    })
    .toList

  def genECommOrder(po:ProductOrder): ProductOrder = {
    val (name, price, site) = MathHelper.chooseFromList(eCommerceList)
//    val quantity = MathHelper.chooseFromWeightedList(quantities,List(0.))
    val quant = price match {
      case price < 10 => MathHelper.chooseFromWeightedList(
        List(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30),
        List(0.125,	0.0555555556,	0.0357142857,	0.0263157895,	0.0208333333,	0.0172413793,	0.0147058824,	0.0128205128,0.0113636364,0.0102040816,0.0092592593,0.0084745763,0.0078125,0.0072463768,0.0067567568,0.0063291139,0.005952381,0.0056179775,0.0053191489,0.0050505051,0.0048076923,0.004587156,0.0043859649,0.0042016807,0.0040322581,0.003875969,0.0037313433,0.0035971223,0.0034722222,0.0033557047)
      )
      case price < 20 =>
      case price < 50 =>
      case price < 100 =>
      case _ =>
    }
    val quantity = r.nextInt(2) + 1

    return po
  }

    def main(args: Array[String]): Unit = {
    println("go fuck yourself")
  }

}
