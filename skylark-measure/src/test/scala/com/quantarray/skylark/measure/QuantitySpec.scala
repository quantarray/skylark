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

import com.quantarray.skylark.measure.quantity._
import com.quantarray.skylark.measure.conversion._
import org.scalatest.{FlatSpec, Matchers}

import scala.language.postfixOps

/**
 * Quantity spec.
 *
 * @author Araik Grigoryan
 */
class QuantitySpec extends FlatSpec with Matchers
{
  "Quantity" should "perform constant arithmetic" in
    {
      10.0.kg * 5 should be(50.0.kg)
    }

  it should "perform quantity arithmetic" in
    {
      10.0.kg * 4.0.m should be(Quantity(40, kg * m))
      4.0.oz_troy * 7.0.percent should be(Quantity(0.28, oz_troy))

      10.0.kg / 2.0.m should be(Quantity(5, kg / m))
      10.0.USD / 2.0.percent should be(Quantity(500, USD))
    }

  it should "be passable to typesafe method" in
    {
      def multiply(quantity: Quantity[MassMeasure], multiplier: Int): Quantity[MassMeasure] =
      {
        quantity * multiplier
      }

      multiply(10.0.kg, 2)
    }

  it should "convertible to lb" in
    {
      ((10.0 kg) to lb) should be(22.04625 lb)
    }

  "ft" should "convertible to in" in
  {
    (1.0.ft to in) should be(12.0 in)
    (12.0.in to ft) should be(1.0 ft)
  }

  "yd" should "convertible to in" in
    {
      //(1.0.yd to in) should be(36.0 in)
    }

  "Basis point" should "be convertible to percent" in
    {
      val rate = 50.0 bp
      val apy = rate to percent
      apy should be(0.5.percent)
    }

  "Hectare" should "be convertible to square kilometers" in
    {
      val plot = 1.0.ha to (km ^ 2)
      plot should equal(0.01.km2)
    }

  "US gallon" should "be convertible to cubic inches" in
    {
      val vessel = 1.0.gal to (in ^ 3)
      vessel should equal(231.0.in3)
    }

  "Barrel" should "be convertible to gallon" in
    {
      // Some general substance, like water
      (1.0.bbl to gal) should equal(31.5.gal)

      import com.quantarray.skylark.measure.conversion.commodity._

      // Specific petroleum substance, having a special conversion
      (1.0.bbl to gal) should equal(42.0.gal)
    }

  "Quantity of currency" should "be convertible to another quantity of currency via percent" in
    {
      val potOfGold = 30000.0.USD
      val rate = 5.0.percent

      val panOfGold = potOfGold * rate

      panOfGold should equal(Quantity(1500, USD))
    }

  "Quantity of currency" should "be convertible to another quantity of currency via basis point" in
    {
      val potOfGold = 30000.0.USD
      val rate = 100.0.bp

      val panOfGold = potOfGold * rate

      panOfGold should equal(Quantity(300, USD))
    }

  "Quantity per percent" should "be convertible to another quantity of per basis point" in
    {
//      val rhoPercent = Quantity(2.5, (USD / MMBtu) / percent)
//      val rhoBasisPoint = rhoPercent to ((USD / MMBtu) / bp)
//
//      rhoBasisPoint should equal(Quantity(0.025, (USD / MMBtu) / bp))
    }
}
