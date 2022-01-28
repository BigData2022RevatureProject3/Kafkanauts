package com

import com.Producer.GenHelper
import com.Tools.{DateHelper, MathHelper}

import java.time.LocalDateTime
import scala.util.Random

case class ProductOrder(
   var order_id: Long,
   var customer_id: Long,
   var customer_name: String,
   var product_id: Long,
   var product_name: String,
   var product_category: String,// String?
   var payment_type: String, // huh?
   var qty: Long,
   var price: Double,
   var datetime: String,
   var country: String,
   var city: String,
   var ecommerce_website_name: String,
   var payment_txn_id: Long,
   var payment_txn_success: String,
   var failure_reason: String
   )


object  ProductOrder {
   def toString(po: ProductOrder): String = {
      List(po.order_id, po.customer_id, po.customer_name, po.product_id, po.product_name, po.product_category,
         po.payment_type, po.qty, po.price, po.datetime, po.country, po.city, po.ecommerce_website_name,
         po.payment_txn_id, po.payment_txn_success, po.failure_reason).mkString("|")
   }
   def getInitialOrder(localDateTime: LocalDateTime, probabilities: List[Double] = List(1,1,1), countries: List[String] = GenHelper.countries): ProductOrder = {
      ProductOrder(-1, -1, "", 0, "", "", "", -1, -1,
         DateHelper.print(localDateTime), MathHelper.chooseFromWeightedList(countries, probabilities),
         "", "", -1, "", "")
   }
   def getSampleOrder(): ProductOrder = {
      ProductOrder(1, 2, "Bob Burr", 3, "pname", "pcategory", "Card", 20, 9.99, "2004-05-23T14:25:10", "U.S", "Flagstaff", "google.com", 234, "Y", "")
   }

   // TODO: Create a random order from any country/category, testing all generators
   def getRandomOrder(): ProductOrder = {
      val country = MathHelper.chooseFromList(List("China", "U.S", "Spain"))
      ProductOrder(Random.nextInt(20000), Random.nextInt(20000), "Bob Burr",
         Random.nextInt(20000), "pname", "pcategory", "Card", Random.nextInt(20) + 1, Random.nextDouble() * 20 + 1,
         "2004-05-23T14:25:10", country, "Flagstaff", "google.com", 234, "Y", "")
   }

}
