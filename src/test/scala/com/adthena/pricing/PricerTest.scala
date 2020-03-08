package com.adthena.pricing

import org.scalatest.flatspec.AnyFlatSpecLike

class PricerTest extends AnyFlatSpecLike {

  private val PRICES_FILE_PATH1 = "src/test/resources/current_prices1.json"
  private val PRICES_FILE_PATH2 = "src/test/resources/current_prices2.json"

  def fixture =
    new {
      val pd1 = PricesAndDiscountsLoader.getPricesAndDiscounts(PRICES_FILE_PATH1)
      val pd2 = PricesAndDiscountsLoader.getPricesAndDiscounts(PRICES_FILE_PATH2)
      assert(!pd1.isEmpty, "Couldn't load prices file " + PRICES_FILE_PATH1)
      assert(!pd2.isEmpty, "Couldn't load prices file " + PRICES_FILE_PATH2)
      val pas1 = pd1.get
      val pas2 = pd2.get

      val items1 = List("Soup", "Soup", "Soup", "Soup", "Milk", "Bread", "Bread", "Bread", "Apples", "Apples")
      val items2 = List("Milk", "Milk", "Milk", "Pears", "Pears", "Apples", "Apples", "Bread")
      val items3 = List("Soup", "Soup", "Bananas", "Bananas", "Apples", "Apples")
    }


  "Items with prices" should "return 10" in {

    val f = fixture
    val itemsWithPrices = Pricer.getItemsAndPrices(f.items1, f.pas1.prices)
    assert(itemsWithPrices.size == 10)
  }

  "Items with adjusted prices" should "return 6 conditions resulting in 4 discounts" in {

    val f = fixture
    val itemsWithPrices = Pricer.getItemsAndPrices(f.items1, f.pas1.prices)
    val itemsWithSomeDiscounts = Pricer.adjustPrices(itemsWithPrices, f.pas1.adjustments)
    val itemsWithConditions = itemsWithSomeDiscounts.filter(!_.conditionForAdjustments.isEmpty)
    val itemsWithDiscounts = itemsWithSomeDiscounts.filter(!_.adjustments.isEmpty)

    assert(itemsWithConditions.size == 6)
    assert(itemsWithDiscounts.size == 4)
  }

  "Item-set-1 subtotal, discounts and total" should "return 830, 730 and 100 (all in pennies)" in {

    val f = fixture
    val itemsWithPrices = Pricer.getItemsAndPrices(f.items1, f.pas1.prices)
    val prices = Pricer.adjustPrices(itemsWithPrices, f.pas1.adjustments)

    val subTotal = Pricer.getSubTotal(prices)
    val discounts = Pricer.getDiscounts(prices).sum
    val total = subTotal - discounts

    // "Soup", "Soup", "Soup", "Soup", "Milk", "Bread", "Bread", "Bread", "Apples", "Apples"
    assert(subTotal == (4 * 65 + 130 + 3 * 80 + 2 * 100))
    assert(discounts == (2 * 40 + 2 * 10))
    assert(total == (4 * 65 + 130 + 2 * 40 + 80 + 2 * 90))
  }

  "Item-set-2 subtotal and discounts" should "return 830 and 730 (in pennies)" in {

    val f = fixture
    val itemsWithPrices = Pricer.getItemsAndPrices(f.items2, f.pas2.prices)
    val prices = Pricer.adjustPrices(itemsWithPrices, f.pas2.adjustments)

    val subTotal = Pricer.getSubTotal(prices)
    val discounts = Pricer.getDiscounts(prices).sum

    // "Milk", "Milk", "Milk", "Pears", "Pears", "Apples", "Apples", "Bread"
    assert(subTotal == (3 * 130 + 2 * 90 + 2 * 100 + 80))
    assert(discounts == (30 + 40))
  }

  "Item-set-3 subtotal and discounts" should "return 830 and 730 (in pennies)" in {

    val f = fixture
    val itemsWithPrices = Pricer.getItemsAndPrices(f.items3, f.pas2.prices)
    val prices = Pricer.adjustPrices(itemsWithPrices, f.pas2.adjustments)

    val subTotal = Pricer.getSubTotal(prices)
    val discounts = Pricer.getDiscounts(prices).sum

    // "Soup", "Soup", "Bananas", "Bananas", "Apples", "Apples"
    assert(subTotal == (2 * 65 + 2 * 120 + 2 * 100))
    assert(discounts == (2 * 30))
  }
}
