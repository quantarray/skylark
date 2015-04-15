/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2015 Quantarray, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quantarray.skylark.measure.market.conversion

import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.conversion.{PhysicalConstantConversionProvider, PhysicalSubstanceConversionProvider}
import com.quantarray.skylark.measure.market.MarketConvertibleQuantity.Implicits._
import com.quantarray.skylark.measure.market.{CommodityDeliveryLocations, DiscreteForwardCurve, GlobalMarket, TimeInstantCurve}
import com.quantarray.skylark.measure.substance.commodities.PhysicalSubstances
import com.quantarray.skylark.time.DateTimeSupport.Implicits.RichDateTime
import com.quantarray.skylark.time.IntervalSupport.Implicits.RichInterval
import com.quantarray.skylark.time.StringDate.StringToDateTime
import org.joda.time.{DateTime, Days, Months}
import org.scalatest.{FlatSpec, Matchers}

/**
 * Market conversion provider spec.
 *
 * @author Araik Grigoryan
 */
class MarketConversionProviderSpec extends FlatSpec with Matchers with PhysicalSubstances with CommodityDeliveryLocations
{
  val valueDate: DateTime = "2014-04-01".d

  implicit val market = new GlobalMarket(
    Seq(
      TimeInstantCurve(valueDate),
      DiscreteForwardCurve(USD / ((bbl of wti) at US.Oklahoma.Cushing), Seq(("2014-04-01".d, 100.0), ("2017-01-01".d, 150.0))),
      DiscreteForwardCurve.flat(1.2 * (CAD / USD), valueDate.until("2018-01-01".d).by(Days.ONE)),
      DiscreteForwardCurve.flat(120 * (CAD / JPY), valueDate.until("2018-01-01".d).by(Days.ONE)),
      DiscreteForwardCurve.flat(100 * (USD / JPY), valueDate.until("2018-01-01".d).by(Days.ONE))
    )
  )

  implicit val constantConversion = new PhysicalConstantConversionProvider()

  implicit val substanceConversion = new PhysicalSubstanceConversionProvider(constantConversion)

  implicit val mcp = new GlobalMarketConversionProvider(substanceConversion)

  "USD/bbl price" should "be addable to USD/gal price" in
    {
      val barrelPrice = 42 * (USD / bbl)
      val gallonPrice = 1 * (USD / (gal of wti))

      val price1 = barrelPrice + gallonPrice
      val price2 = gallonPrice + barrelPrice

      price1 should equal(84 * (USD / bbl))

      price2 should equal(2 * (USD / (gal of wti)))
    }

  it should "be subtractable from USD/gal price" in
    {
      val barrelPrice = 84 * (USD / bbl)
      val gallonPrice = 1 * (USD / (gal of wti))

      val price1 = barrelPrice - gallonPrice

      price1 should equal(42 * (USD / bbl))
    }

  it should "be addable to CAD/gal price" in
    {
      val barrelPrice = 42 * (USD / (bbl of wti))
      val gallonPrice = 1 * (CAD / gal)

      val price1 = (barrelPrice + gallonPrice) to (CAD / gal)
      val price2 = (gallonPrice + barrelPrice) to (USD / (bbl of wti))

      price1.value should equal(2.2 +- 0.0000000000001)
      price1.measure should be(CAD / gal)

      price2.value should equal(77.0 +- 0.0000000000001)
      price2.measure should be(USD / (bbl of wti))
    }

  it should "be addable to CAD/gal price and then convertible to USD/JPY" in
    {
      val barrelPrice = 42 * (USD / (bbl of wti))
      val gallonPrice = 1 * (CAD / gal)

      val price1 = (barrelPrice + gallonPrice) to (JPY / (bbl of wti))
      val price2 = (gallonPrice + barrelPrice) to (JPY / (bbl of wti))

      price1 should be(price2)

      price1 should equal(0.77 * (JPY / (bbl of wti)))

      price2 should equal(0.77 * (JPY / (bbl of wti)))
    }

  "bbl" should "be convertible to gal" in
    {
      val bblOfWti = bbl of wti
      val barrels = 5 * bblOfWti
      val gallons = barrels to gal
      gallons should equal(210.gal)
    }

  "MW * h" should "be convertible to W * s" in
    {
      (5 * (MW * h) to (W * s)) should equal(1.8E10 * (W * s))
    }

  "Quantity of asset" should "be convertible to measure of currency via a market" in
    {
      val bblOfWti = bbl of wti

      val tanker = 3000000 * (bblOfWti at US.Oklahoma.Cushing)

      val valueAtPrompt0_1 = tanker to USD // Prompt delivery

      valueAtPrompt0_1 should equal(3E8.USD)

      val valueOnJAN17_1 = tanker to (USD deliveredOn "2017-01-01".d) // JAN-17 delivery at Cushing

      valueOnJAN17_1 should equal(4.5E8 * (USD deliveredOn "2017-01-01".d))
    }

  "Currency" should "be addable to another currency" in
    {
      val moneys = 1.USD + 1.CAD

      moneys.value should be(1.8333333 +- 0.0000001)
    }
}
