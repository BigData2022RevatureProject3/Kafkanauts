package com

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.SparkSession
import org.apache.spark

import java.util.Properties
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Random

object SparkKafkaStreamTestProducer extends App {
  val spark = SparkSession
    .builder
    .appName("SparkKafkaStreamingExample")
    .config("spark.master", "local")
    .getOrCreate()

  import spark.implicits._

  val firstFile = "data/first_name_american_common_female.txt"
  val lastFile = "data/last_name_american_common.txt"
  val firstNameList = new ListBuffer[String]
  val lastNameList = new ListBuffer[String]
  for (line <- Source.fromFile(firstFile).getLines) {
    val name = line.toLowerCase().split(",")
    firstNameList += name(1).capitalize
  }
  for (line <- Source.fromFile(lastFile).getLines) {
    val name = line.toLowerCase().split(",")
    lastNameList += name(1).capitalize
  }

  val ran = new Random(100)
  def genName(random_generator:Random):String ={
    //val ran = scala.util.Random
    val fName = firstNameList(ran.nextInt(firstNameList.length))
    val lName = lastNameList(ran.nextInt(lastNameList.length))
    val name = fName + " " + lName
    name
  }

  val groceryFile = "data/grocery_data.txt"
  val groceryList = new ListBuffer[String]


  //println(genName())

  val col1 = spark.sparkContext.parallelize(List((1,2,3), (4,5,6), (7,8,9))).toDF()
  val col2 = spark.sparkContext.parallelize(List((genName(ran) + ",5.89"))).toDF()
  //val col3 = spark.sparkContext.parallelize(List((genName(ran) + "5.89"), (genName(ran) + "5.89")))

  val props:Properties = new Properties()
  props.put("bootstrap.servers","[::1]:9092")
  props.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("acks","all")
  val producer = new KafkaProducer[String, String](props)
  val topic = "text_topic"
  try {
    //val collStrings = col1.collect.map(_.getInt(1)).mkString(" ")
    for (i <- 0 to 999) {
      val orderString = i.toString + "," + i.toString + "," + firstNameList(i) + " " + lastNameList(i) + "," + "5," + "Tacos," + "Food," + "card," + "10," + "4.99," + "2022-01-26 11:44," + "USA," +
        "San Jose," + "welovetacos.com," + i.toString + "," + "Y," + "null"
      val col3 = spark.sparkContext.parallelize(List((orderString))).toDF()
      val collStrings = col3.collect.map(_.getString(0)).mkString(" ")
      val record = new ProducerRecord[String, String](topic, collStrings, collStrings)
      val metadata = producer.send(record)
      printf(s"sent record(key=%s value=%s) " +
        "meta(partition=%d, offset=%d)\n",
        record.key(), record.value(),
        metadata.get().partition(),
        metadata.get().offset())
    }
  }catch{
    case e:Exception => e.printStackTrace()
  }finally {
    producer.close()
  }



















//  val df = spark.readStream
//    .format("kafka")
//    .option("kafka.bootstrap.servers", "[::1]:9092")
//    .option("subscribe", "TOPICNAME")
//    .option("startingOffsets", "from-beginning") // Starts reading messages from the beginning.
//    .load()

  spark.close()

}