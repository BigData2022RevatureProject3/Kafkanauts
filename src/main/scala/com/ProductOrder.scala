package com

case class ProductOrder(
   order_id: Long,
   customer_id: Long,
   customer_name: String,
   product_id: String,
   product_name: String,
   product_category: String,// String?
   payment_type: String, // huh?
   qty: Long,
   price: Double,
   datetime: String,
   country: String,
   city: String,
   ecommerce_website_name: String,
   payment_txn_id: Long,
   payment_txn_success: String,
   failure_Reason: String
   )
