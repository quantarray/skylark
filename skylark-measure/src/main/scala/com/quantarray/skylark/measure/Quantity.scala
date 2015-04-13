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
case class Quantity[M <: Measure](value: Double, measure: M)
{
  def unary_-() = Quantity(-value, measure)

  def *(constant: Double) = Quantity(value * constant, measure)

  def /(constant: Double) = Quantity(value / constant, measure)

  def +(constant: Double) = Quantity(value + constant, measure)

  def -(constant: Double) = Quantity(value - constant, measure)

  def *[N <: Measure](quantity: Quantity[N]): Quantity[ProductMeasure] =
  {
    quantity.measure.dimension match
    {
      case _: NoDimension.type => Quantity(value * quantity.value * quantity.measure.multBaseValue, measure * UnitMeasure)
      case _ => Quantity(value * quantity.value, measure.*(quantity.measure))
    }
  }

  override def toString = s"$value $measure"
}

object Quantity
{

  object Implicits
  {
    implicit def quantityToTuple2[M <: Measure](quantity: Quantity[M]): (Double, M) = (quantity.value, quantity.measure)
  }

}
