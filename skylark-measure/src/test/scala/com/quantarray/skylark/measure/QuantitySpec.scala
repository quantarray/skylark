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

package com.quantarray.skylark.measure

import com.quantarray.skylark.measure.ConstantConvertibleQuantity.Implicits._
import com.quantarray.skylark.measure.conversion.PhysicalConstantConversionProvider
import com.quantarray.skylark.measure.substance.commodities.PhysicalSubstances
import org.scalatest.{FlatSpec, Matchers}

import scala.language.postfixOps

/**
 * Quantity spec.
 *
 * @author Araik Grigoryan
 */
class QuantitySpec extends FlatSpec with Matchers with PhysicalSubstances
{
  implicit val conversion = new PhysicalConstantConversionProvider()

  "Quantity" should "perform constant arithmetic" in
    {
      10.kg * 5 should be(50.kg)

      (10 * (kg of cotton)) * 5 should be(50 * (kg of cotton))
    }

  it should "perform quantity arithmetic" in
    {
      10.kg * 4.m should be(40 * (kg * m))
    }

  it should "be passable to typesafe method" in
    {
      def multiply(quantity: Quantity[MassMeasure], multiplier: Int): Quantity[MassMeasure] =
      {
        quantity * multiplier
      }

      multiply(10.kg, 2)
    }

  it should "" in
    {
      ((10 kg) to lb) should be(22.04625 lb)
    }

  "Basis point" should "be convertible to percent" in
    {
      val rate = 50 bp

      val apy = rate to percent

      apy should be(0.5.percent)
    }

  "Hectare" should "be convertible to square kilometers" in
    {
      val plot = 1.ha to (km ^ 2)

      plot should equal(0.01.km2)
    }

  "US gallon" should "be convertible to cubic inches" in
    {
      val vessel = 1.gal to (in ^ 3)

      vessel should equal(231.in3)
    }

  "Barrel" should "be convertible to gallon" in
    {
      // Some general substance, like water
      (1.bbl to gal) should equal(31.5.gal)

      // Specific petroleum substance, having a special conversion
      (1 * (bbl of wti) to gal) should equal(42.gal)
    }

  "Quantity of currency" should "be convertible to another quantity of currency via percent" in
    {
      val potOfGold = 30000.USD

      val rate = 5.percent

      val panOfGold = potOfGold * rate

      panOfGold should equal(1500 * (USD * UnitMeasure))
      panOfGold.measure.compact should equal(USD)
    }

  "Quantity of currency" should "be convertible to another quantity of currency via basis point" in
    {
      val potOfGold = 30000.USD

      val rate = 100.bp

      val panOfGold = potOfGold * rate

      panOfGold should equal(300 * (USD * UnitMeasure))
      panOfGold.measure.compact should equal(USD)
    }

  "Quantity per percent" should "be convertible to another quantity of per basis point" in
    {
      val rhoPercent = 2.5 * ((USD / MMBtu) / percent)

      val rhoBasisPoint = rhoPercent to ((USD / MMBtu) / bp)

      rhoBasisPoint should equal(0.025 * ((USD / MMBtu) / bp))
    }
}
