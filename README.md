#Hello Adthena !

##How to build and run the solution:
please unzip the provided file and go to the root directory and build:
+ **mvn package -DskipTests=true**
    
the Adthena sample prices and discounts are in a json file in the resources directories.  
now we can run the 2 unit tests:
+ **mvn test**

also you can run the main program from the console (argument list is in the pom file):
+ **mvn scala:run**

But the source can also be built with sbt (sbt.build is provided and tested in intellij).

##Description
"PriceBasket" is the main file, it loads the prices+discounts json file in the resources directory, then run the "Pricer" to adjust prices, afterthat it prints the invoice.
There is a limitation that the discounts applies to all relevant items, and it should be only to some: 
for example 2 tins of soup should give half price only to one loaf of bread.
The code to do this is incomplete but I know what should be done if I have more time.  

#Finally
  This was an interesting problem, and got me thinking about all what goes behind supermarket tills !