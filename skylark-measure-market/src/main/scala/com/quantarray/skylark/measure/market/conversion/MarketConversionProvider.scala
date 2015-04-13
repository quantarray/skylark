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

package com.quantarray.skylark.measure.market.conversion

import com.quantarray.skylark.measure.market.Market
import com.quantarray.skylark.measure.{Dimension, Measure}

/**
 * Conversion provider taking into account substance and spacetime.
 *
 * @author Araik Grigoryan
 */
trait MarketConversionProvider
{
  /**
   * Gets factor in the form numerator/denominator, such that if you multiply the factor by units of denominator,
   * the resulting units are the units of the numerator (i.e. denominator units cancel out).
   */
  def factor(numerator: Measure, denominator: Measure, market: Option[Market] = None): Option[Double]
}
