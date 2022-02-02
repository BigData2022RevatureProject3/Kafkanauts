package com.Consumer

import java.util.{Collections, Properties}
import java.util.regex.Pattern
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._
import com.Producer.ProducerPipeline.useEC2
import os.RelPath

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
  val topics = List("team2")
  val path = os.pwd / RelPath("team2/products.csv")
  println(s"Consumer reading from topic: ${topics.head}")
  println(s"Writing into ${path.toString()}")

  try {
    consumer.subscribe(topics.asJava)
    while (true) {
      val records = consumer.poll(2000L)
      val size = records.asScala.toList.length
      if (size != 0)
        println(size)
      for (record <- records.asScala) {
        os.write.append(path, record.value.toString + "\n", createFolders = true)
      }
      //      for (record <- records.asScala) {
      //        println("Topic: " + record.topic() +
      //          ",Key: " + record.key() +
      //          ",Value: " + record.value() +
      //          ", Offset: " + record.offset() +
      //          ", Partition: " + record.partition())
      //      }
    }
  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    consumer.close()
  }
}
