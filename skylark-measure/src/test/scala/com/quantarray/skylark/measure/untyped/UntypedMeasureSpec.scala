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

package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.implicits._
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

class UntypedMeasureSpec extends FlatSpec with Matchers
{

  import com.quantarray.skylark.measure.commodity.us.commercial.grains.corn.shelled.{bushel => cbu}

  private val numericTolerance = 0.00000000001

  def to(source: untyped.Measure, target: untyped.Measure): Option[Double] =
  {
    import com.quantarray.skylark.measure.untyped.conversion.default._

    source.to(target)
  }

  def simplify(measure: untyped.Measure): untyped.Measure =
  {
    import com.quantarray.skylark.measure.untyped.simplification.default._

    measure.simplify
  }

  "Untyped measure" should "exponentiate" in
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
      to(mt * (USD / cbu), USD).value should equal(39.36830357142857 +- numericTolerance)
    }

  it should "simplify" in
    {
      simplify(kg * Unit) should be(kg)
      simplify(((bbl ^ 2) / Unit) * ((USD / bbl) ^ 2)) should be(USD ^ 2)
      simplify((USD / bp) * bp * (USD ^ -1.0)) should be(Unit)
    }
}
