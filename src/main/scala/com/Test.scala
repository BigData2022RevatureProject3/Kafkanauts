package com

import com.Tools.POCityGenerator

object Test {
  def main(args: Array[String]): Unit = {
    print(POCityGenerator.genCity("123","Spain"))
    val po = ProductOrder.getSampleOrder()
//    ProductOrder.show(po)
  }
}
