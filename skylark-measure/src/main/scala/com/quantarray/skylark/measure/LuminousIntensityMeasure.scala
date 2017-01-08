/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2017 Quantarray, LLC
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
  * Luminous intensity measure.
  *
  * @author Araik Grigoryan
  */
case class LuminousIntensityMeasure(name: String, system: SystemOfUnits, base: Option[(LuminousIntensityMeasure, Double)] = None) extends Measure[LuminousIntensityMeasure]
{
  type D = LuminousIntensityDimension

  val dimension = LuminousIntensity

  override def composes(name: String, system: SystemOfUnits, multiple: Double): LuminousIntensityMeasure = LuminousIntensityMeasure(name, system, Some(this, multiple))

  override def toString = name
}
