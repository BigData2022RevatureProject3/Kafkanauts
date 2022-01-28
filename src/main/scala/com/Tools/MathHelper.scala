package com.Tools

import org.apache.commons.math3.distribution.NormalDistribution

import scala.util.Random

object MathHelper {
  def main(args: Array[String]): Unit = {
    val myGaussian: (Double) => Double = getNormalPDF(1/2, 1)
    myGaussian(1/2)
  }

  // https://www.itl.nist.gov/div898/handbook/eda/section3/eda3661.htm
  def getNormalPDF(mean: Double, stdDev: Double): (Double) => Double = {
    (x: Double) => (Math.exp(-Math.pow(x - mean, 2)) / Math.sqrt(2 * Math.PI)) / (stdDev * Math.sqrt(2 * Math.PI))
  }


  def chooseFromList[T](list: List[T]): T = {
    list(Random.nextInt(list.length))
  }

  def getGaussianFunc(mean: Double, variance: Double): (Double) => Double = {
    (_: Double) => Random.nextGaussian() * variance + mean
  }

  def chooseFromWeightedList[T](list: List[T], probabilities: List[Double]): T = {
    val sum = probabilities.sum
    val normalized = probabilities.map(_ / sum)
    var rand = Random.nextDouble()

    //
    for ((l, p) <- list.zip(normalized)) {
      if (rand < p)
        return l
      else
        rand -= p
    }
    chooseFromList(list)
  }
}
