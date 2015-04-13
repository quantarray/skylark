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

import com.quantarray.skylark.measure.conversion.ConstantConversionProvider

/**
 * Constant-convertible quantity.
 *
 * @author Araik Grigoryan
 */
object ConstantConvertibleQuantity
{

  object Implicits
  {

    implicit final class QuantityToConstantConvertibleQuantity[M <: Measure](private val quantity: Quantity[M]) extends AnyVal
    {
      def to(measure: Measure)(implicit conversion: ConstantConversionProvider): Quantity[Measure] =
      {
        quantity.measure.to(measure) match
        {
          case Some(factor) => Quantity(quantity.value * factor, measure)
          case _ => throw new Exception(s"No conversion factor available between ${quantity.measure} and $measure.")
        }
      }

    }

  }

}
