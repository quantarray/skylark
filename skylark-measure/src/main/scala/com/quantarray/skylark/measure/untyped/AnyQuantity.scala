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

import com.quantarray.skylark.measure._

/**
  * Any quantity.
  *
  * @author Araik Grigoryan
  */
abstract class AnyQuantity[N] extends Product with Serializable
{
  implicit val qn: QuasiNumeric[N]

  def value: N

  def measure: AnyMeasure

  def unary_-() = AnyQuantity(qn.negate(value), measure)

  def *(constant: Double): AnyQuantity[N]

  def *(quantity: AnyQuantity[N])(implicit cm: CanMultiply[AnyMeasure, AnyMeasure, AnyMeasure]): AnyQuantity[N] =
    AnyQuantity(qn.times(value, quantity.value), measure * quantity.measure)

  def /(constant: Double): AnyQuantity[N]

  def /(quantity: AnyQuantity[N])(implicit cd: CanDivide[AnyMeasure, AnyMeasure, AnyMeasure]): AnyQuantity[N] =
    AnyQuantity(qn.divide(value, quantity.value), measure / quantity.measure)

  def +(quantity: AnyQuantity[N])(implicit caq: CanAddAnyQuantity[N, AnyQuantity[N], AnyQuantity[N]], cc: CanConvert[AnyMeasure, AnyMeasure]): caq.QR =
    caq.plus(this, quantity)

  def -(quantity: AnyQuantity[N])(implicit caq: CanAddAnyQuantity[N, AnyQuantity[N], AnyQuantity[N]], cc: CanConvert[AnyMeasure, AnyMeasure]): caq.QR =
    caq.plus(this, -quantity)

  def to(target: AnyMeasure)(implicit cc: CanConvert[AnyMeasure, AnyMeasure]): Option[AnyQuantity[N]] =
    cc.convert(measure, target).map(cf => AnyQuantity(qn.timesConstant(value, cf), target))

  def toOrElse[B >: AnyQuantity[N]](target: AnyMeasure, default: B)(implicit cc: CanConvert[AnyMeasure, AnyMeasure]): B = to(target).getOrElse(default)

  def simplify(implicit cs: CanSimplify[AnyMeasure, AnyMeasure]): AnyQuantity[N] = AnyQuantity(value, measure.simplify)
}

object AnyQuantity
{
  def apply[N](value: N, measure: AnyMeasure)(implicit qn: QuasiNumeric[N]): AnyQuantity[N] =
  {
    val params = (value, measure, qn)

    new AnyQuantity[N]
    {
      val value: N = params._1

      val measure: AnyMeasure = params._2

      implicit val qn: QuasiNumeric[N] = params._3

      override def *(constant: Double): AnyQuantity[N] = AnyQuantity(qn.timesConstant(value, constant), measure)

      override def /(constant: Double): AnyQuantity[N] = AnyQuantity(qn.divideByConstant(value, constant), measure)

      private val productElements = Seq(value, measure)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[AnyQuantity[_]]

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: AnyQuantity[_] => canEqual(that) && this.value == that.value && this.measure == that.measure
        case _ => false
      }

      override def hashCode(): Int = 41 * value.hashCode() + measure.hashCode()

      override def toString: String = s"$value $measure"
    }
  }
}
