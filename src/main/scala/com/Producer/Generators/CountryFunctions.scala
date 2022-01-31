package com.Producer.Generators

import com.Tools.MathHelper

object CountryFunctions {
  def main(args: Array[String]): Unit = {
    compareTotals()
    printWeeklySum()

  }
  def compareTotals(): Unit = {
    val globalScale = 1
    val chinaScale  = 2
    val usScale     = 1
    val spainScale  = 1

    val c = getChineseDaily()
    val u = getUSDaily()
    val s = getSpainDaily()
    val chinaSum = MathHelper.areaUnderCurve(c(0), 1) * chinaScale
    val usSum =    MathHelper.areaUnderCurve(u(0), 1) * usScale
    val spainSum = MathHelper.areaUnderCurve(s(0), 1) * spainScale
    val all = chinaSum + usSum + spainSum
    println(chinaSum, usSum, spainSum, Math.ceil(all) * globalScale)
  }

  def printWeeklySum(): Unit = {
    val globalScale = 5
    val chinaScale  = 1402
    val usScale     = 330
    val spainScale  = 47
    val weekly = (0 to 2).map(getDailySum(_, chinaScale, usScale, spainScale)).sum * globalScale
    println(s"Weekly sum: $weekly")
  }
  def getDailySum(day: Int, chinaScale: Double, usScale: Double, spainScale: Double): Double = {
    val c = getChineseDaily()
    val u = getUSDaily()
    val s = getSpainDaily()
    val chinaSum = MathHelper.areaUnderCurve(c(day), 1) * chinaScale
    val usSum =    MathHelper.areaUnderCurve(u(day), 1) * usScale
    val spainSum = MathHelper.areaUnderCurve(s(day), 1) * spainScale
    chinaSum + usSum + spainSum
  }

//  List(1,2,3,4), List(10, 11, 12, 13, 14)
//  List(1, 10), (2, 12), (3,13),

  // Zips the nth element of each list together
  //  (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300))
  //  => List(List(1, 10, 100), List(2, 20, 200), List(3, 30, 300))
  def combineLists[A](ss:List[A]*): List[List[A]] = {
    val sa = ss.reverse;
    (sa.head.map(List(_)) /: sa.tail)(_.zip(_).map(p=>p._2 :: p._1))
  }

  def getChineseDaily(): List[Double => Double] = {
    combineLists(getChinaCategories():_*).map(dayFuncs => MathHelper.addFuncs(dayFuncs:_*))
  }
  def getUSDaily(): List[Double => Double] = {
    combineLists(getUSCategories():_*).map(dayFuncs => MathHelper.addFuncs(dayFuncs:_*))
  }
  def getSpainDaily(): List[Double => Double] = {
    combineLists(getSpainCategories():_*).map(dayFuncs => MathHelper.addFuncs(dayFuncs:_*))
  }

  def getCategoryProbabilities(country: String, day: Int, dayPercent: Double): List[Double] = {
    val categoriesForWeek =  country match {
      case "China" => getChinaCategories()
      case "United States" => getUSCategories()
      case "Spain" => getSpainCategories()
    }
    categoriesForWeek.map(_(day)(dayPercent))
  }

  def getChinaCategories(): List[List[Double => Double]] = {
    List(chineseGas, chineseGroceries, chineseMedicine)
  }

  def getUSCategories(): List[List[Double => Double]] = {
    List(usGas, usGroceries, usMedicine)
  }

  def getSpainCategories(): List[List[Double => Double]]= {
    List(spainGas, spainGroceries, spainMedicine)
  }

  val chineseEcommerce = List(
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
  )
  val chineseGas = List(
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
  )
  val chineseGroceries = List(
    MathHelper.getConstantFunc(5),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(5),
  )
  val chineseMedicine = List(
    MathHelper.getConstantFunc(5),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(1),
    MathHelper.getConstantFunc(5),
  )
/////////////////////////////////////////////
  val usEcommerce = List(
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
  )
  val usGas = List(
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
  )
  val usGroceries = List(
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
  )
  val usMedicine = List(
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
  )
///////////////////////////////////////////////

  val spainEcommerce = List(
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
  )
  val spainGas = List(
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
  )
  val spainGroceries = List(
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
  )
  val spainMedicine = List(
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
    MathHelper.getNormalPDF(0.5, 0.8),
  )

}
