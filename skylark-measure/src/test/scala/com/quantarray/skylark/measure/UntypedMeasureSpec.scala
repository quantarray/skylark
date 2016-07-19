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

import com.quantarray.skylark.measure.Implicits._
import org.scalatest.{FlatSpec, Matchers}

class UntypedMeasureSpec extends FlatSpec with Matchers
{

  def simplify(measure: untyped.Measure): untyped.Measure =
  {
    import com.quantarray.skylark.measure.untyped.simplification.Implicits._

    measure.simplify
  }

  def pow(measure: untyped.Measure, exponent: Double): untyped.Measure =
  {
    import com.quantarray.skylark.measure.untyped.Implicits._

    measure ^ exponent
  }

  def times(multiplicand: untyped.Measure, multiplier: untyped.Measure): untyped.Measure =
  {
    import com.quantarray.skylark.measure.untyped.Implicits._

    multiplicand * multiplier
  }

  def divide(numerator: untyped.Measure, denominator: untyped.Measure): untyped.Measure =
  {
    import com.quantarray.skylark.measure.untyped.Implicits._

    numerator / denominator
  }

  "Untyped measure" should "exponentiate" in
    {
      pow(kg, 2) should be(kg ^ 2)
    }

  it should "multiply" in
    {
      times(kg, m) should be(kg * m)
    }

  it should "divide" in
    {
      divide(m, s) should be(m / s)
    }

  it should "simplify" in
    {
      simplify(kg * Unit) should be(kg)
    }
}
