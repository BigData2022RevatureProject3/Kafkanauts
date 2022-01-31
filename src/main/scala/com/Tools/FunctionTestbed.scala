package com.Tools

import org.apache.spark.sql.DataFrame

object FunctionTestbed {
  def main(args: Array[String]): Unit = {
    val n1 = MathHelper.getNormalPDF(0.3, 0.1)
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
