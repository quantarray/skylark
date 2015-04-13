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

/**
 * Asset.
 *
 * http://en.wikipedia.org/wiki/Asset
 *
 * @author Araik Grigoryan
 */
trait Asset
{
  def name: String

  /**
   * Gets factor in the form numerator/denominator, such that if you multiply the factor by units of denominator,
   * the resulting units are the units of the numerator (i.e. denominator units cancel out).
   *
   * This is a substance-specific factor that most substance will not care to implement. In the specific case
   * that this does make sense (e.g. for converting a barrel of oil to gallons, as there are different nature
   * of barrels), the substance that represents the oil product should implement its own conversion factor.
   */
  def factor(numerator: Measure, denominator: Measure): Option[Double] = None

  /**
   * Gets factor in the form numerator/denominator, such that if you multiply the factor by units of denominator,
   * the resulting units are the units of the numerator (i.e. denominator units cancel out).
   *
   * This is a asset-specific factor that some assets may not care to implement. In the specific case
   * that this does make sense (e.g. for converting a barrel of <b>oil</b> to gallons, as there are different kind
   * of barrels), the asset that represents the oil product should implement its own conversion factor.
   */
  def toFactor(numerator: Measure, denominator: Measure)(d: Double): Option[Double] = None
}
