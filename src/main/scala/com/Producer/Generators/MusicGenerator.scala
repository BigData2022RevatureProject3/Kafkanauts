package com.Producer.Generators
import com.ProductOrder
object MusicGenerator {
  private final val ran = scala.util.Random
  private final val musicFile = os.read.lines(os.pwd/"clean_data"/"track_info.txt").drop(1).toList

  def genMusic(po:ProductOrder): ProductOrder = {
    val music = musicFile(Math.abs(ran.nextInt(musicFile.length))).split("""\|""")
    val price = Math.floor(music(2).toDouble / 10000 * 100) / 100
    po.product_id = Math.abs(ran.nextLong())
    po.product_name = music(1)
    po.price = price
    po
  }

}
