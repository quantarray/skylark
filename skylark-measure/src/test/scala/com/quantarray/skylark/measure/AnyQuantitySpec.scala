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

import com.quantarray.skylark.measure.measures._
import com.quantarray.skylark.measure.quantities.any._
import com.quantarray.skylark.measure.untyped.implicits._
import org.scalatest.{FlatSpec, Matchers}

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
      import com.quantarray.skylark.measure.untyped.arithmetic.unsafe._

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
        override def convert: Converter[AnyMeasure, AnyMeasure] = new AnyMeasureConverter
        {
          protected override def convert(from: AnyMeasure, to: AnyMeasure): Option[Double] = ⤇(from, to) match
          {
            case `bbl` ⤇ `gal` => Some(42)
            case `gal` ⤇ `bbl` => apply(to, from).map(1.0 / _)
            case _ => super.convert(from, to)
          }
        }
      }

      3.0.bbl + 42.0.gal should equal(Some(4.bbl))
      42.gal + 1.bbl should equal(Some(84.gal))

      3.bbl - 42.gal should equal(Some(2.bbl))
      40.gal - 1.bbl should equal(Some(-2.gal))
    }

  it should "reduce unsafely" in
  {

    def targetSum(quantities: Seq[AnyQuantity[Double]], target: AnyMeasure): AnyQuantity[Double] =
    {
      quantities.reduce[AnyQuantity[Double]]
      {
        (a, b) =>

          import com.quantarray.skylark.measure.untyped.arithmetic.unsafe._

          a.to(target).get + b
      }
    }

    targetSum(List(3.kg, 4.kg), g) should equal(7000.g)
  }
}
