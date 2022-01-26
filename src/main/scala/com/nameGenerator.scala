package com
import scala.collection.mutable.ListBuffer
import scala.io.Source
class nameGenerator{

  def genAmericanName():String ={
    val firstFile = "data/first_name_american_common_female.txt"
    val lastFile = "data/last_name_american_common.txt"
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(firstFile).getLines) {
      val name = line.toLowerCase().split(",")
      firstNameList += name(1).capitalize}
    for (line <- Source.fromFile(lastFile).getLines) {
      val name = line.toLowerCase().split(",")
      lastNameList += name(1).capitalize}
    val ran = scala.util.Random
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName + " " + lName
    name
  }

  def genChineseName():String={
    val firstFile = "data/first_name_chinese(given names).txt"
    val lastFile = "data/last_name_chinese(family name).txt"
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(firstFile).getLines) {firstNameList += line}
    for (line <- Source.fromFile(lastFile).getLines) {lastNameList += line}
    val ran = scala.util.Random
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName + lName
    name
  }

  def genEuropeanName():String={
    val firstFile = "data/first_name_european.txt"
    val lastFile = "data/last_name_american_common.txt"
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(firstFile).getLines) {firstNameList += line}
    for (line <- Source.fromFile(lastFile).getLines) {
      val name = line.toLowerCase().split(",")
      lastNameList += name(1).capitalize}
    val ran = scala.util.Random
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName +" " + lName
    name
  }

}
