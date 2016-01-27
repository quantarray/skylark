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

import com.quantarray.skylark.measure.{Derived, Hybrid, SystemOfUnits}

import scala.language.dynamics

/**
  * Untyped measure.
  *
  * @author Araik Grigoryan
  */
trait UntypedMeasure extends Dynamic
{
  self =>

  /**
    * Gets dimension of this measure.
    */
  def dimension: Dimension

  /**
    * Gets system of units.
    */
  def system: SystemOfUnits

  /**
    * Determines if this measure can be decomposed into constituent measures.
    */
  val isStructuralAtom: Boolean = true

  /**
    * Gets exponent of this measure.
    */
  def exponent: Double = 1.0

  @inline
  def collect[B](pf: PartialFunction[UntypedMeasure, B]): B = pf(this)
}

trait UntypedProductMeasure extends UntypedMeasure
{
  val multiplicand: UntypedMeasure

  val multiplier: UntypedMeasure

  lazy val dimension = ProductDimension(multiplicand.dimension, multiplier.dimension)

  lazy val system = if (multiplicand.system == multiplier.system) Derived(multiplicand.system) else Hybrid(multiplicand.system, multiplier.system)
}

object UntypedProductMeasure
{
  def apply(multiplicand: UntypedMeasure, multiplier: UntypedMeasure): UntypedProductMeasure =
  {
    val params = (multiplicand, multiplier)

    new UntypedProductMeasure
    {
      override val multiplicand: UntypedMeasure = params._1

      override val multiplier: UntypedMeasure = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: UntypedProductMeasure => this.multiplicand == that.multiplicand && this.multiplier == that.multiplier
        case _ => false
      }

      override def hashCode(): Int = 41 * multiplicand.hashCode() + multiplier.hashCode()

      override def toString = s"$multiplicand * $multiplier"
    }
  }

  def unapply(upm: UntypedProductMeasure): Option[(UntypedMeasure, UntypedMeasure)] = Some((upm.multiplicand, upm.multiplier))
}

trait UntypedRatioMeasure extends UntypedMeasure
{
  val numerator: UntypedMeasure

  val denominator: UntypedMeasure

  lazy val dimension: Dimension = RatioUntypedDimension(numerator.dimension, denominator.dimension)

  lazy val system = if (numerator.system == denominator.system) Derived(numerator.system) else Hybrid(numerator.system, denominator.system)
}

object UntypedRatioMeasure
{
  def apply(numerator: UntypedMeasure, denominator: UntypedMeasure): UntypedRatioMeasure =
  {
    val params = (numerator, denominator)

    new UntypedRatioMeasure
    {
      override val numerator: UntypedMeasure = params._1

      override val denominator: UntypedMeasure = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: UntypedRatioMeasure => this.numerator == that.numerator && this.denominator == that.denominator
        case _ => false
      }

      override def hashCode(): Int = 41 * numerator.hashCode() + denominator.hashCode()

      override def toString = s"$numerator / $denominator"
    }
  }

  def unapply(urm: UntypedRatioMeasure): Option[(UntypedMeasure, UntypedMeasure)] = Some((urm.numerator, urm.denominator))
}

trait UntypedExponentialMeasure extends UntypedMeasure
{
  val base: UntypedMeasure

  lazy val dimension: Dimension = ExponentialDimension(base.dimension, exponent)

  lazy val system = base.system
}

object UntypedExponentialMeasure
{
  def apply(base: UntypedMeasure, exponent: Double): UntypedExponentialMeasure =
  {
    val params = (base, exponent)

    new UntypedExponentialMeasure
    {
      override val base: UntypedMeasure = params._1

      override val exponent: Double = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: UntypedExponentialMeasure => this.base == that.base && this.exponent == that.exponent
        case _ => false
      }

      override def hashCode(): Int = 41 * base.hashCode() + exponent.hashCode()

      override def toString = s"$base ^ $exponent"
    }
  }

  def unapply(uem: UntypedExponentialMeasure): Option[(UntypedMeasure, Double)] = Some((uem.base, uem.exponent))
}