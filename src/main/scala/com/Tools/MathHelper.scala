package com.Tools

import org.apache.spark.sql.DataFrame
import scala.util.Random

object MathHelper {
  def main(args: Array[String]): Unit = {
    val myGaussian: (Double) => Double = getNormalPDF(1/2, 1)
    myGaussian(1/2)
  }

  def areaUnderCurve(function: Double => Double, minutesPerSample: Int = 15): Double = {
    val step = minutesPerSample/1440.0
    (0.0 to 1.0 by step).map(function(_)).sum * step
  }



  def addFuncs(functions: (Double) => Double*): Double => Double = {
    functions.reduce((a, b) => (x: Double) => a(x) + b(x))
  }

  def scaleFunc(function: Double => Double, scale: Double): Double => Double = {
    (x: Double) => function(x) * scale
  }

  def getBimodalFunc(mean1: Double, var1: Double, scale1: Double,
                     mean2: Double, var2: Double, scale2: Double): Double => Double = {
    x: Double => getNormalPDF(mean1, var1)(x) * scale1 + getNormalPDF(mean2, var2)(x) * scale2
  }

  def minOfFunctions(functions: (Double) => Double*): Double => Double = {
    functions.reduce((a, b) => (x: Double) => Math.min(a(x), b(x)))
  }

  def minOfFunctions(functions: Array[(Double) => Double]): Double => Double = {
    functions.reduce((a, b) => (x: Double) => Math.min(a(x), b(x)))
  }

  def maxOfFunctions(functions: (Double) => Double*): Double => Double = {
    functions.reduce((a, b) => (x: Double) => Math.max(a(x), b(x)))
  }

  // https://www.itl.nist.gov/div898/handbook/eda/section3/eda3661.htm
  def getNormalPDF(mean: Double, stdDev: Double): Double => Double = {
    (x: Double) => (Math.exp(-Math.pow(x - mean, 2)) / Math.sqrt(2 * Math.PI)) / (stdDev * Math.sqrt(2 * Math.PI))
  }

  def getLinearFunc(slope: Double, height: Double): Double => Double = {
    (x: Double) => slope * x + height
  }

  def getConstantFunc(height: Double): Double => Double = {
    (x: Double) => height
  }
  def functionToDataFrame(func: (Double) => Double, samples: Int = 10, initialX: Double = 0.0): DataFrame = {
    val spark = SparkHelper.spark
    import spark.implicits._

    val step = (1.0 - initialX) / samples
    val x = (initialX to 1.0 by step)
    val y = x.map(func)
    spark.sparkContext.parallelize(x.zip(y)).toDF()
  }


  def chooseFromList[T](list: List[T]): T = {
    list(Random.nextInt(list.length))
  }

  def getGaussianFunc(mean: Double, variance: Double): Double => Double = {
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
