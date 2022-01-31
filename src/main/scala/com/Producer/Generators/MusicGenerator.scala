package com.Producer.Generators

import com.ProductOrder

import scala.collection.mutable.ListBuffer
import scala.io.Source

object MusicGenerator {
  private final val ran = scala.util.Random
  private final val musicFile = os.read.lines(os.pwd/"clean_data"/"track_info.txt").drop(1)

  def genMusic(po:ProductOrder): ProductOrder = {
    val price = Math.floor(musicFile(2).toDouble / 10000 * 100) / 100
    po.product_id = Math.abs(ran.nextLong())
    po.product_name = musicFile(1)
    po.price = price
    po
  }
}
