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

import scala.language.existentials

/**
 * Dimension.
 *
 * @author Araik Grigoryan
 */
trait Dimension[Self <: Dimension[Self]]
{
  self: Self =>

  def *[D <: Dimension[D]](dimension: D): ProductDimension[Self, D] = ProductDimension(this, dimension)

  def /[D <: Dimension[D]](dimension: D): RatioDimension[Self, D] = RatioDimension(this, dimension)

  def ^(exponent: Double): ExponentialDimension[Self] = ExponentialDimension(this, exponent)
}

sealed case class ProductDimension[D1 <: Dimension[D1], D2 <: Dimension[D2]](multiplicand: D1, multiplier: D2) extends Dimension[ProductDimension[D1, D2]]
{
  override def toString = s"$multiplicand * $multiplier"
}

sealed case class RatioDimension[D1 <: Dimension[D1], D2 <: Dimension[D2]](numerator: D1, denominator: D2) extends Dimension[RatioDimension[D1, D2]]
{
  override def toString = s"$numerator / $denominator"
}

sealed case class ExponentialDimension[B <: Dimension[B]](base: B, exponent: Double) extends Dimension[ExponentialDimension[B]]
{
  override def toString = s"$base ^ $exponent"
}

