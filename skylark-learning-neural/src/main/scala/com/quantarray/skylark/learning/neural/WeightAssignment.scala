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

package com.quantarray.skylark.learning.neural

import scala.util.Random

/**
 * Weight assignment function.
 *
 * @author Araik Grigoryan
 */
trait WeightAssignment extends ((Cell, Cell) => Double)

case object IndexedWeightAssignment extends WeightAssignment
{
  override def apply(source: Cell, target: Cell): Double = if (source.isBias) 0.1 * target.index else 0.1 * source.index + target.index
}

case object GaussianWeightAssignment extends WeightAssignment
{
  val random = new Random()

  override def apply(source: Cell, target: Cell): Double =
  {
    val gaussian = random.nextGaussian()

    if (source.isBias) gaussian else gaussian / math.sqrt(source.layer.cells.size)
  }
}