package com
import scala.collection.mutable.ListBuffer
import scala.io.Source

class cityCountryGenerator{
  private final val usCityFile = "data/american_cities.txt"
  private final val cnCityFile = "data/chinese_cities.txt"
  private final val spCityFile = "data/spain_cities.txt"
  private final val countries = new ListBuffer[String]
  private final val ran = scala.util.Random
  for (line <- Source.fromFile("data/countries.txt").getLines) {
    countries += line }

  protected def genUS():String={
    val cities = new ListBuffer[String]
    val country = countries(0)
    for (line <- Source.fromFile(usCityFile).getLines) {
      cities += line }
    val result = cities(ran.nextInt(cities.length+1)) +","+ country
    result
  }

  protected def genCN():String={
    val cities = new ListBuffer[String]
    val country = countries(1)
    for (line <- Source.fromFile(cnCityFile).getLines) {
      cities += line }
    val result = cities(ran.nextInt(cities.length+1)) +","+ country
    result
  }

  protected def genSP():String={
    val cities = new ListBuffer[String]
    val country = countries(2)
    for (line <- Source.fromFile(spCityFile).getLines) {
      cities += line }
    val result = cities(ran.nextInt(cities.length)+1) +","+ country
    result
  }

}
