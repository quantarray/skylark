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

import scala.language.implicitConversions

/**
  * Quantity.
  *
  * @author Araik Grigoryan
  */
case class Quantity[N, M](value: N, measure: M)(implicit qn: QuasiNumeric[N])
{
  def unary_-() = Quantity(qn.negate(value), measure)

  def *(constant: Double) = Quantity(qn.timesConstant(value, constant), measure)

  def /(constant: Double) = Quantity(qn.divideByConstant(value, constant), measure)

  /**
    * Adds another quantity. CanAddQuantity instance allows addition of apples and oranges to obtain bananas.
    */
  def +[M2](quantity: Quantity[N, M2])
           (implicit caq: CanAddQuantity[N, M, Quantity, M2, Quantity, M],
            cc1: CanConvert[M, M], cc2: CanConvert[M2, M]): caq.QR = caq.plus(this, quantity)

  /**
    * Subtracts another quantity. CanAddQuantity instance allows addition of apples and oranges to obtain bananas.
    */
  def -[M2](quantity: Quantity[N, M2])
           (implicit caq: CanAddQuantity[N, M, Quantity, M2, Quantity, M],
            cc1: CanConvert[M, M], cc2: CanConvert[M2, M]): caq.QR = caq.plus(this, -quantity)

  /**
    * Divides by another quantity.
    */
  def /[M2, R](quantity: Quantity[N, M2])(implicit cdq: CanDivideQuantity[N, M, Quantity, M2, Quantity, R]): cdq.QR = cdq.divideQuantity(this, quantity)

  /**
    * Multiplies by another quantity.
    */
  def *[M2, R](quantity: Quantity[N, M2])(implicit cmq: CanMultiplyQuantity[N, M, Quantity, M2, Quantity, R]): cmq.QR = cmq.timesQuantity(this, quantity)

  /**
    * Raises this quantity to expoenent.
    */
  def ^[R](exponent: Double)(implicit ceq: CanExponentiateQuantity[N, M, Quantity, R]): ceq.QR = ceq.powQuantity(this, exponent)

  /**
    * Converts this quantity to another.
    */
  def to[M2](target: M2)(implicit cc: CanConvert[M, M2]): Option[Quantity[N, M2]] = cc.convert(measure, target).map(cf => Quantity(qn.timesConstant(value, cf), target))

  /**
    * Convert this quantity to another or default.
    */
  @inline
  def toOrElse[M2, B >: Quantity[N, M2]](target: M2, default: B)(implicit cc: CanConvert[M, M2]): B = to(target).getOrElse(default)

  def simplify[R](implicit csq: CanSimplifyQuantity[N, M, Quantity, R]): csq.QR = csq.simplifyQuantity(this)

  override def toString = s"$value $measure"
}


