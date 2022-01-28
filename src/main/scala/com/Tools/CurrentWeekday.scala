package com.Tools

import scala.io.Source

object CurrentWeekday {

  def getWeekday(date: String): String = {

    /**
     * Purpose: Takes a date as an argument and returns the real day of the week on that date.
     * @param date must be in yyyy/MM/dd HH:mm:ss.
     * @return Dat of the week (String).
     */

    /**
     * Takes lines from a CSV holding dates
     * and the days of the week from 1970 onward
     * and stores these pairs in a map.
     */
    var datesAndWeekdays = collection.mutable.Map[String,String]()
    val file = "dates_and_weekdays_starting_1970.csv"
    val bufferedSource = scala.io.Source.fromFile(file).getLines()
    while (bufferedSource.hasNext) {
      val str1 = bufferedSource.next()
      val str2 = str1.split(",")(0)
      val str3 = str1.split(",")(1)
      datesAndWeekdays += (str2 -> str3)
    }

//  Returns the day of the week for the given date.
    val justTheDate = date.split(" ")(0)
    return datesAndWeekdays(justTheDate)

  }

  def main (args: Array[String]): Unit = {
    getWeekday("2022/01/27 18:58:42")
  }

}
