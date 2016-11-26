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

package com.quantarray.skylark

package object measure extends DefaultDimensions
{
  val * = ProductMeasure
  val / = RatioMeasure
  val ^ = ExponentialMeasure

  type ⤇[From, To] = Conversion[From, To]

  val ⤇ = Conversion

  object measures extends DefaultMeasures
  {
    measuresScope =>

    @AscribeAnyMeasure[DefaultMeasures](measuresScope)
    object any
  }

  object quantities extends DefaultMeasures
  {

    measuresScope =>

    @QuantifyMeasure[DefaultMeasures, Quantity[Double, _]](measuresScope)
    class QuantifiedMeasures(val value: Double)

    object any
    {

      @QuantifyAnyMeasure[DefaultMeasures, AnyQuantity[Double]](measuresScope)
      class QuantifiedAnyMeasures(val value: Double)

    }

  }

  type ExponentialLengthMeasure = ExponentialMeasure[LengthMeasure]

  type Price[M <: Measure[M], N <: Measure[N]] = RatioMeasure[M, N]

  type EnergyPrice = RatioMeasure[Currency, EnergyMeasure]

  type CurrencyPrice = RatioMeasure[Currency, Currency]

  object commodity
  {

    object us
    {

      import composition._

      object commercial
      {

        object grains
        {

          object corn
          {

            import com.quantarray.skylark.measure.measures._

            object shelled
            {
              val bushel: MassMeasure = "bushel" := 56 * lb
            }

          }

        }

      }

    }

  }

  object implicits extends AnyRef
                           with arithmetic.SafeArithmeticImplicits
                           with simplification.DefaultSimplificationImplicits
                           with conversion.DefaultConversionImplicits

}
