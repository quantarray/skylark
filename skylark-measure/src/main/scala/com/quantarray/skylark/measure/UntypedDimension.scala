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

/**
 * Untyped dimension.
 *
 * @author Araik Grigoryan
 */
trait UntypedDimension

trait ProductUntypedDimension extends UntypedDimension
{
  def multiplicand: UntypedDimension

  def multiplier: UntypedDimension
}

object ProductUntypedDimension
{
  def apply(multiplicand: UntypedDimension, multiplier: UntypedDimension): ProductUntypedDimension =
  {
    val params = (multiplicand, multiplier)

    new ProductUntypedDimension
    {
      val multiplicand: UntypedDimension = params._1

      val multiplier: UntypedDimension = params._2

      override def toString: String = s"$multiplicand * $multiplier"
    }
  }
}

trait RatioUntypedDimension extends UntypedDimension
{
  def numerator: UntypedDimension

  def denominator: UntypedDimension
}

object RatioUntypedDimension
{
  def apply(numerator: UntypedDimension, denominator: UntypedDimension): RatioUntypedDimension =
  {
    val params = (numerator, denominator)

    new RatioUntypedDimension
    {
      val numerator: UntypedDimension = params._1

      val denominator: UntypedDimension = params._2

      override def toString: String = s"$numerator / $denominator"
    }
  }
}

trait ExponentialUntypedDimension extends UntypedDimension
{
  def base: UntypedDimension

  def exponent: Double
}

object ExponentialUntypedDimension
{
  def apply(base: UntypedDimension, exponent: Double): ExponentialUntypedDimension =
  {
    val params = (base, exponent)

    new ExponentialUntypedDimension
    {
      val base: UntypedDimension = params._1

      val exponent: Double = params._2

      override def toString: String = s"$base ^ $exponent"
    }
  }
}