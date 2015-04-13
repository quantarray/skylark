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

package com.quantarray.skylark.measure.market

import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.market.conversion.MarketConversionProvider

/**
 * Market convertible quantity.
 *
 * @author Araik Grigoryan
 */
object MarketConvertibleQuantity
{

  object Implicits
  {

    implicit final class QuantityToMarketConvertibleQuantity[M <: Measure](private val quantity: Quantity[M]) extends AnyVal
    {
      def +[N <: Measure](quantity: Quantity[N])(implicit market: Market, mcp: MarketConversionProvider): Quantity[M] =
      {
        import Quantity.Implicits._

        val sum = QuantityToMarketConvertibleQuantity.to(this.quantity, quantity)

        new Quantity(sum._1, sum._2)

      }

      def to[N <: Measure](measure: N)(implicit market: Market, mcp: MarketConversionProvider): Quantity[N] =
      {
        import Quantity.Implicits._

        val to = QuantityToMarketConvertibleQuantity.to((0, measure), this.quantity)

        new Quantity(to._1, to._2)
      }
    }

    object QuantityToMarketConvertibleQuantity
    {
      def to[M <: Measure, N <: Measure](target: (Double, M), source: (Double, N))
                                        (implicit market: Market, mcp: MarketConversionProvider): (Double, M) =
      {
        (target._2, source._2) match
        {
          case (thisMeasure, otherMeasure) if thisMeasure == otherMeasure => (target._1 + source._1, thisMeasure)
          case (thisMeasure: RatioMeasure, otherMeasure: RatioMeasure) =>

            val nFactor = mcp.factor(thisMeasure.numerator, otherMeasure.numerator, Some(market))
            val dFactor = mcp.factor(thisMeasure.denominator, otherMeasure.denominator, Some(market))

            (nFactor, dFactor) match
            {
              case (Some(n), Some(d)) => (target._1 + source._1 * n / d, thisMeasure.asInstanceOf[M])
              case (None, _) => throw new Exception(s"No conversion factor available between ${thisMeasure.numerator} and ${otherMeasure.numerator}.")
              case (_, None) => throw new Exception(s"No conversion factor available between ${thisMeasure.denominator} and ${otherMeasure.denominator}.")
            }
          case (thisMeasure, otherMeasure) =>

            mcp.factor(thisMeasure, otherMeasure, Some(market)) match
            {
              case Some(factor) => (target._1 + source._1 * factor, thisMeasure)
              case _ => throw new Exception(s"No conversion factor available between $thisMeasure and $otherMeasure.")
            }
          case _ => throw new Exception(s"No conversion factor available between ${target._2} and ${source._2}.")
        }
      }
    }

  }

}
