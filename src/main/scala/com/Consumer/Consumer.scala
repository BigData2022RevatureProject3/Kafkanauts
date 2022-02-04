package com.Consumer

import com.Producer.ProducerPipeline

import java.util.{Collections, Properties}
import java.util.regex.Pattern
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._
import com.Producer.ProducerPipeline.useEC2
import com.Tools.SparkHelper
import org.codehaus.jackson.map.ser.std.StaticListSerializerBase
import os.RelPath

import scala.collection.mutable.ListBuffer


object Consumer extends App {

  val props: Properties = new Properties()
  props.put("group.id", "david")
  if (useEC2)
    props.put("bootstrap.servers", "ec2-44-202-112-109.compute-1.amazonaws.com:9092")
  else
    props.put("bootstrap.servers", "[::1]:9092")

  props.put("key.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  val consumer = new KafkaConsumer(props)
  val topics = List(ProducerPipeline.readTopic)
  val path = ProducerPipeline.consumerPath

  val buffer: ListBuffer[String] = ListBuffer[String]()
  val bufferLimit = 20

  println(s"Consumer reading from topic: ${topics.head}")
  println(s"Writing into ${path.toString()}")

  try {
    consumer.subscribe(topics.asJava)
    while (true) {
      val records = consumer.poll(10000L)
      val size = records.asScala.toList.length
      if (size != 0) {
        println(size)
        buffer ++= records.asScala.map(_.value.toString)
      }
      if (buffer.size > bufferLimit) {
        val batch = buffer.toList
        buffer.clear()
//        batch.foreach(println)
        if (ProducerPipeline.writeToFileNotHDFS)
          os.write.append(path, records.asScala.map(_.value.toString).mkString("\n"), createFolders = true)
        else {
          val spark = SparkHelper.spark
          import spark.implicits._
          val dataset = Team2Consumer.parseIntoDataSet(batch, false)
          dataset.show()
        }
      }
    }
  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    consumer.close()
  }
}
