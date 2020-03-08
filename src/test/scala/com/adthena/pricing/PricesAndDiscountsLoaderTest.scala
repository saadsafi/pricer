package com.adthena.pricing

import org.scalatest.flatspec.AnyFlatSpecLike

class PricesAndDiscountsLoaderTest extends AnyFlatSpecLike {

  private val PRICES_FILE_PATH1 = "src/test/resources/current_prices1.json"
  private val PRICES_FILE_PATH2 = "src/test/resources/current_prices2.json"

  def fixture =
    new {
      val pd1 = PricesAndDiscountsLoader.getPricesAndDiscounts(PRICES_FILE_PATH1)
      val pd2 = PricesAndDiscountsLoader.getPricesAndDiscounts(PRICES_FILE_PATH2)
    }

  "Reading prices file 1" should "return 4 prices and 2 price adjustments" in {

    val f = fixture
    assert(!f.pd1.isEmpty, "Couldn't load prices file " + PRICES_FILE_PATH1)

    val pas = f.pd1.get
    assert(pas.prices.size == 4)

    assert(pas.adjustments.size == 2)
  }

  "Reading prices file 2" should "return 6 prices and 3 price adjustments" in {

    val f = fixture
    assert(!f.pd2.isEmpty, "Couldn't load prices file " + PRICES_FILE_PATH2)

    val pas = f.pd2.get
    assert(pas.prices.size == 6)

    assert(pas.adjustments.size == 3)
  }

}
