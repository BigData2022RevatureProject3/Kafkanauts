package com.Producer.Generators

import com.ProductOrder

import scala.collection.mutable.ListBuffer
import scala.io.Source

object MusicGenerator {
  private final val ran = scala.util.Random
  private final val musicFile = "clean_data/track_info.txt"

  def genMusic(po:ProductOrder): ProductOrder = {
    val lb = new ListBuffer[String]
    for (line <- Source.fromFile(musicFile).getLines.drop(1)) {
      lb += line
    }
    val music = lb(ran.nextInt(lb.length)).split("""\|""")
    val price = Math.floor(music(2).toDouble / 10000 * 100) / 100
    po.product_id = Math.abs(ran.nextLong())
    po.product_name = music(1)
    po.price = price
    po
  }
}
