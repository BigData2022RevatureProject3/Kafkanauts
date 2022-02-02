package com.Tools

import MathHelper.shiftTimezone
import MathHelper._

object CountryFunctions {
  val globalScale: Double = 5
  val chinaScale:  Double = 1047
  val usScale:     Double = 344
  val spainScale:  Double = 47
  val tacoScale:   Double = 2.0

  val weekscale = List(1.0, 0.8)
  def main(args: Array[String]): Unit = {
    compareTotals()
    printWeeklySum()

  }

  def compareTotals(): Unit = {
    val globalScale = 1
    val chinaScale = 2
    val usScale = 1
    val spainScale = 1

    val c = getChineseDaily()
    val u = getUSDaily()
    val s = getSpainDaily()
    val chinaSum = areaUnderCurve(c(0), 1) * chinaScale
    val usSum = areaUnderCurve(u(0), 1) * usScale
    val spainSum = areaUnderCurve(s(0), 1) * spainScale
    val all = chinaSum + usSum + spainSum
    println(chinaSum, usSum, spainSum, Math.ceil(all) * globalScale)
  }

  def printWeeklySum(): Unit = {
    val globalScale = 5
    val chinaScale = 1402
    val usScale = 330
    val spainScale = 47
    val weekly = (0 to 2).map(getDailySum(_, chinaScale, usScale, spainScale)).sum * globalScale
    println(s"Weekly sum: $weekly")
  }

  def getDailySum(day: Int, chinaScale: Double, usScale: Double, spainScale: Double): Double = {
    val c = getChineseDaily()
    val u = getUSDaily()
    val s = getSpainDaily()
    val chinaSum = MathHelper.areaUnderCurve(c(day), 1) * chinaScale
    val usSum = MathHelper.areaUnderCurve(u(day), 1) * usScale
    val spainSum = MathHelper.areaUnderCurve(s(day), 1) * spainScale
    chinaSum + usSum + spainSum
  }

  //  List(1,2,3,4), List(10, 11, 12, 13, 14)
  //  List(1, 10), (2, 12), (3,13),

  // Zips the nth element of each list together
  //  (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300))
  //  => List(List(1, 10, 100), List(2, 20, 200), List(3, 30, 300))
  def combineLists[A](ss: List[A]*): List[List[A]] = {
    val sa = ss.reverse;
    (sa.head.map(List(_)) /: sa.tail) (_.zip(_).map(p => p._2 :: p._1))
  }

  def getChineseDaily(): List[Double => Double] = {
    combineLists(getChinaCategories(): _*).map(dayFuncs => addFuncs(dayFuncs: _*))
  }

  def getUSDaily(): List[Double => Double] = {
    combineLists(getUSCategories(): _*).map(dayFuncs => addFuncs(dayFuncs: _*))
  }

  def getSpainDaily(): List[Double => Double] = {
    combineLists(getSpainCategories(): _*).map(dayFuncs => addFuncs(dayFuncs: _*))
  }

  def getCategoryProbabilities(country: String, day: Int, dayPercent: Double): List[Double] = {
    val categoriesForWeek = country match {
      case "China" => getChinaCategories()
      case "United States" => getUSCategories()
      case "Spain" => getSpainCategories()
    }
    categoriesForWeek.map(_ (day)(dayPercent))
  }

  def getChinaCategories(): List[List[Double => Double]] = {
    List(chineseEcommerce, chineseGas, chineseGroceries, chineseMedicine)
      .map(_.map(shiftTimezone(_, chinaTimeDiff)))
  }

  def getUSCategories(): List[List[Double => Double]] = {
    List(usEcommerce, usGas, usGroceries, usMedicine)
  }

  def getSpainCategories(): List[List[Double => Double]] = {
    List(spainEcommerce, spainGas, spainGroceries, spainMedicine)
      .map(_.map(shiftTimezone(_, spainTimeDiff)))
  }

  //////////////////////////////////////////
  val chinaTimeDiff = 12
  val chinaStart = 12 - 4
  val chinaEnd = 12 + 4
  val chineseF = getNormalPDF(0, 0.1, 0.4)
  val chinaFScale = 0.8
//  val sVar = 0.1
//  val sScale = 0.6
//  val sMinusScale = 0.2

  val chineseEcommerce = List(
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
  )
  val chineseGas = List(
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
  )
  val chineseGroceries = List(
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
  )
  val chineseMedicine = List(
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
  )
  val chineseMusic = List(
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
    getQuadModal(chineseF, chinaStart, chinaEnd, chinaFScale),
  )
  /////////////////////////////////////////////
  val usVar = 0.25
  val usEcommerce = List(
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
  )
  val usGas = List(
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
  )
  val usGroceries = List(
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
  )
  val usMedicine = List(
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
  )

  val usMusic = List(
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
    getNormalPDF(0.5, usVar),
  )
  ///////////////////////////////////////////////
  val spainTimeDiff = 6
  val spainStartTime: Double = 14 / 24.0
  val spainEndTime: Double = 17 / 24.0
  val spainMidtime: Double = (spainStartTime + spainEndTime) / 2
  val sVar = 0.1
  val sScale = 0.6
  val sMinusScale = 0.2

  val spainEcommerce = List(
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
  )
  val spainGas = List(
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
  )
  val spainGroceries = List(
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
  )
  val spainMedicine = List(
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
  )

  val spainMusic = List(
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
    getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, sMinusScale),
  )

}
