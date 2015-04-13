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
trait Dimension
{
  def *(dimension: Dimension) = ProductDimension(this, dimension)

  def /(dimension: Dimension) = RatioDimension(this, dimension)

  def ^(exponent: Double) = ExponentialDimension(this, exponent)
}

sealed case class ProductDimension(multiplicand: Dimension, multiplier: Dimension) extends Dimension
{
  override def toString = s"$multiplicand * $multiplier"
}

sealed case class RatioDimension(numerator: Dimension, denominator: Dimension) extends Dimension
{
  override def toString = s"$numerator / $denominator"
}

sealed case class ExponentialDimension[B <: Dimension](base: B, exponent: Double) extends Dimension
{
  override def toString = s"$base ^ $exponent"
}

