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
 * Force measure.
 *
 * @author Araik Grigoryan
 */
case class ForceMeasure(name: String, system: SystemOfUnits, declMultBase: Option[(Double, Measure)]) extends Measure with MeasureCanBecomeAsset
{
  type D = Force.type

  type Repr = ForceMeasure

  def dimension = Force

  override protected[measure] def build(name: String, mb: (Double, Measure)): Repr = ForceMeasure(name, system, Some(mb))

  override def toString = name
}

object ForceMeasure
{
  def apply(name: String, system: SystemOfUnits) = new ForceMeasure(name, system, None)

  def apply(name: String, system: SystemOfUnits, dmb: (Double, Measure)): ForceMeasure = new ForceMeasure(name, system, Some(dmb))

  def apply(name: String, dmb: (Double, Measure)): ForceMeasure = apply(name, dmb._2.system, dmb)
}
