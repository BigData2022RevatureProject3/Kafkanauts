package com.Tools
import scala.collection.mutable.ListBuffer
import scala.io.Source

class musicGenerator{
  private final val ran = scala.util.Random
  private final val musicFile = "clean_data/track_info.txt"
  def genMusic():String={
    val lb = new ListBuffer[String]
    for(line <- Source.fromFile(musicFile).getLines.drop(1)){lb += line}
    val music = lb(ran.nextInt(lb.length)).split("""\|""")
    val price = Math.floor(music(2).toDouble/10000*100)/100
    music(0)+"*"+music(1)+"*"+price
  }
}
