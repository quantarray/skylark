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

import com.quantarray.skylark.measure.{CanConvert, CanSimplify, QuasiNumeric}

/**
  * Quantity.
  *
  * @author Araik Grigoryan
  */
abstract class Quantity[N] extends Serializable
{
  implicit val qn: QuasiNumeric[N]

  def value: N

  def measure: Measure

  def *(constant: Double): Quantity[N]

  def /(constant: Double): Quantity[N]

  def +(constant: N): Quantity[N]

  def -(constant: N): Quantity[N]

  def to(target: Measure)(implicit cc: CanConvert[Measure, Measure]): Option[Quantity[N]] = cc.convert(measure, target).map(cf => Quantity(qn.timesConstant(value, cf), target))

  def toOrElse(target: Measure, default: Quantity[N])(implicit cc: CanConvert[Measure, Measure]): Quantity[N] = to(target).getOrElse(default)

  def simplify(implicit cs: CanSimplify[Measure, Measure]): Quantity[N] = Quantity(value, measure.simplify)
}

object Quantity
{
  def apply[N](value: N, measure: Measure)(implicit qn: QuasiNumeric[N]): Quantity[N] =
  {
    val params = (value, measure, qn)

    new Quantity[N]
    {
      val value: N = params._1

      val measure: Measure = params._2

      implicit val qn: QuasiNumeric[N] = params._3

      override def *(constant: Double): Quantity[N] = Quantity(qn.timesConstant(value, constant), measure)

      override def /(constant: Double): Quantity[N] = Quantity(qn.divideByConstant(value, constant), measure)

      override def +(constant: N): Quantity[N] = Quantity(qn.plus(value, constant), measure)

      override def -(constant: N): Quantity[N] = Quantity(qn.minus(value, constant), measure)

      override def toString: String = s"$value $measure"
    }
  }
}
