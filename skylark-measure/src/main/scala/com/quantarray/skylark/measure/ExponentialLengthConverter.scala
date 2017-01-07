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
  * Length ^n^ converter.
  *
  * @author Araik Grigoryan
  */
trait ExponentialLengthConverter extends SameTypeConverter[ExponentialLengthMeasure]
{
  override protected def convert(from: ExponentialLengthMeasure, to: ExponentialLengthMeasure): Option[Double] = ⤇(from, to) match
  {
    case `gal` ⤇ `in3` => Some(231)
    case `ha` ⤇ `km2` => Some(0.01)
    case _ => super.convert(from, to)
  }
}

object ExponentialLengthConverter
{
  def apply(): ExponentialLengthConverter = new ExponentialLengthConverter {}
}