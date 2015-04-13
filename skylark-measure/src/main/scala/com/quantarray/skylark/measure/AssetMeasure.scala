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

import com.quantarray.skylark.measure.conversion.AssetConversionProvider

/**
 * Asset measure.
 *
 * @author Araik Grigoryan
 */
case class AssetMeasure[A <: Asset](measure: Measure, asset: A) extends AssetMeasureLike[A] with MeasureCanBecomeSpacetemporalAssetFromAsset[A]
{
  type D = measure.D

  type Repr = AssetMeasure[A]

  override def dimension: D = measure.dimension

  def to(measure: AssetMeasure[A])(implicit conversion: AssetConversionProvider): Option[Double] = conversion.factor(measure, this)

  override protected[measure] def build(name: String, mb: (Double, Measure)): Repr = AssetMeasure(measure.build(name, mb), asset)

  override def toString = s"$measure of $asset"
}