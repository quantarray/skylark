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

package com.quantarray.skylark.measure.untyped

/**
 * Untyped dimension.
 *
 * @author Araik Grigoryan
 */
trait Dimension extends Product with Serializable

trait ProductDimension extends Dimension
{
  def multiplicand: Dimension

  def multiplier: Dimension
}

object ProductDimension
{
  def apply(multiplicand: Dimension, multiplier: Dimension): ProductDimension =
  {
    val params = (multiplicand, multiplier)

    new ProductDimension
    {
      val multiplicand: Dimension = params._1

      val multiplier: Dimension = params._2

      private val productElements = Seq(multiplicand, multiplier)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[ProductDimension]

      override def toString: String = s"$multiplicand * $multiplier"
    }
  }
}

trait RatioDimension extends Dimension
{
  def numerator: Dimension

  def denominator: Dimension
}

object RatioDimension
{
  def apply(numerator: Dimension, denominator: Dimension): RatioDimension =
  {
    val params = (numerator, denominator)

    new RatioDimension
    {
      val numerator: Dimension = params._1

      val denominator: Dimension = params._2

      private val productElements = Seq(numerator, denominator)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[RatioDimension]

      override def toString: String = s"$numerator / $denominator"
    }
  }
}

trait ExponentialDimension extends Dimension
{
  def base: Dimension

  def exponent: Double
}

object ExponentialDimension
{
  def apply(base: Dimension, exponent: Double): ExponentialDimension =
  {
    val params = (base, exponent)

    new ExponentialDimension
    {
      val base: Dimension = params._1

      val exponent: Double = params._2

      private val productElements = Seq(base, exponent)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[ExponentialDimension]

      override def toString: String = s"$base ^ $exponent"
    }
  }
}