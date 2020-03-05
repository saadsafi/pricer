package com.adthena.pricing

case class Price(item:String, price:Double, count:Int, discount:Double=0, message:String="")

object Pricer {

  def specialPrice(adj: Adjustment, itemsWithPrices: List[(String, Double)]): List[Price] = {
    val itemsWithCount = itemsWithPrices.groupBy(_._1).map {e=> (e._1, e._2.size) }.toMap
    val conditionApplies = adj.condition.forall { e => itemsWithCount.contains(e._1) && itemsWithCount(e._1) >= e._2 }

    val priceMap = itemsWithPrices.toMap
    if (conditionApplies) {
      adj.discount.map {e=> Price(e._1, priceMap(e._1) * (1.0 - e._2/100.0),
        itemsWithCount(e._1), priceMap(e._1) * (e._2/100.0), adj.message) }.toList
    } else {
      adj.discount.map {e=> Price(e._1, priceMap(e._1), itemsWithCount(e._1)) }.toList
    }
  }

  def adjustPrices(items: List[(String, Double)], adjustments: List[Adjustment]) : List[Price] = {

    val spItems = adjustments.foldLeft(List[Price]()) { (list, adj) =>
      list ++ specialPrice(adj, items)
    }
    val spMap = spItems.map(p=> p.item -> p).toMap
    val adjustedPrices = items.map {t => if(spMap.contains(t._1)) spMap(t._1) else Price(t._1, t._2, 1) }
    adjustedPrices
  }

  def getItemsAndPrices(items: List[String], prices:Map[String, Double]): List[(String, Double)] = {
    val diff = items.toSet.diff(prices.keys.toSet)
    if (!diff.isEmpty) throw new Exception("Given items without prices " + diff)

    items.map {item =>
      (item, prices(item))
    }
  }

}
