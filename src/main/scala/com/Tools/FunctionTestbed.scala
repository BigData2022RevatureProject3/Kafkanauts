package com.Tools

import org.apache.spark.sql.DataFrame

object FunctionTestbed {
  def main(args: Array[String]): Unit = {
    val normal = MathHelper.getNormalPDF(0.5, 0.2)
    functionToDataFrame(normal).show()
    
  }

  def functionToDataFrame(func: (Double) => Double, samples: Int = 10, initialX: Double = 0.0): DataFrame = {
    val spark = SparkHelper.spark
    import spark.implicits._

    var step = (1.0 - initialX) / samples
    val x = (initialX to 1.0 by step)
    val y = x.map(func)
    spark.sparkContext.parallelize(x.zip(y)).toDF()
  }

}
