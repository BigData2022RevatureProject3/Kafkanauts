package com.Tools

import java.text.{DateFormat, SimpleDateFormat}
import java.util.Date
import scala.io.AnsiColor.{GREEN, RED, RESET}


object DateTimeConverter {
  /**
   * This method is used to convert time in milliseconds to DateFormat[yyyy-MM-dd HH:mm:ss] or vice versa.
   * @param dateTime Must be in DateFormat[yyyy-MM-dd HH:mm:ss] or can be parsed into a Long[milliseconds]
   * @return A string in the form of DateFormat[yyyy-MM-dd HH:mm:ss] or Long[milliseconds]
   */
  def getDateElseMS(dateTime: Any): String = {
    val dateFormat: DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    //Regex of the dateFormat
    val dateRegex = """^(\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) (?:(?:([01]?\d|2[0-3]):)?([0-5]?\d):)?([0-5]?\d))$""".r
    dateTime match {
      case x: String if x.forall(Character.isDigit) => s"${dateFormat.format(new Date(x.toLong))}"
      case x: String if dateRegex.pattern.matcher(x).matches() => s"${dateFormat.parse(x).getTime}"
      case x: Long => s"${new Date(x).getTime}"
      case _ => "DateManipulator.date(), invalid input"
    }
  }

  //Main method used to test Proof of Concept
  def main(args: Array[String]): Unit = {
    val pass1: String = "2021-02-19 11:12:12"
    val pass2: String = "1613751132000"
    val pass3: Long = 1613751132000L
    val fail1: String = "2021-02-19"
    val fail2: String = "11:12:12"
    val fail3: String = "11:12:12 2021-02-19"
    val fail4: Int = 123456789


    println(s"${GREEN}Should pass 1, x in yyyy-MM-dd HH:mm:ss = $RESET" + DateTimeConverter.getDateElseMS(pass1))
    println(s"${GREEN}Should pass 2, x parse to Long from String = $RESET" + DateTimeConverter.getDateElseMS(pass2))
    println(s"${GREEN}Should pass 3, x asInstanceOf[Long] = $RESET" + DateTimeConverter.getDateElseMS(pass3))
    println(s"${RED}Should fail 1, x in yyyy-MM-dd = $RESET" + DateTimeConverter.getDateElseMS(fail1))
    println(s"${RED}Should fail 2, x in HH:mm:ss = $RESET" + DateTimeConverter.getDateElseMS(fail2))
    println(s"${RED}Should fail 3, x in HH:mm:ss yyyy-MM-dd = $RESET" + DateTimeConverter.getDateElseMS(fail3))
    println(s"${RED}Should fail 4, x asInstanceOf[Int] = $RESET" + DateTimeConverter.getDateElseMS(fail4))
  }
}

