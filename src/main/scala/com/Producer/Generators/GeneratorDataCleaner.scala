package com.Producer.Generators

object GeneratorDataCleaner {

  def isValid(relativeFilePath: String): Boolean = {
    val filePath = os.pwd / relativeFilePath
    if (os.isFile(filePath)) {
      os
        .read
        .lines(filePath)
        .toList
        .map(_.contains("|"))
        .contains("false")
    }
    false
  }


  def proofOfConcept(): Unit = {
    //os.read.lines(os.pwd / "data" / "price.txt").take(5).foreach(println)
    val a = os
      .read
      .lines(os.pwd / "clean_data" / "dates_and_weekdays_starting_1970.csv")
      .take(5)
      .map(l => l.split(","))
      .map{ case Array(a,b) => (a,b)}
      .toMap[String,String]
      .foreach(println)
  }

  def main(args: Array[String]): Unit = {
    println(isValid("medicine/medicine_2007.txt"))
  }
}
