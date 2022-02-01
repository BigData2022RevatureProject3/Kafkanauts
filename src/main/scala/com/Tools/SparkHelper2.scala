package com.Tools

import org.apache.spark.sql.SparkSession

object SparkHelper2 {

  val spark: SparkSession = {
    suppressLogs(List("org", "akka", "org.apache.spark", "org.spark-project"))
    System.setProperty("hadoop.home.dir", "C:\\hadoop")
    val spark = SparkSession
      .builder
      .appName("Spark Pipeline")
      .config("spark.master", "local")
      .enableHiveSupport()
      .getOrCreate()
    spark.conf.set("hive.exec.dynamic.partition.mode", "nonstrict")
    spark.sparkContext.setLogLevel("ERROR")
    spark
  }


  def suppressLogs(params: List[String]): Unit = {
    // Levels: all, debug, error, fatal, info, off, trace, trace_int, warn
    import org.apache.log4j.{Level, Logger}
    params.foreach(Logger.getLogger(_).setLevel(Level.OFF))
  }
  def main2(args:Array[String]):Unit = {
    val testRDD = spark.sparkContext.parallelize(Seq((1, 2, 3), (4, 5, 6), (7, 8, 9)))
    val testDF = spark.createDataFrame(testRDD)
    spark.sql("show databases").show()
    spark.sql("show tables").show()
    spark.sql("create table if not exists test_table(col1 int, col2 int, col3 int)")
    spark.sql("show tables").show()
    println(testDF.schema)
    testDF.write.mode("overwrite").saveAsTable("test_table")
    spark
  }
  def main(args:Array[String]):Unit = {
    System.setProperty("hadoop.home.dir", "C:\\hadoop")
    val spark = SparkSession
      .builder
      .appName("hello hive")
      .config("spark.master", "local")
      .enableHiveSupport()
      .getOrCreate()
    println("created spark session")
    //spark.sparkContext.setLogLevel("ERROR")
    spark.sql("drop table if exists newone")
    spark.sql("create table IF NOT EXISTS newtwo(id Int,name String) row format delimited fields terminated by ','")
    val testRDD = spark.sparkContext.parallelize(Seq((1, 2, 3), (4, 5, 6), (7, 8, 9)))
    val testDF = spark.createDataFrame(testRDD)
    spark.sql("show databases").show()
    spark.sql("show tables").show()
    //spark.sql("LOAD DATA LOCAL INPATH 'kv1.txt' INTO TABLE newone")
    spark.sql("SELECT * FROM newone").show()
    spark.close()
  }
}
