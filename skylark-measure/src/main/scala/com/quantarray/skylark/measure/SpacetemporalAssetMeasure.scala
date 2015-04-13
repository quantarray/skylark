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
 * Spacetemporal asset measure.
 *
 * @author Araik Grigoryan
 */
case class SpacetemporalAssetMeasure[A <: Asset](measure: Measure, asset: A, spacetime: SpacetimeLike) extends AssetMeasureLike[A]
{
  type D = measure.D

  type Repr = SpacetemporalAssetMeasure[A]

  override def dimension: D = measure.dimension

  override protected[measure] def build(name: String, mb: (Double, Measure)): Repr = SpacetemporalAssetMeasure(measure.build(name, mb), asset, spacetime)

  override def toString: String = s"$measure of $asset at $spacetime"
}
