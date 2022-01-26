package com.Producer
import scala.util.Random

object MathHelper {
  def main(args: Array[String]): Unit = {
    val list1 = List(0,1,2)
    val ans1 = (0 to 100000).map(_ => chooseFromList(list1)).sum / 100000
    println(s"Average is $ans1")

    val list2 = List(0,100)
    val probs = List(100, 150).map(_.toDouble)
    val ans2 = (0 to 100000).map(_ => chooseFromWeightedList(list2, probs).toDouble).sum / 100000
    println(s"Average 2 is $ans2")
  }


  def chooseFromList[T](list: List[T]): T = {
    list(Random.nextInt(list.length))
  }


  def chooseFromWeightedList[T](list: List[T], probabilities: List[Double]): T = {
    val sum = probabilities.sum
    val normalized = probabilities.map(_ / sum)
    var rand = Random.nextDouble()

    //
    for((l,p) <- list.zip(normalized)) {
      if (rand < p)
        return l
      else
        rand -= p
    }
    chooseFromList(list)
  }

}
