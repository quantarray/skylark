/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2017 Quantarray, LLC
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
import com.quantarray.skylark.measure.measures._
import org.scalatest.{FlatSpec, Matchers}

class AnyMeasureParsersSpec extends FlatSpec with Matchers with AnyMeasureParsers
{
  val measureAtoms: Map[String, AnyMeasure] = Seq(USD, bbl, m, s, kg).map(measure => measure.name -> measure).toMap

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
