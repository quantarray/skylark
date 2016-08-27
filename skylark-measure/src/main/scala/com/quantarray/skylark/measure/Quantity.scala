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
case class Quantity[N, M <: Measure[M]](value: N, measure: M)(implicit val qn: QuasiNumeric[N]) extends untyped.Quantity[N]
{
  def unary_-() = Quantity(qn.negate(value), measure)

  def *(constant: Double) = Quantity(qn.timesConstant(value, constant), measure)

  def /(constant: Double) = Quantity(qn.divideByConstant(value, constant), measure)

  def +(constant: N) = Quantity(qn.plus(value, constant), measure)

  def -(constant: N) = Quantity(qn.minus(value, constant), measure)

  def +[M2 <: Measure[M2]](quantity: Quantity[N, M2])(implicit cc: CanConvert[M2, M]): Option[Quantity[N, M]] =
    cc.convert(quantity.measure, measure).map(cf => Quantity(qn.plus(value, qn.timesConstant(quantity.value, cf)), measure))

  def -[M2 <: Measure[M2]](quantity: Quantity[N, M2])(implicit cc: CanConvert[M2, M]): Option[Quantity[N, M]] =
    cc.convert(quantity.measure, measure).map(cf => Quantity(qn.minus(value, qn.timesConstant(quantity.value, cf)), measure))

  def /[M2 <: Measure[M2], R <: Measure[R]](quantity: Quantity[N, M2])(implicit cd: CanDivide[M, M2, R]): Quantity[N, R] =
    Quantity(qn.divide(value, quantity.value), measure / quantity.measure)

  def *[M2 <: Measure[M2], R <: Measure[R]](quantity: Quantity[N, M2])(implicit cm: CanMultiply[M, M2, R]): Quantity[N, R] =
    Quantity(qn.times(value, quantity.value), measure * quantity.measure)

  def ^[R <: Measure[R]](exponent: Double)(implicit ce: CanExponentiate[M, R]): Quantity[N, R] =
    Quantity(qn.pow(value, exponent), measure ^ exponent)

  def to[M2 <: Measure[M2]](target: M2)(implicit cc: CanConvert[M, M2]): Option[Quantity[N, M2]] =
    cc.convert(measure, target).map(cf => Quantity(qn.timesConstant(value, cf), target))

  def toOrElse[M2 <: Measure[M2]](target: M2, default: Quantity[N, M2])(implicit cc: CanConvert[M, M2]): Quantity[N, M2] =
    to(target).getOrElse(default)

  def simplify[R <: Measure[R]](implicit cs: CanSimplify[M, Option[R]]): Option[Quantity[N, R]] = measure.simplify[R].map(Quantity(value, _))

  override def canEqual(that: Any): Boolean = that.isInstanceOf[untyped.Quantity[_]]

  override def equals(obj: scala.Any): Boolean = obj match
  {
    case that: untyped.Quantity[_] => canEqual(that) && this.value == that.value & this.measure == that.measure
    case _ => false
  }

  override def hashCode(): Int = 41 * value.hashCode() + measure.hashCode()

  override def toString = s"$value $measure"
}


