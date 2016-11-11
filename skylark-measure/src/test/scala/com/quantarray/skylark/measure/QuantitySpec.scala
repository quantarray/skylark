/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2016 Quantarray, LLC
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

package com.quantarray.skylark.measure

import com.quantarray.skylark.measure.implicits._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.OptionValues._

import scala.language.postfixOps

/**
  * Quantity spec.
  *
  * @author Araik Grigoryan
  */
class QuantitySpec extends FlatSpec with Matchers
{
  private val numericTolerance = 0.00000000001

  /**
    * EnergyPrice / () -> EnergyPrice / ().
    */
  implicit val energyPricePerDimensionlessCanConvert = CanConvert(EnergyPricePerDimensionlessConverter(CanConvert.empty))

  "Quantity" should "perform constant arithmetic" in
    {
      10.kg * 5 should equal(50.kg)
    }

  it should "perform quantity arithmetic" in
    {
      10.kg * 4.m should equal(40.0 * (kg * m))
      (4.oz_troy * 7.percent).to(oz_troy).value should equal(0.28.oz_troy)

      10.kg / 2.m should equal(5.0 * (kg / m))
      (10.USD / 2.percent).to(USD).value should equal(500 USD)

      (10.kg + 3.kg) should equal(Some(13 kg))
      (10.kg - 3.kg) should equal(Some(7 kg))
      (10.kg + 3.lb) should equal(Some(11.360775642116007 kg))
      (10.lb - 3.kg) should equal(Some(3.386125 lb))
    }

  it should "be passable to typesafe method" in
    {
      def multiply(quantity: Quantity[Double, MassMeasure], multiplier: Int): Quantity[Double, MassMeasure] =
      {
        quantity * multiplier
      }

      multiply(10.kg, 2)
    }

  it should "convertible to lb" in
    {
      ((10.0 kg) to lb).value should equal(22.04625 lb)
    }

  "ft" should "convertible to in" in
    {
      (1.ft to in).value should equal(12.0 in)
      (12.in to ft).value should equal(1.0 ft)
    }

  "yd" should "convertible to in" in
    {
      (1.yd to in).value should equal(36.0 in)
    }

  "Basis point" should "be convertible to percent" in
    {
      val rate = 50.0 bp
      val apy = rate to percent
      apy.value should equal(0.5.percent)
    }

  "Hectare" should "be convertible to square kilometers" in
    {
      val plot = 1.ha to (km ^ 2)
      plot.value should equal(0.01.km2)
    }

  "US gallon" should "be convertible to cubic inches" in
    {
      val vessel = 1.gal to (in ^ 3)
      vessel.value should equal(231.in3)
    }

  "Barrel" should "be convertible to gallon" in
    {
      // Some general substance, like water
      (1.bbl to gal).value should equal(31.5.gal)

      import com.quantarray.skylark.measure.conversion.commodity.default._

      // Specific petroleum substance, having a special conversion
      (1.bbl to gal).value should equal(42.gal)
    }

  "Quantity of currency" should "be convertible to another quantity of currency via percent" in
    {
      val potOfGold = 30000.USD
      val rate = 5.percent

      val panOfGold = potOfGold * rate

      panOfGold should equal(150000.0 * (USD * percent))
    }

  "Quantity of currency" should "be convertible to another quantity of currency via basis point" in
    {
      val potOfGold = 30000.USD
      val rate = 100.bp

      val panOfGold = (potOfGold * rate).to(USD)

      panOfGold.value should equal(300.USD)
    }

  "Quantity per percent" should "be convertible to another quantity of per basis point" in
    {
      val rhoPercent = Quantity(2.5, (USD / MMBtu) / percent)
      val rhoBasisPoint = rhoPercent to ((USD / MMBtu) / bp)

      rhoBasisPoint.value should equal(Quantity(0.025, (USD / MMBtu) / bp))
    }

  "Price times FX rate" should "simplify" in
    {
      val usdPrice = 3.0 * (USD / MMBtu)
      val fxRate = 2.0 * (CAD / USD)

      val cadPrice = usdPrice * fxRate

      val expectedCadPrice = 6.0 * ((USD / MMBtu) * (CAD / USD))

      cadPrice should equal(expectedCadPrice)

      cadPrice.simplify.value should equal(6 * (CAD / MMBtu))
    }

  "CanConvert" should "allow flexible units" in
    {
      case class TradeProvider()
      {
        def trade[Q <: Measure[Q], D <: Measure[D]](quantity: Quantity[Double, Q], price: Quantity[Double, Price[Currency, D]])
                                                   (implicit cc: CanConvert[Q, D]): Unit =
        {
        }
      }

      val provider = TradeProvider()

      provider.trade(10.MMBtu, Quantity(2.0, CAD / GJ))
    }

  it should "allow trivial empty definition" in
    {
      (5.bbl to bbl).value should equal(5.0 bbl)
    }

  "mt (metric ton)" should "be convertible to cbu (corn bushels)" in
    {
      import com.quantarray.skylark.measure.commodity.us.commercial.grains.corn.shelled.{bushel => cbu}

      val container = 1 * mt
      container.to(cbu).value.value should equal(39.36830357142857 +- numericTolerance)
    }

  type VelocityMeasure = RatioMeasure[LengthMeasure, TimeMeasure]

  type MomentumMeasure = ProductMeasure[MassMeasure, VelocityMeasure]

  type Mass = Quantity[Double, MassMeasure]

  type Velocity = Quantity[Double, VelocityMeasure]

  type Momentum = Quantity[Double, MomentumMeasure]

  def momentum(mass: Mass, velocity: Velocity): Momentum = mass * velocity

  "Mass and velocity quantities" should "be strongly typed" in
    {
      momentum(5.kg, 4 * (m / s)) should be(20 * (kg * (m / s)))
    }
}
