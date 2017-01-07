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

import com.quantarray.skylark.measure.measures._

/**
  * Energy converter.
  *
  * @author Araik Grigoryan
  */
trait EnergyConverter extends SameTypeConverter[EnergyMeasure]
{
  override protected def convert(from: EnergyMeasure, to: EnergyMeasure): Option[Double] = ⤇(from, to) match
  {
    case MMBtu ⤇ GJ => Some(1.055056)
    case _ => super.convert(from, to)
  }
}

object EnergyConverter
{
  def apply(): EnergyConverter = new EnergyConverter {}
}