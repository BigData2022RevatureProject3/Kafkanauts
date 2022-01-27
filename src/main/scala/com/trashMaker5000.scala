import org.apache.spark.sql.SparkSession
import scala.io.Source
import scala.util.Random
import org.apache.spark
import org.apache.spark.sql.functions.{col, when}
import org.apache.spark.rdd.RDD
import scala.collection.mutable.ArrayBuffer

object trashMaker5000 extends App {

  def makeTrash(file: String, x: Int): Unit = {
    //  Reads data from text file into an ArrayBuffer.
    val aB = new ArrayBuffer[Array[String]]()
    val bufferedSource = scala.io.Source.fromFile(file).getLines.foreach( aB += _.split(","))
    println(aB(1).toList)
    println(aB.size)

    //  Chooses a random element from the list and screws it up.
    val r = new Random()
    for (i <- 1 to x) {
      val randomIndex1 = r.nextInt(aB.size)
      val randomIndex2 = r.nextInt(aB(randomIndex1).size)
      aB(randomIndex1)(randomIndex2) = "CRAP DATA"
    }

//    aB.foreach(x => println(x.toList))
// Still need to append each line to the file. Basically just replace println with an append method.
    aB.foreach(x => println(x.mkString(",")))

  }

//  WORK:
//  makeTrash("taco_master.csv",10)
//  makeTrash("chinese_cities.txt",20)
//  makeTrash("first_name_american_common_male.txt",100)
//  makeTrash("ecommerce_cleaned_teddy.txt",500)
//  DON'T WORK:
//  makeTrash("ecommerce_data.csv",500) Problem: seems to be a problem with the formatting of the CSV itself. Teddy's cleaned version works.
  

}
