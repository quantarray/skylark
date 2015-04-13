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

package com.quantarray.skylark.measure.substance.commodities

import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.substance.StandardSubstance

/**
 * Physical substances.
 *
 * @author Araik Grigoryan
 */
trait PhysicalSubstances
{
  val ng = NaturalGasSubstance("NG", 0.7 * (kg / (m ^ 3)))

  val wti = PetroleumSubstance("WTI", 800 * (kg / m3))

  val cotton = StandardSubstance("Cotton", 1.54 * (g / (cm ^ 3)))
}
