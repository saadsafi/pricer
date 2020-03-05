package com.adthena.pricing

import org.scalatest.flatspec.AnyFlatSpecLike

class PricerTest extends AnyFlatSpecLike {

  def fixture =
    new {
      val pd = PricesAndDiscountsLoader.getPricesAndDiscounts()
      assert(!pd.isEmpty, "Couldn't load prices")
      val pas = pd.get

      val items = List("Soup", "Soup", "Milk", "Bread", "Apples", "Apples")
    }

  "Items with prices" should "return 6" in {

    val f = fixture

    val itemsWithPrices = Pricer.getItemsAndPrices(f.items, f.pas.prices)

    assert(itemsWithPrices.size == 6)
  }

  "Items with adjusted prices" should "return 3" in {

    val f = fixture

    val itemsWithPrices = Pricer.getItemsAndPrices(f.items, f.pas.prices)
    val itemsWithSomeAdjustedPrices = Pricer.adjustPrices(itemsWithPrices, f.pas.adjustments)
    val itemsWithAdjustedPrices = itemsWithSomeAdjustedPrices.filter(!_.message.isEmpty)

    assert(itemsWithAdjustedPrices.size == 3)
  }

}
