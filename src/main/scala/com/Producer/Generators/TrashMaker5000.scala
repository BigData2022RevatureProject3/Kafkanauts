package com.Producer.Generators

import com.ProductOrder

import scala.util.Random

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object TrashMaker5000 {

  def makeTrash(PO: ProductOrder, easterEgg: Option[String] = None): String = {
    /**
     * Purpose: Takes a PO and returns it as a string with random "corruptions."
     * @param PO to be parsed and modified.
     * @param easterEgg is an optional string that has a 5% of being introduced into the PO.
     * @return PO as string, with "corruptions."
     */

    val str = PO.toString.replaceAll("ProductOrder","")
    val str2 = str.slice(1, str.length - 1)
    var aB = new ArrayBuffer[String]()
    val arr = str2.split(",").foreach(aB += _)

    val r = new Random()
    val randomTrash = r.nextInt(21)
    val randomIndex1 = r.nextInt(aB.size - 1)

    randomTrash match {
      case 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 => aB(randomIndex1) = ""
      case 10 | 11 | 12 | 13 | 14 | 15 => aB(randomIndex1) = r.nextString(aB(randomIndex1).length)
      case 16 | 17 | 18 => aB(randomIndex1) = r.nextPrintableChar().toString.concat(r.nextPrintableChar().toString).concat(r.nextPrintableChar().toString).concat(r.nextPrintableChar().toString).concat(r.nextPrintableChar().toString).concat(r.nextPrintableChar().toString)
      case 19 => aB(randomIndex1) = easterEgg.toString.slice(4,easterEgg.toString.length)
      case 20 => aB -= aB(randomIndex1)
      case _ =>
    }

    val str3 = new StringBuilder(aB.mkString("|"))
    val randomTrash2 = r.nextInt(20)
    val randomIndex2 = r.nextInt(str3.length)
    randomTrash2 match {
      case 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11| 12 | 13 | 14 | 15 | 16 | 17 | 18 =>
      case 19 => str3(randomIndex2) = '|'
    }

    return str3.toString()

  }

  def makeTrashes(file: String, x: Int, easterEgg: Option[String] = None): ArrayBuffer[Array[String]] = {

    /**

     * Purpose: Takes a date as an argument and returns the real day of the week on that date.
     *
     * @param file is the name of the CSV or other text file to be parsed.
     * @param x    is the number of modifications (corruptions) to be performed on the data.
=======
     * Purpose: takes a text file, stored it in an ArrayBuffer, and modifies it at random points, creating "corrupted data".
     * @param file is the name of the CSV or other text file to be parsed.
     * @param x is the number of modifications (corruptions) to be performed on the data.
     * @param easterEgg is an optional string that has a 5% of being introduced into the PO.

     * @return An ArrayBuffer containing the lines from the original file with the modifications.
     */

    //  Reads data from text file into an ArrayBuffer.
    val aB = new ArrayBuffer[Array[String]]()
    val bufferedSource = scala.io.Source.fromFile(file).getLines.foreach(aB += _.split(","))

    //  For each number 1 to x, chooses a random element from the file and modifies it.
    val r = new Random()
    for (i <- 1 to x) {
      val randomIndex1 = r.nextInt(aB.size)
      val randomIndex2 = r.nextInt(aB(randomIndex1).size)
      val randomTrash = r.nextInt(21)
      randomTrash match {
        case 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 => aB(randomIndex1)(randomIndex2) = "CRAP DATA"
        case 10 | 11 | 12 | 13 | 14 | 15 => aB(randomIndex1)(randomIndex2) = r.nextString(aB(randomIndex1)(randomIndex2).length)
        case 16 | 17 | 18 | 19 => aB(randomIndex1)(randomIndex2) = r.nextPrintableChar().toString.concat(r.nextPrintableChar().toString).concat(r.nextPrintableChar().toString).concat(r.nextPrintableChar().toString).concat(r.nextPrintableChar().toString).concat(r.nextPrintableChar().toString)
        case 20 => aB(randomIndex1)(randomIndex2) = easterEgg.toString.slice(4, easterEgg.toString.length)
        case _ =>
      }
    }

    aB.foreach(x => println(x.mkString(",")))
    return aB

  }

  def main(args: Array[String]): Unit = {
    makeTrashes("taco_master.csv", 50, Option("This is an Easter Egg. Merry Christmas to Thor."))

  }

}
