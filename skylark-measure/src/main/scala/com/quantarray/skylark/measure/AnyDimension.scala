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

/**
 * Any dimension.
 *
 * @author Araik Grigoryan
 */
trait AnyDimension extends Product with Serializable

trait AnyProductDimension extends AnyDimension
{
  def multiplicand: AnyDimension

  def multiplier: AnyDimension
}

object AnyProductDimension
{
  def apply(multiplicand: AnyDimension, multiplier: AnyDimension): AnyProductDimension =
  {
    val params = (multiplicand, multiplier)

    new AnyProductDimension
    {
      val multiplicand: AnyDimension = params._1

      val multiplier: AnyDimension = params._2

      private val productElements = Seq(multiplicand, multiplier)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[AnyProductDimension]

      override def toString: String = s"$multiplicand * $multiplier"
    }
  }
}

trait AnyRatioDimension extends AnyDimension
{
  def numerator: AnyDimension

  def denominator: AnyDimension
}

object AnyRatioDimension
{
  def apply(numerator: AnyDimension, denominator: AnyDimension): AnyRatioDimension =
  {
    val params = (numerator, denominator)

    new AnyRatioDimension
    {
      val numerator: AnyDimension = params._1

      val denominator: AnyDimension = params._2

      private val productElements = Seq(numerator, denominator)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[AnyRatioDimension]

      override def toString: String = s"$numerator / $denominator"
    }
  }
}

trait AnyExponentialDimension extends AnyDimension
{
  def base: AnyDimension

  def exponent: Double
}

object AnyExponentialDimension
{
  def apply(base: AnyDimension, exponent: Double): AnyExponentialDimension =
  {
    val params = (base, exponent)

    new AnyExponentialDimension
    {
      val base: AnyDimension = params._1

      val exponent: Double = params._2

      private val productElements = Seq(base, exponent)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[AnyExponentialDimension]

      override def toString: String = s"$base ^ $exponent"
    }
  }
}