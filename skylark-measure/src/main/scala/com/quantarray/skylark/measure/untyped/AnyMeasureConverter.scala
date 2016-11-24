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

package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure.SameTypeConverter

/**
  * Any measure converter.
  *
  * @author Araik Grigoryan
  */
trait AnyMeasureConverter extends SameTypeConverter[AnyMeasure]
{
  protected override def convert(from: AnyMeasure, to: AnyMeasure): Option[Double] =
  {
    if (from == to)
    {
      Some(1.0)
    }
    else
    {
      (from.ultimateBase, to.ultimateBase) match
      {
        case (Some(f), Some(t)) if from.system == to.system && f._1 == t._1 => Some(f._2 / t._2)
        case _ => super.convert(from, to)
      }
    }
  }
}

