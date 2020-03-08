#Hello Adthena !

##How to build and run the solution:
please unzip the provided file and go to the root directory and build:
+ **sbt package**

the Adthena sample prices and discounts are in a json file in the resources directories.  
now we can run the 2 unit tests:
+ **sbt test**

There are 2 sets of prices, and three sets of test items, all covered in the PricerTest.scala.

also you can run the main program:
+ **sbt "run Soup Soup Soup Soup Milk Bread Bread Bread Apples Apples"**

Results according to prices in "src/main/resources/current_prices.json" should be £8.30 and £7.30 respectively. 

Intellij project is provided.

##Description
"PriceBasket" is the main file, it loads the prices+discounts json file in the resources directory, then run the "Pricer" to adjust prices, after that it prints the invoice.


#Finally
  This was an interesting problem, and got me thinking about all what goes behind supermarket tills !