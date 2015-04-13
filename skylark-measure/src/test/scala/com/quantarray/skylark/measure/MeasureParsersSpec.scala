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

import com.quantarray.skylark.measure.substance.commodities.PhysicalSubstances
import org.scalatest.{FlatSpec, Matchers}

/**
 * Measure parsers spec.
 *
 * @author Araik Grigoryan
 */
class MeasureParsersSpec extends FlatSpec with Matchers with MeasureParsers with PhysicalSubstances
{
  implicit final val measureProvider = new PhysicalMeasureProvider

  implicit final val assetProvider = new PhysicalAssetProvider

  "USD" should "be parsable" in
    {
      parseAll(measureExpression, "USD").get should be(USD)
    }

  "USD / bbl" should "be parsable" in
    {
      parseAll(measureExpression, "USD / bbl").get should be(USD / bbl)
    }

  "USD * (MMBtu / bbl) ^ 3" should "be parsable" in
    {
      parseAll(measureExpression, "USD * (MMBtu / bbl) ^ 3").get should be(USD * ((MMBtu / bbl) ^ 3))
    }

  "((USD * (MMBtu / bbl) ^ 3) ^ 2) / MMBtu" should "be parsable" in
    {
      parseAll(measureExpression, "((USD * (MMBtu / bbl) ^ 3) ^ 2) / MMBtu").get should be(((USD * ((MMBtu / bbl) ^ 3)) ^ 2) / MMBtu)
    }

  "bbl of WTI" should "be parsable" in
    {
      parseAll(measureExpression, "bbl of WTI").get should be(bbl of wti)
    }

  "XYZ" should "not be parsable" in
    {
      intercept[MatchError]
      {
        parseAll(measureExpression, "XYZ")
      }
    }
}
