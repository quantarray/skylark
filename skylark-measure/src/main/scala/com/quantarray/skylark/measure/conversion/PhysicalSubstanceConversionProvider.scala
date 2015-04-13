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

package com.quantarray.skylark.measure.conversion

import com.quantarray.skylark.measure.{Measure, Dimension}

/**
 * Physical substance conversion provider.
 *
 * @author Araik Grigoryan
 */
class PhysicalSubstanceConversionProvider(constantConversion: ConstantConversionProvider) extends AssetConversionProvider
{
  /**
   * Gets a factor for measures defined in two same or different dimensions.
   *
   * Gets factor in the form numerator/denominator, such that if you multiply the factor by units of denominator,
   * the resulting units are the units of the numerator (i.e. denominator units cancel out).
   *
   * Substance is optional because most conversion factors are not substance-specific. For the case when
   * conversion factors *are* substance specific (e.g. 1 barrel of oil = 42 gallons of oil), one should
   * supply the substance.
   */
  override def factor(numerator: Measure, denominator: Measure): Option[Double] = constantConversion.factor(numerator, denominator)
}
