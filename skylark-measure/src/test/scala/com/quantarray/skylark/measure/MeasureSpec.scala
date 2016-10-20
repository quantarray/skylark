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

import com.quantarray.skylark.measure.arithmetic.default._
import com.quantarray.skylark.measure.conversion.default._
import org.scalatest.{FlatSpec, Matchers}

/**
  * Measure spec.
  *
  * @author Araik Grigoryan
  */
class MeasureSpec extends FlatSpec with Matchers
{
  "kg" should "have expected properties" in
    {
      kg.name should be("kg")
      kg.dimension should be(Mass)
      kg.system should be(SI)
      kg.isStructuralAtom should be(right = true)
      kg.exponent should be(1.0)
      kg / lb should be(Unit)
      kg * s should be(ProductMeasure(kg, s))
      kg.inverse should be(ExponentialMeasure(kg, -1.0))
      kg to kg should be(Some(1))
      kg to lb should be(Some(2.204625))
      kg to g should be(Some(1000))
      kg to cg should be(Some(100000))
      kg to oz_metric should be(None) // Default conversion is not guaranteed to exist
    }

  it should "add and subtract" in
  {
    kg + g should be(kg)
    g + kg should be(g)

    kg - g should be(kg)
    g - kg should be(g)
  }

  "mi / h" should "be convertible to m/s" in
    {
      (mi / h) to (m / s) should be(Some(0.4470388888888889))
    }

  it should "be collectible" in
    {
      import untyped._

      (kg * m / (s ^ 2)).collect({ case (x * _) / _ => x }) should be(kg)
    }

  "(bbl ^ 2) / USD" should "be convertible to string" in
    {
      val measure = (bbl ^ 2) / USD
      measure.toString should be("(bbl ^ 2.0) / USD")
    }

  "USD" should "be convertible to USC" in
    {
      USD to USC should be(Some(100))
    }
}
