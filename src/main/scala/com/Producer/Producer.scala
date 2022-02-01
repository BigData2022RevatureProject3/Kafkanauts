package com.Producer

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
object Producer{

  val props:Properties = new Properties()
  props.put("bootstrap.servers","[::1]:9092")
  props.put("key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put("acks","all")
  val producer = new KafkaProducer[String, String](props)
  val topic = "text_topic"

  def send(pStrs:String) {
    try {
      val record = new ProducerRecord[String, String](topic, pStrs)
      val metadata = producer.send(record)
      printf(s"sent record(key=%s value=%s) " +
        "meta(partition=%d, offset=%d)\n",
        record.key(), record.value(),
        metadata.get().partition(),
        metadata.get().offset())
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      //producer.close()
    }
  }
}

