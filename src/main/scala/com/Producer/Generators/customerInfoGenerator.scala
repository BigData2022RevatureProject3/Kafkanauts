package com.Producer.Generators

import com.ProductOrder

import scala.collection.mutable.ListBuffer
import scala.io.Source

object customerInfoGenerator {

  //Each function return a string separated by commas
  //In the follow respective order:
  //orderId, Customer ID, Customer Name, Customer City, Customer Country

  private final val fUsFile = "data/first_name_american_common_female.txt"
  private final val lUsFile = "data/last_name_american_common.txt"
  private final val fCnFile = "data/first_name_chinese(given names).txt"
  private final val lCnFile = "data/last_name_chinese(family name).txt"
  private final val fEuFile = "data/first_name_european.txt"
  private final val usCityFile = "data/american_cities.txt"
  private final val cnCityFile = "data/chinese_cities.txt"
  private final val spCityFile = "data/spain_cities.txt"
  private final val countries = new ListBuffer[String]
  private final val ran = scala.util.Random
  for (line <- Source.fromFile("data/countries.txt").getLines) {
    countries += line
  }
<<<<<<< HEAD

  def generate(po: ProductOrder): ProductOrder = {
    po.country match {
      case "China" => genCNCustomer(po)
    }
  }

  def genUSCustomer(po:ProductOrder): ProductOrder = {
=======
  def generateCustomer(po:ProductOrder):ProductOrder={
    if(po.product_category == "gas"){
      //Gas to be added
      po
    }else{
      po.country match{
        case "United States" => genUSCustomer(po)
        case "China" => genCNCustomer(po)
        case "Spain" => genEUCustomer(po)
      }
    }
  }
  private def genUSCustomer(po:ProductOrder): ProductOrder = {
>>>>>>> 63594d42483fb7891f5f55853fafe273cd0d308f
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(fUsFile).getLines) {
      val name = line.toLowerCase().split(",")
      firstNameList += name(1).capitalize
    }
    for (line <- Source.fromFile(lUsFile).getLines) {
      val name = line.toLowerCase().split(",")
      lastNameList += name(1).capitalize
    }
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName + " " + lName
    val cities = new ListBuffer[String]
    val country = countries(0)
    for (line <- Source.fromFile(usCityFile).getLines) {
      cities += line
    }
    po.customer_id = Math.abs(name.hashCode())
    po.customer_name = name
    po.city = cities(ran.nextInt(cities.length))
    po.country = country
    po
  }

  private def genCNCustomer(po:ProductOrder): ProductOrder = {
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(fCnFile).getLines) {
      firstNameList += line
    }
    for (line <- Source.fromFile(lCnFile).getLines) {
      lastNameList += line
    }
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName + lName
    val cities = new ListBuffer[String]
    val country = countries(1)
    for (line <- Source.fromFile(cnCityFile).getLines) {
      cities += line
    }
    po.customer_id = Math.abs(name.hashCode())
    po.customer_name = name
    po.city = cities(ran.nextInt(cities.length))
    po.country = country
    po
  }

  private def genEUCustomer(po:ProductOrder): ProductOrder = {
    val firstNameList = new ListBuffer[String]
    val lastNameList = new ListBuffer[String]
    for (line <- Source.fromFile(fEuFile).getLines) {
      firstNameList += line
    }
    for (line <- Source.fromFile(lUsFile).getLines) {
      val name = line.toLowerCase().split(",")
      lastNameList += name(1).capitalize
    }
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName + " " + lName
    val cities = new ListBuffer[String]
    val country = countries(2)
    for (line <- Source.fromFile(spCityFile).getLines) {
      cities += line
    }
    po.customer_id = Math.abs(name.hashCode())
    po.customer_name = name
    po.city = cities(ran.nextInt(cities.length))
    po.country = country
    po
  }
}
