package com

import com.Producer.Generators.CityGenerator

object Test {
  def main(args: Array[String]): Unit = {
    print(CityGenerator.genCity("123","Spain"))
    val po = ProductOrder.getSampleOrder()
//    ProductOrder.show(po)
  }
}
