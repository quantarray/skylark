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

package com.quantarray.skylark.measure

/**
  * EnergyPrice / () converter.
  *
  * @author Araik Grigoryan
  */
trait EnergyPricePerDimensionlessConverter extends SameMeasureConverter[RatioMeasure[RatioMeasure[Currency, EnergyMeasure], DimensionlessMeasure]]
{
  type From = RatioMeasure[RatioMeasure[Currency, EnergyMeasure], DimensionlessMeasure]

  type To = RatioMeasure[RatioMeasure[Currency, EnergyMeasure], DimensionlessMeasure]

  implicit val cc1: CanConvert[RatioMeasure[Currency, EnergyMeasure], RatioMeasure[Currency, EnergyMeasure]]

  protected override def convert(from: From, to: To): Option[Double] = Some(to.denominator.immediateBase / from.denominator.immediateBase)
}

object EnergyPricePerDimensionlessConverter
{
  def apply(cc: CanConvert[RatioMeasure[Currency, EnergyMeasure], RatioMeasure[Currency, EnergyMeasure]]): EnergyPricePerDimensionlessConverter =
  {
    val params = cc

    new EnergyPricePerDimensionlessConverter
    {
      implicit val cc1: CanConvert[RatioMeasure[Currency, EnergyMeasure], RatioMeasure[Currency, EnergyMeasure]] = params
    }
  }
}