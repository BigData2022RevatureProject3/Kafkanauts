package com.Producer.Generators

import os.RelPath

import scala.collection.mutable

object GeneratorDataCleaner {

  private def isValid(filePath: os.pwd.ThisType): Boolean = {
    //If the file exists, check if it is pipe['|'] delimited
    if (os.isFile(filePath)) {
      return os
        .read
        .lines(filePath)
        .toList
        .forall(_.contains("|"))
    }
    false
  }

  def validatedData(relativeFilePath: String): Unit = {
    val filePath = os.pwd / RelPath(relativeFilePath)
    if(!isValid(filePath)) {
      val validatedText = os
        .read
        .lines(filePath)
        .map(_.replace(",", "|")).mkString("\n")
      os.write.over(filePath, validatedText)
    }
  }


  def proofOfConcept(): Unit = {
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
    validatedData("clean_data/ecommerce_cleaned_teddy.txt")
  }
}
