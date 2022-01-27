package com

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
   def getSampleOrder(): ProductOrder = {
      ProductOrder(1, 2, "Bob Burr", 3, "pname", "pcategory", "Card", 20, 9.99, "2004-05-23T14:25:10", "U.S", "Flagstaff", "google.com", 234, "Y", "")
   }

}
