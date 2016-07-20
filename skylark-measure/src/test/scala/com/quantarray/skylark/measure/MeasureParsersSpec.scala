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

import com.quantarray.skylark.measure.arithmetic.default._
import org.scalatest.{FlatSpec, Matchers}

class MeasureParsersSpec extends FlatSpec with Matchers with MeasureParsers
{
  final val measureProvider = new MeasureProvider
  {

    override def read: MeasureReader = new MeasureReader
    {
      override def apply(name: String): Option[untyped.Measure] = name match
      {
        case "USD" => Some(USD)
        case "bbl" => Some(bbl)
        case "m" => Some(m)
        case "s" => Some(s)
        case "kg" => Some(kg)
      }
    }
  }

  "USD" should "be parsable" in
    {
      parseMeasure("USD").get should equal(USD)
    }

  "USD / bbl" should "be parsable" in
    {
      parseMeasure("USD / bbl").get should equal(USD / bbl)
    }

  "(m / s) ^ 3" should "be parsable" in
    {
      parseMeasure("(m / s) ^ 3").get should equal((m / s) ^ 3)
    }

  "kg * ((m / s) ^ 2)" should "be parsable" in
    {
      parseMeasure("kg * ((m / s) ^ 2)").get should equal(kg * ((m / s) ^ 2))
    }

  "XYZ" should "not be parsable" in
    {
      intercept[MatchError]
      {
        parseMeasure("XYZ")
      }
    }
}
