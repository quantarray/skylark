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

package com.quantarray.skylark.measure.market

import com.quantarray.skylark.measure.{Dimension, Measure, Quantity}
import org.joda.time.DateTime

/**
 * Discrete forward curve.
 *
 * @author Araik Grigoryan
 */
case class DiscreteForwardCurve[M <: Measure](measure: M, points: Seq[(DateTime, Double)]) extends TermPriceCurve[M] with PartialFunction[DateTime, Double]
{
  lazy val values = points.toMap

  /**
   * Gets value portion of quantity at key.
   */
  override def value(key: DateTime): Option[Double] = values.get(key)

  override def isDefinedAt(key: DateTime): Boolean = values.isDefinedAt(key)

  override def apply(key: DateTime): Double = values(key)
}

object DiscreteForwardCurve
{
  def flat[M <: Measure](quantity: Quantity[M], dates: Seq[DateTime]): DiscreteForwardCurve[M] =
    new DiscreteForwardCurve(quantity.measure, dates.map((_, quantity.value)))
}
