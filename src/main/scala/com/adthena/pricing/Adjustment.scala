package com.adthena.pricing

import scala.collection.mutable.ArrayBuffer

case class Adjustment(conditions: Map[String, Int], discount: Map[String, Double], message: String) {
  def conditionApplies(prices: Seq[Price]) = {
    conditions.forall {
      cond => prices.count(price => price.item == cond._1 && !price.conditionForAdjustments.contains(this)) >= cond._2
    }
  }
  def applyConditionToPrices(prices: Seq[Price]) = {
    conditions.flatMap {
      cond => prices.filter(price => price.item == cond._1 && !price.conditionForAdjustments.contains(this)).take(cond._2)
    }.map(price => price.conditionForAdjustments += this)
  }

  def discountApplies(prices: Seq[Price]) = {
    prices.count(price => price.item == DiscountedItem && !price.adjustments.contains(this)) > 0
  }
  def applyDiscountToPrice(prices: Seq[Price]) = {
    prices.filter(price => price.item == DiscountedItem && !price.adjustments.contains(this)).head.adjustments += this
  }

  // the item discounted
  def DiscountedItem = discount.keys.head
  // this is the discount as percentage of price
  def Discount = discount.values.head
}

case class Discount(item:String, discount:Double)

case class Price(item:String, price:Double,
                 adjustments:ArrayBuffer[Adjustment] = ArrayBuffer[Adjustment](),
                 conditionForAdjustments:ArrayBuffer[Adjustment] = ArrayBuffer[Adjustment]()) {

  def discountsWithMessages: Seq[(String, Double)] = {
    adjustments.map(adj => (adj.message, adj.Discount/100.0 * price))
  }

  def discounts: Seq[Double] = {
    adjustments.map(adj => adj.Discount/100.0 * price)
  }
}

case class PAs(prices: Map[String, Double], adjustments: List[Adjustment])
