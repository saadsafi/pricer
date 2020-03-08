package com.adthena.pricing


object Pricer {

  def adjustPrices(itemsWithPrices: List[(String, Double)], adjustments: List[Adjustment]) : List[Price] = {

    val prices = itemsWithPrices.map(t => Price(t._1, t._2)).toList

    adjustments.foreach { adj =>
      while (adj.conditionApplies(prices) && adj.discountApplies(prices)) {
        adj.applyConditionToPrices(prices)
        adj.applyDiscountToPrice(prices)
      }
    }
    prices
  }

  def getItemsAndPrices(items: List[String], prices:Map[String, Double]): List[(String, Double)] = {
    val diff = items.toSet.diff(prices.keys.toSet)
    if (!diff.isEmpty) throw new Exception("Given items without prices " + diff)

    items.map {item =>
      (item, prices(item))
    }
  }

  def getSubTotal(prices: Seq[Price]) : Double = {
    prices.map(_.price).sum
  }

  def getMessageAndDiscounts(prices: Seq[Price]) : Map[String, Double] = {
    prices.filter(!_.adjustments.isEmpty)
      .flatMap { _.discountsWithMessages }.groupBy(_._1).mapValues(_.map(_._2).sum)
  }

  def getDiscounts(prices: Seq[Price]) : Seq[Double] = {
    prices.filter(!_.adjustments.isEmpty).flatMap { _.discounts }
  }

}
