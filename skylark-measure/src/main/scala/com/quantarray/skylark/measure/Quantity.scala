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

import scala.language.implicitConversions

/**
  * Quantity.
  *
  * @author Araik Grigoryan
  */
case class Quantity[N, M <: Measure[M]](value: N, measure: M)(implicit qn: QuasiNumeric[N])
{
  def unary_-() = Quantity(qn.negate(value), measure)

  def *(constant: Double) = Quantity(qn.timesConstant(value, constant), measure)

  def /(constant: Double) = Quantity(qn.divideByConstant(value, constant), measure)

  def +(constant: N) = Quantity(qn.plus(value, constant), measure)

  def -(constant: N) = Quantity(qn.minus(value, constant), measure)

  def +[M2 <: Measure[M2]](quantity: Quantity[N, M2])(implicit ev: M =:= M2): Quantity[N, M] =
    Quantity(qn.plus(value, quantity.value), measure + quantity.measure)

  def -[M2 <: Measure[M2]](quantity: Quantity[N, M2])(implicit ev: M =:= M2): Quantity[N, M] =
    Quantity(qn.minus(value, quantity.value), measure - quantity.measure)

  def /[M2 <: Measure[M2], R <: Measure[R]](quantity: Quantity[N, M2])(implicit cd: CanDivide[M, M2, R]): Quantity[N, R] =
    Quantity(qn.divide(value, quantity.value), measure / quantity.measure)

  def *[M2 <: Measure[M2], R <: Measure[R]](quantity: Quantity[N, M2])(implicit cm: CanMultiply[M, M2, R]): Quantity[N, R] =
    Quantity(qn.times(value, quantity.value), measure * quantity.measure)

  def ^[R <: Measure[R]](exponent: Double)(implicit ce: CanExponentiate[M, R]): Quantity[N, R] =
    Quantity(qn.pow(value, exponent), measure ^ exponent)

  def to[M2 <: Measure[M2]](target: M2)(implicit cc: CanConvert[M, M2]): Quantity[N, M2] = cc.convert(measure, target) match
  {
    case Some(cf) => Quantity(qn.timesConstant(value, cf), target)
    case _ => throw new Exception(s"No conversion from [$measure] to [$target] available in $cc.")
  }

  def simplify[R <: Measure[R]](implicit cr: CanSimplify[M, R]): Option[Quantity[N, R]] = measure.simplify[R].map(Quantity(value, _))

  override def toString = s"$value $measure"
}


