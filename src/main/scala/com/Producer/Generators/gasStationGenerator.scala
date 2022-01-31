package com.Producer.Generators

import scala.collection.mutable.ListBuffer
import scala.io.Source

object gasStationGenerator {
  private final val ran = scala.util.Random
  private final val gasStations = "data/uniqueGasStations.csv"

  private def genGasStations(): String = {
    val gasNames = new ListBuffer[String]
    for (line <- Source.fromFile(gasStations).getLines) {
      gasNames += line
    }
    gasNames(ran.nextInt(gasNames.length))
  }

}
