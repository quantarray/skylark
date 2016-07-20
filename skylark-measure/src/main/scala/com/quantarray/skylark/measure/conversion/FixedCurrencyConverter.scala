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

package com.quantarray.skylark.measure.conversion

import com.quantarray.skylark.measure._

/**
  * Currency converter with fixed FX rate.
  *
  * @author Araik Grigoryan
  */
trait FixedCurrencyConverter extends SameMeasureConverter[Currency]
{
  protected override def convert(from: Currency, to: Currency): Option[Double] =
  {
    (from, to) match
    {
      case (USD, USC) => Some(100)
      case (USC, USD) => Some(1 / 100)
      case _ => super.convert(from, to)
    }
  }
}

object FixedCurrencyConverter
{
  def apply(): FixedCurrencyConverter = new FixedCurrencyConverter {}
}