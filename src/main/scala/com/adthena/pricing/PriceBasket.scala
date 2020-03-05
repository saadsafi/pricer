package com.adthena.pricing


object PriceBasket {

  def main(args: Array[String]): Unit = {

    val items = args.toList
    if (items.isEmpty) throw new Exception("No items given to price !")

    val po = PricesAndDiscountsLoader.getPricesAndDiscounts()
    val pas = po.getOrElse(throw new Exception("Couldn't load prices"))

    val itemsWithPrices = Pricer.getItemsAndPrices(items, pas.prices)

    val itemsWithSomeAdjustedPrices = Pricer.adjustPrices(itemsWithPrices, pas.adjustments)

    printInvoice(itemsWithPrices, itemsWithSomeAdjustedPrices)
  }

  def printInvoice(itemsWithPrices: List[(String, Double)], itemsWithAdjustedPrices: List[Price]): Unit = {

    // Saad: we can also explicitly use Locale.UK
    val formatter = java.text.NumberFormat.getCurrencyInstance()
    val subTotal = itemsWithPrices.map(_._2).sum
    val total = itemsWithAdjustedPrices.map(_.price).sum
    val discountsPrices = itemsWithAdjustedPrices.filter(!_.message.isEmpty)
      .foldLeft(collection.mutable.Map[String, Double]()) { (map, price) =>
        map += map.get(price.message).map { x =>
          price.message -> (x + price.discount)
        }
          .getOrElse(price.message -> price.discount)
      }
    println("Subtotal: " + formatter.format(subTotal / 100.0))
    if (discountsPrices.isEmpty)
      println("No offers available")
    else
      discountsPrices.foreach(dp => println(s"${dp._1} :" + formatter.format(dp._2 / 100)))
    println("Total price: " + formatter.format(total / 100.0))
  }

}
