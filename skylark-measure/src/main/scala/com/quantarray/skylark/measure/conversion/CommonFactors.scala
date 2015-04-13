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

import com.quantarray.skylark.measure._

/**
 * Common factors.
 *
 * @author Araik Grigoryan
 */
trait CommonFactors
{
  // Length
  val InchPerMeter = 39.3701

  // Mass
  val OuncePerGram = 0.035274

  // Fluid Volume
  val GallonPerBarrel = 31.5 // If wondering why this is not 42, you have oil on your mind, which is special. Look in [[PetroleumSubstance]].

  /**
   * Gets a factor for converting between measures of the same dimension residing in different systems.
   */
  def crossSystemFactor(numerator: Measure, denominator: Measure): Option[Double] =
  {
    (numerator, denominator) match
    {
      // Length
      case (`in`, `m`) => Some(InchPerMeter)
      case (`m`, `in`) => Some(1 / InchPerMeter)

      // Mass
      case (`oz`, `g`) => Some(OuncePerGram)
      case (`g`, `oz`) => Some(1 / OuncePerGram)

      // Fluid Volume
      case (`gal`, `bbl`) => Some(GallonPerBarrel)
      case (`bbl`, `gal`) => Some(1 / GallonPerBarrel)

      case _ => None
    }
  }

  def crossSystemFunction(source: Measure, target: Measure): Double => Option[Double] =
  {
    (source, target) match
    {
      //      case (C_°, F_°) => (c: Double) => Some(9 * c / 5 + 32)
      //      case (F_°, C_°) => (f: Double) => Some((f - 32) * 5 / 9)
      //      case (C_°, K_°) => (c: Double) => Some(c + 273.15)
      //      case (K_°, C_°) => (k: Double) => Some(k - 273.15)
      case _ => (x: Double) => None
    }
  }
}
