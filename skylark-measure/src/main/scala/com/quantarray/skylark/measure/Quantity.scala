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

package com.quantarray.skylark.measure

import scala.language.implicitConversions

/**
 * Quantity.
 *
 * @author Araik Grigoryan
 */
case class Quantity[M <: Measure[M]](value: Double, measure: M)
{
  def unary_-() = Quantity(-value, measure)

  def *(constant: Double) = Quantity(value * constant, measure)

  def /(constant: Double) = Quantity(value / constant, measure)

  def +(constant: Double) = Quantity(value + constant, measure)

  def -(constant: Double) = Quantity(value - constant, measure)

  def /[M2 <: Measure[M2], R <: Measure[R]](quantity: Quantity[M2])(implicit cd: CanDivide[M, M2, R]): Quantity[R] =
    Quantity(value / quantity.value * cd.unit(measure, quantity.measure), measure / quantity.measure)

  def *[M2 <: Measure[M2], R <: Measure[R]](quantity: Quantity[M2])(implicit cm: CanMultiply[M, M2, R]): Quantity[R] =
    Quantity(value * quantity.value * cm.unit(measure, quantity.measure), measure * quantity.measure)

  def ^[R <: Measure[R]](exponent: Double)(implicit ce: CanExponentiate[M, R]): Quantity[R] = Quantity(math.pow(value, exponent), measure ^ exponent)

  def to[M2 <: Measure[M2]](to: M2)(implicit cc: CanConvert[M, M2]): Quantity[M2] = Quantity(value * cc.convert(measure, to).getOrElse(1.0), to)

  override def toString = s"$value $measure"
}

