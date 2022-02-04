package com.Analyzer

object ZeppelinFunctions extends App{

  //  A place to paste scala functions (like for loading our data), as well as sql strings
  // Load data into dataframe in zeppelin
  """"

    val text = sc.textFile("hdfs://localhost:9000/user/steve_rev/valid_products.csv")
    val data = text.toDF().select("*")
    val df2 = data.select(
    split(col("value"), "\\|").getItem(0).as("order_id").cast("long"),
    split(col("value"), "\\|").getItem(1).as("customer_id").cast("long"),
    split(col("value"), "\\|").getItem(2).as("customer_name").cast("string"),
    split(col("value"), "\\|").getItem(3).as("product_id").cast("long"),
    split(col("value"), "\\|").getItem(4).as("product_name").cast("string"),
    split(col("value"), "\\|").getItem(5).as("product_category").cast("string"),
    split(col("value"), "\\|").getItem(6).as("payment_type").cast("string"),
    split(col("value"), "\\|").getItem(7).as("qty").cast("long"),
    split(col("value"), "\\|").getItem(8).as("price").cast("double"),
    split(col("value"), "\\|").getItem(9).as("datetime").cast("string"),
    split(col("value"), "\\|").getItem(10).as("country").cast("string"),
    split(col("value"), "\\|").getItem(11).as("city").cast("string"),
    split(col("value"), "\\|").getItem(12).as("ecommerce_website_name").cast("string"),
    split(col("value"), "\\|").getItem(13).as("payment_txn_id").cast("long"),
    split(col("value"), "\\|").getItem(14).as("payment_txn_success").cast("string"),
    split(col("value"), "\\|").getItem(15).as("failure_reason").cast("string")
  ).drop("value")
  df2.createOrReplaceTempView("products")
  """


  //Bao SQL Code Portion
  """

  SELECT SUBSTRING(datetime,1,10) as date,country,COUNT(product_id) AS total_orders FROM products GROUP BY date, country HAVING country NOT IN("null") AND date NOT IN ("null") ORDER BY date,total_orders DESC;
  SELECT SUBSTRING(datetime,1,10) as date,product_category,COUNT(product_id) AS total_orders FROM products GROUP BY date, product_category HAVING product_category IN("Electronics","Entertainment","Computers","Food","Home")AND date NOT IN ("null") ORDER BY date,total_orders DESC;

  """


  //Ben SQL Code Portion
  //Brady SQL Code Portion
  //David SQL Code Portion
  //Teddy SQL Code Portion
  //Tiffany SQL Code Portion

  //Steven SQL Code Portion
  """

  SELECT distinct product_category, COUNT(product_category) as Number_Of_Products FROM products Where product_category In ("Electronics", "Computers", "Food", "Entertainment", "Home") GROUP By product_category;

  """
}
