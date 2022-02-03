# Project 3
In this document, all rules,and requirements on how to proceed with defining and implementing the Revature capstone project are defined. In this project the Cohort will be divided into two teams. Each team will begin by creating a data producer that will generate real time data simulating orders from an E-Commerce application. Each team will then consume the output data from the other team through Kafka and run additional processing through Spark.The finalgoal will be to decipher the algorithms used to generate data from the other team based on the output.

## Tasks
- Create a producer program that will ingest data to a Kafka Topic.
  - Data will have to be generated in the program.
  - Up to 5% of the data can be bad data
  - The data generation methods must be self-sustaining
    - No changing the algorithm unannounced during the project
    - No hard coded value changes based on dates ie. X% increase on 11/30/2021
  - Ingest the datafrom the other teamevery 2 seconds into the Kafka Topic.
- Display the data from the input Kafka Topic in a console consumer (CLI).
- Create a consumer program in Sparkthat will readand cleanthe data stream from the input Kafka Topic and will process the data further.
  - Read the data into DataFrame objects.
  - Print the schema of the input data stream
  - Apply the above-mentioned schemato the dataframesand print the schema.
  - Apply Exception Handling wherever applicable for a stable application.
  - From the consumer program:
    - Collect the data and manipulate/aggregate it to best allow you to predict what logic is being used to produce the data.
  - Display a visualization of all above outputs in Zeppelin
  
## Fields(Schema)
Field Name  | Description
------------- | -------------
order_id  | Order Id
customer_id  |  Customer Id
customer_name | Customer Name
product_id | Product Id
product_name | Product Name
product_category | Product Category
payment_type | Payment Type (card, Internet Banking, UPI, Wallet)
qty | Quantity ordered
price | Price of the product
datetime | Data and time when order was placed
country | Customer Country
city | Customer City
ecommerce_website_name | Site from where order was placed
payment_txn_id | Payment Transaction Confirmation Id
payment_txn_success | Payment Success or Failure (Y=Success. N=Failed)
failure_reason | Reason for payment failure

## Technologies:
- Java 1.8.0_311
- Scala 2.12.10
- Spark 3.1.2
- Kafka 2.8.1 (for Scala 2.12)
- HDFS
- Zeppelin 0.10.0
- Git/GitHub
- IntelliJ 2021.3.1 Community Edition
- JIRA
- Google Drive (Doc, Slides)

