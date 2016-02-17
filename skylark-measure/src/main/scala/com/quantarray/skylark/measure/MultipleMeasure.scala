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
  * Multiple measure.
  *
  * @author Araik Grigoryan
  */
trait MultipleMeasure[Self <: MultipleMeasure[Self]]
{
  self: Self with MultipleMeasure[Self] =>

  def base: Option[(Self, Double)]

  def toUltimateBase: Double =
  {
    def descend(m: Option[Self], multiple: Double): Double =
    {
      m.fold(multiple)(m1 => descend(m1.base.map(_._1), m1.base.map(_._2).getOrElse(1.0) * multiple))
    }

    descend(Some(this), 1.0)
  }
}
