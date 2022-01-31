package com.Producer.Generators

import com.Tools.MathHelper

import scala.collection.immutable.Nil.:::

object CountryFunctions {
  def main(args: Array[String]): Unit = {
//    println("global total length: ", getGlobalTotals().length)

//    val mg = combineLists(usGas, spainGas).map(funcs => MathHelper.addFuncs(funcs:_*))
//    MathHelper.functionToDataFrame(mg(0)).show()
//    MathHelper.functionToDataFrame(mg(1)).show()
//    val t = getGlobalTotals()
//    println("total length", t.length)
//    MathHelper.functionToDataFrame(t(0)).show()
//    MathHelper.functionToDataFrame(t(1)).show()
//    MathHelper.functionToDataFrame(t(2)).show()

    val c = getChineseDaily()
    MathHelper.functionToDataFrame(c(0)).show()
    MathHelper.functionToDataFrame(c(1)).show()
    MathHelper.functionToDataFrame(c(2)).show()

//    println(combineLists(List(List(1, 2, 3), List(10, 20, 30), List(100, 200, 300)):_*))

  }

  // Zips the nth element of each list together
  //  (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300))
  //  => List(List(1, 10, 100), List(2, 20, 200), List(3, 30, 300))
  def combineLists[A](ss:List[A]*): List[List[A]] = {
    val sa = ss.reverse;
    (sa.head.map(List(_)) /: sa.tail)(_.zip(_).map(p=>p._2 :: p._1))
  }

  def getGlobalTotals(): List[Double => Double] = {
    val allCategories = getCountryFunctions()
    combineLists(allCategories:_*).map(dayFuncs => MathHelper.addFuncs(dayFuncs:_*))
  }



  def getChineseDaily(): List[Double => Double] = {
    combineLists(getChinaFunctions():_*).map(dayFuncs => MathHelper.addFuncs(dayFuncs:_*))
  }
  def getUSDaily(): List[Double => Double] = {
    combineLists(getUSFunctions():_*).map(dayFuncs => MathHelper.addFuncs(dayFuncs:_*))
  }
  def getSpainDaily(): List[Double => Double] = {
    combineLists(getSpainFunctions():_*).map(dayFuncs => MathHelper.addFuncs(dayFuncs:_*))
  }





  def test(some:String*){}

  def call () {
    val some = Array("asd", "zxc")
    test(some: _*)
  }

  def getCountryFunctions():  List[List[Double => Double]]= {
    List(getChinaFunctions(), getUSFunctions(), getSpainFunctions()).reduce(_ ++ _)
  }

  def getChinaFunctions(): List[List[Double => Double]] = {
    List(chineseGas, chineseMedicine)
  }

  def getUSFunctions(): List[List[Double => Double]] = {
    List(usGas, usMedicine)
  }

  def getSpainFunctions(): List[List[Double => Double]]= {
    List(spainGas, spainMedicine)
  }

  val chineseGas = List(
    MathHelper.getConstantFunc(100),
    MathHelper.getConstantFunc(200),
    MathHelper.getConstantFunc(300),
  )
  val chineseMedicine = List(
    MathHelper.getConstantFunc(0.1),
    MathHelper.getConstantFunc(0.2),
    MathHelper.getConstantFunc(0.3),
  )

  val usGas = List(
    MathHelper.getConstantFunc(30),
    MathHelper.getConstantFunc(40),
    MathHelper.getConstantFunc(50),
  )
  val usMedicine = List(
    MathHelper.getConstantFunc(0.03),
    MathHelper.getConstantFunc(0.04),
    MathHelper.getConstantFunc(0.05),
  )

  val spainGas = List(
    MathHelper.getConstantFunc(5),
    MathHelper.getConstantFunc(6),
    MathHelper.getConstantFunc(7),
  )
  val spainMedicine = List(
    MathHelper.getConstantFunc(0.005),
    MathHelper.getConstantFunc(0.006),
    MathHelper.getConstantFunc(0.007),
  )

}
