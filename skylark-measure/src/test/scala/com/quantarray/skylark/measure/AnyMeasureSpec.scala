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

import com.quantarray.skylark.measure.any.implicits._
import com.quantarray.skylark.measure.measures.any._
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class AnyMeasureSpec extends FlatSpec with Matchers
{

  import com.quantarray.skylark.measure.commodity.us.commercial.grains.corn.shelled.{bushel => cbu}

  private val numericTolerance = 0.00000000001

  "AnyMeasure" should "exponentiate" in
    {
      kg ^ 2 should be(kg ^ 2)
    }

  it should "multiply" in
    {
      kg * m should be(kg * m)
    }

  it should "divide" in
    {
      m /s should be(m / s)
    }

  it should "convert" in
    {
      (mt * (USD / cbu)).to(USD).value should equal(39.36830357142857 +- numericTolerance)
    }

  it should "simplify" in
    {
      (kg * Unit).simplify should be(kg)
      (((bbl ^ 2) / Unit) * ((USD / bbl) ^ 2)).simplify should be(USD ^ 2)
      ((USD / bp) * bp * (USD ^ -1.0)).simplify should be(Unit)
    }
}
