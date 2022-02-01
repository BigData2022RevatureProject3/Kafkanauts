package com.Tools

import org.apache.spark.sql.DataFrame
import MathHelper._

object FunctionTestbed {
  def main(args: Array[String]): Unit = {
//    testBimodal()
//    testSkew()
//    biSkew()
    testCountry()
  }

  def testCountry(): Unit = {
    val spainStartTime: Double = 14 / 24.0
    val spainEndTime: Double = 17 / 24.0
    val spainMidtime: Double = (spainStartTime + spainEndTime) / 2
    val sVar = 0.1
    val sScale = 0.4

    val spain = getExtraBimodalFunc(spainStartTime, sVar, sScale, spainEndTime, sVar, sScale, spainMidtime, sVar, 0.2)
    val spainShift = shiftTimezone(spain, 6)
    val china = getNormalPDF(0.5, 0.5, 1.2)
    val china2 = getBimodalFunc(0.4, 0.3, 2, 0.6, 0.3, 2)
    val chinaShift = shiftTimezone(china2, 12)
    val us = getNormalPDF(0.5, 0.25)

    val all = addFuncs(spain, us, china2)
    val allShift = addFuncs(spainShift, chinaShift, us)
    functionsToString(24, 0, china2, us, spain, all)
  }

  def testShifting(): Unit = {
    val f = MathHelper.getNormalPDF(0.5, 1, 10)
    functionToDataFrame(f).show()
    functionToDataFrame(shiftTimezone(f, 6)).show()

  }

  def testSkew(): Unit = {
    val f1 = MathHelper.getNormalPDF(0, 1, 10)
    val f2 = MathHelper.getNormalPDF(0, 0.1, 0.2)

    val both = MathHelper.subtractFunc(f1, f2)
    MathHelper.functionToDataFrame(f1, 20, -1.0).show()
    MathHelper.functionToDataFrame(f2, 20, -1.0).show()
    MathHelper.functionToDataFrame(both, 20, -1.0).show()
  }

  def biSkew(): Unit = {
    val start = 14.0 / 24
    val end = 17.0 / 24
    val middle = (start + end) / 2
    val bi = MathHelper.getBimodalFunc(start, 0.1, 0.4, end, 0.1, 0.4)
    val fminus = MathHelper.getNormalPDF(middle, 0.1, 0.2)

    val all = MathHelper.subtractFunc(bi, fminus)
    val biAll = MathHelper.shiftTimezone(all, 6)
    val bimodalPlus = MathHelper.getExtraBimodalFunc(start, 0.1, 0.4, end, 0.1, 0.4, middle, 0.1, 0.2)
    MathHelper.functionsToString(24, 0, all, biAll, bimodalPlus)

  }
  def testBimodal(): Unit = {
    val start = 14.0 / 24
    val end = 17.0 / 24
    val middle = (start + end) / 2
    val one = MathHelper.getNormalPDF(start, 0.1, 0.1)
    val two = MathHelper.getNormalPDF(end, 0.1, 0.1)
    val bi = MathHelper.getBimodalFunc(start, 0.1, 0.1, end, 0.1, 0.1)
    val tri = MathHelper.addFuncs(bi, MathHelper.getNormalPDF(middle, 1, 0.5))
    val fminus = MathHelper.getNormalPDF(middle, 0.1, 0.05)

    val all = MathHelper.subtractFunc(bi, fminus)
//    MathHelper.functionToDataFrame(bi, 24).show(50)
//    MathHelper.functionToDataFrame(fminus, 24).show(50)
//    MathHelper.functionToDataFrame(all, 24).show(50)

    MathHelper.functionsToString(24, 0, one, two, bi, fminus, all)

  }

  def testPDF(): Unit = {
    val n1 = MathHelper.getNormalPDF(0, 0.1)
    val n2 = MathHelper.getNormalPDF(0.7, 0.1)
    val n3 = (x: Double) => x
    val func = (x: Double) => n1(x)
    val func2 = MathHelper.minOfFunctions(n1, n3)

    MathHelper.functionToDataFrame(func).show()
    MathHelper.functionToDataFrame(func2).show()
  }

  def getSampleFunc(): DataFrame = {
    val n1 = MathHelper.getNormalPDF(0.3, 0.1)
    val n2 = MathHelper.getNormalPDF(0.7, 0.1)
    val func = (x: Double) => n1(x) + n2(x)
    MathHelper.functionToDataFrame(func)
  }



}
