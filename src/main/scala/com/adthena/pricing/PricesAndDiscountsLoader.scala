package com.adthena.pricing

import java.io.File
import scala.io.Source
import argonaut.Argonaut._
import argonaut._


case class Adjustment(condition: Map[String, Int], discount: Map[String, Double], message: String)

case class PAs(prices: Map[String, Double], adjustments: List[Adjustment])

object PricesAndDiscountsLoader {

  private val PRICES_FILE_PATH = "src/main/resources/current_prices.json"

  def getPricesAndDiscounts(filePath: String = PRICES_FILE_PATH): Option[PAs] = {
    if (!(new File(filePath).exists)) {
      throw new Exception("Pricing file not found")
    }
    val json = Source.fromFile(filePath).getLines.mkString

    implicit def codecAdjustment: CodecJson[Adjustment] =
      casecodec3(Adjustment.apply, Adjustment.unapply)("condition", "discount", "message")

    implicit def codecPAs: CodecJson[PAs] =
      casecodec2(PAs.apply, PAs.unapply)("prices", "adjustments")

    json.decodeOption[PAs]
  }

}
