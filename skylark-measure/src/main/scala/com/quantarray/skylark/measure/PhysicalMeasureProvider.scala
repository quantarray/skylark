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
 * Physical measure provider.
 *
 * @author Araik Grigoryan
 */
class PhysicalMeasureProvider extends MeasureProvider
{
  lazy val measures = Seq(kg, lb, bbl, MMBtu, USD)

  lazy val map = measures.foldLeft(Map.empty[String, Measure])((mapSoFar, measure) => mapSoFar + (measure.name -> measure))

  override def measure(name: String) = map.get(name)
}
