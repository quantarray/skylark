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

import com.quantarray.skylark.measure.any.conversion.DefaultMeasureConverterWithCanConvert
import com.quantarray.skylark.measure.any.implicits._
import com.quantarray.skylark.measure.any.measures._
import com.quantarray.skylark.measure.any.quantities._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.OptionValues._

import scala.language.postfixOps

class AnyQuantitySpec extends FlatSpec with Matchers
{
  "Quantity" should "perform constant arithmetic" in
    {
      10.kg * 5 should equal(50.kg)
    }

  it should "add/subtract safely" in
    {
      (3.kg + 3.lb) should be(Some(4.360775642116006 kg))
      (3.kg + 3.gr) should be(None)

      (3.kg - 3.lb) should be(Some(1.639224357883994 kg))
      (3.kg - 3.gr) should be(None)
    }

  it should "add/subtract unsafely" in
    {
      import com.quantarray.skylark.measure.any.arithmetic.unsafe._

      (3.kg + 3.lb) should be(4.360775642116006 kg)
      intercept[ConvertException]
        {
          3.kg + 3.gr
        }

      (3.kg - 3.lb) should be(1.639224357883994 kg)
      intercept[ConvertException]
        {
          3.kg - 3.gr
        }
    }

  it should "add/subtract with a custom CanConvert" in
    {
      implicit val measureCanConvert = new CanConvert[AnyMeasure, AnyMeasure]
      {
        private val cc = this

        override def convert: Converter[AnyMeasure, AnyMeasure] = new DefaultMeasureConverterWithCanConvert
        {
          implicit def canConvert: CanConvert[AnyMeasure, AnyMeasure] = cc

          protected override def convert(from: AnyMeasure, to: AnyMeasure): Option[Double] = ⤇(from, to) match
          {
            case `bbl` ⤇ `gal` => Some(42)
            case _ => super.convert(from, to)
          }
        }
      }

      3.0.bbl + 42.0.gal should equal(Some(4.bbl))
      42.gal + 1.bbl should equal(Some(84.gal))

      3.bbl - 42.gal should equal(Some(2.bbl))
      40.gal - 1.bbl should equal(Some(-2.gal))
    }

  it should "convert with a custom CanConvert" in
    {
      implicit val measureCanConvert = new CanConvert[AnyMeasure, AnyMeasure]
      {
        private val cc = this

        override def convert: Converter[AnyMeasure, AnyMeasure] = new DefaultMeasureConverterWithCanConvert
        {
          implicit def canConvert: CanConvert[AnyMeasure, AnyMeasure] = cc

          protected override def convert(from: AnyMeasure, to: AnyMeasure): Option[Double] = ⤇(from, to) match
          {
            case `bbl` ⤇ `gal` => Some(42)
            case _ => super.convert(from, to)
          }
        }
      }

      (bbl * (USD / gal)).to(USD).value should equal(42.0)
      ((bbl ^ -1.0) * (USD * gal)).to(USD).value should equal(1 / 42.0)
      (bbl / percent).to(gal).value should equal(4200)
      (bbl * (percent ^ -1.0)).to(gal).value should equal(4200)
    }

  it should "reduce unsafely" in
    {

      def targetSum(quantities: Seq[Quantity[Double, AnyMeasure]], target: AnyMeasure): Quantity[Double, AnyMeasure] =
      {
        quantities.reduce[Quantity[Double, AnyMeasure]]
          {
            (a, b) =>

              import com.quantarray.skylark.measure.any.arithmetic.unsafe._

              a.to(target).get + b
          }
      }

      targetSum(List(3.kg, 4.kg), g) should equal(7000.g)
    }
}
