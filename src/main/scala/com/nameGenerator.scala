package com
import scala.collection.mutable.ListBuffer
import scala.io.Source
class nameGenerator{
  
  private final val fUsFile = "data/first_name_american_common_female.txt"
  private final val lUsFile = "data/last_name_american_common.txt"
  private final val fCnFile = "data/first_name_chinese(given names).txt"
  private final val lCnFile = "data/last_name_chinese(family name).txt"
  private final val fEuFile = "data/first_name_european.txt"
  private final val ran = scala.util.Random

  protected def genAmericanName():String ={
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(fUsFile).getLines) {
      val name = line.toLowerCase().split(",")
      firstNameList += name(1).capitalize}
    for (line <- Source.fromFile(lUsFile).getLines) {
      val name = line.toLowerCase().split(",")
      lastNameList += name(1).capitalize}
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName + " " + lName
    name
  }

  protected def genChineseName():String={
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(fCnFile).getLines) {firstNameList += line}
    for (line <- Source.fromFile(lCnFile).getLines) {lastNameList += line}
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName + lName
    name
  }

  protected def genEuropeanName():String={
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(fEuFile).getLines) {firstNameList += line}
    for (line <- Source.fromFile(lUsFile).getLines) {
      val name = line.toLowerCase().split(",")
      lastNameList += name(1).capitalize}
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName +" " + lName
    name
  }
 println(genEuropeanName())
}
