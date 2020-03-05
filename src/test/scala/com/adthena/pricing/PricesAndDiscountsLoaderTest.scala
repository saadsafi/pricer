package com.adthena.pricing

import org.scalatest.flatspec.AnyFlatSpecLike

class PricesAndDiscountsLoaderTest extends AnyFlatSpecLike {

  def fixture =
    new {
      val pd = PricesAndDiscountsLoader.getPricesAndDiscounts()
    }

  "Reading prices file" should "return 4 prices and 3 price adjustments" in {

    val f = fixture
    assert(!f.pd.isEmpty, "Couldn't load prices")

    val pas = f.pd.get
    assert(pas.prices.size == 4)

    assert(pas.adjustments.size == 2)
  }

}
