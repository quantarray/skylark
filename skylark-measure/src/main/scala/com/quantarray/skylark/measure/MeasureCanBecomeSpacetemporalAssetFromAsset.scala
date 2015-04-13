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

import com.quantarray.skylark.measure.Spacetime.Implicits.SpaceToSpacetime
import org.joda.time.DateTime

/**
 * Indicative subtype of Measure that can become asset and spacetemporal.
 *
 * @author Araik Grigoryan
 */
trait MeasureCanBecomeSpacetemporalAssetFromAsset[A <: Asset]
{
  self: AssetMeasure[A] =>

  def at(spacetime: SpacetimeLike) = SpacetemporalAssetMeasure(self.measure, self.asset, spacetime)

  def at(space: Space) = SpacetemporalAssetMeasure(self.measure, self.asset, space.anytime)

  def on(space: Space) = at(space)

  def on(time: DateTime) = SpacetemporalAssetMeasure(self.measure, self.asset, Here at time)
}