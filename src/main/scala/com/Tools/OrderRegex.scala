package com.Tools

class OrderRegex {
  protected def isValidOrder(order:String):Boolean ={
    val pattern = "^\\d+[,]\\d+[,]\\w+\\s\\w+,\\d+,\\w+,\\w+,\\w+,\\d+,\\d+.\\d+,\\d+-\\d+-\\d+\\s\\d+:\\d+,\\w+,\\w+\\s\\w+,\\w+.\\w+,\\d+,[YN],\\w+$".r
    val result = (pattern findAllIn order)
    if (result.isEmpty) {
      return false
    }
    return true
  }
}
