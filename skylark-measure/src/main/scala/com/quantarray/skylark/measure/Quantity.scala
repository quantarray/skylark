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
case class Quantity[N, M <: Measure[M]](override val value: N, override val measure: M)(implicit qn: QuasiNumeric[N]) extends AnyQuantity[N](value, measure)
{
  override def unary_-() = Quantity(qn.negate(value), measure)

  override def *(constant: Double) = Quantity(qn.timesConstant(value, constant), measure)

  override def /(constant: Double) = Quantity(qn.divideByConstant(value, constant), measure)

  /**
    * Adds another quantity. CanAdd instance allows addition of apples and oranges to obtain bananas.
    */
  def +[M2 <: Measure[M2]](quantity: Quantity[N, M2])
                          (implicit caq: CanAddQuantity[N, M, Quantity[N, M], M2, Quantity[N, M2], M],
                           cc1: CanConvert[M, M], cc2: CanConvert[M2, M]): caq.QR = caq.plus(this, quantity)

  /**
    * Subtracts another quantity. CanAdd instance allows addition of apples and oranges to obtain bananas.
    */
  def -[M2 <: Measure[M2]](quantity: Quantity[N, M2])
                          (implicit caq: CanAddQuantity[N, M, Quantity[N, M], M2, Quantity[N, M2], M],
                           cc1: CanConvert[M, M], cc2: CanConvert[M2, M]): caq.QR = caq.plus(this, -quantity)

  def /[M2 <: Measure[M2], R <: Measure[R]](quantity: Quantity[N, M2])(implicit cd: CanDivide[M, M2, R]): Quantity[N, R] =
    Quantity(qn.divide(value, quantity.value), measure / quantity.measure)

  def *[M2 <: Measure[M2], R <: Measure[R]](quantity: Quantity[N, M2])(implicit cm: CanMultiply[M, M2, R]): Quantity[N, R] =
    Quantity(qn.times(value, quantity.value), measure * quantity.measure)

  def ^[R <: Measure[R]](exponent: Double)(implicit ce: CanExponentiate[M, R]): Quantity[N, R] =
    Quantity(qn.pow(value, exponent), measure ^ exponent)

  def to[M2 <: Measure[M2]](target: M2)(implicit cc: CanConvert[M, M2]): Option[Quantity[N, M2]] =
    cc.convert(measure, target).map(cf => Quantity(qn.timesConstant(value, cf), target))

  def toOrElse[M2 <: Measure[M2], B >: Quantity[N, M2]](target: M2, default: B)(implicit cc: CanConvert[M, M2]): B =
    to(target).getOrElse(default)

  def simplify[R <: Measure[R]](implicit cs: CanSimplify[M, Option[R]]): Option[Quantity[N, R]] = measure.simplify[R].map(Quantity(value, _))

  override def toString = s"$value $measure"
}


