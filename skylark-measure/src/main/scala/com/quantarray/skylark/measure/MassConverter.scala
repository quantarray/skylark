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

import com.quantarray.skylark.measure.measures._

/**
  * Mass converter.
  *
  * @author Araik Grigoryan
  */
trait MassConverter extends SameMeasureConverter[MassMeasure]
{
  protected override def convert(from: MassMeasure, to: MassMeasure): Option[Double] = ⤇(from, to) match
  {
    case `kg` ⤇ `lb` => Some(2.204625)
    case _ => super.convert(from, to)
  }
}

object MassConverter
{
  def apply(): MassConverter = new MassConverter {}
}