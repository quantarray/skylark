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
      kg.dimension should be(Mass())
      kg.system should be(SI)
      kg.isStructuralAtom should be(right = true)
      kg.exponent should be(1.0)
      kg / lb should be(UnitMeasure())
      kg * s should be(ProductMeasure(kg, s))
      kg.inverse should be(ExponentialMeasure(kg, -1.0))
      kg to kg should be(Some(1))
      kg to lb should be(Some(2.204625))
      kg to g should be(Some(1000))
    }

  "CanConvert" should "allow flexible units" in
    {
      case class TradeProvider()
      {
        def trade[M <: Measure[M], N <: Measure[N]](quantity: Quantity[M], price: Quantity[Price[N]])
                                                   (implicit cc: CanConvert[M, N]): Unit =
        {

        }
      }

      val provider = TradeProvider()

      provider.trade(10.0.MMBtu, Quantity(2.0, CAD / GJ))
    }
}
