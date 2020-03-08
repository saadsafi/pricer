package com.adthena.pricing


object PriceBasket {

  def main(args: Array[String]): Unit = {

    val items = args.toList
    if (items.isEmpty) throw new Exception("No items given to price !")

    val po = PricesAndDiscountsLoader.getPricesAndDiscounts()
    val pas = po.getOrElse(throw new Exception("Couldn't load prices"))

    val itemsWithPrices = Pricer.getItemsAndPrices(items, pas.prices)

    val itemsWithAdjustedPrices = Pricer.adjustPrices(itemsWithPrices, pas.adjustments)

    printInvoice(itemsWithAdjustedPrices)
  }

  def printInvoice(prices: List[Price]): Unit = {

    val subTotal = Pricer.getSubTotal(prices)
    //prices.foreach(println(_))

    val messageAndDiscounts = Pricer.getMessageAndDiscounts(prices)
    println("Subtotal: £" + f"${subTotal / 100.0}%1.2f")
    if (messageAndDiscounts.isEmpty)
      println("No offers available")
    else
      messageAndDiscounts.foreach(dp => println(s"${dp._1}: £" + f"${dp._2 / 100.0}%1.2f"))

    val discounts = Pricer.getDiscounts(prices).sum
    println("Total price: £" + f"${(subTotal - discounts) / 100.0}%1.2f")
  }

}
