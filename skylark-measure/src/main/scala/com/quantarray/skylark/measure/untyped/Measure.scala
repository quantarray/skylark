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
trait Measure extends Dynamic
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
  def collect[B](pf: PartialFunction[Measure, B]): B = pf(this)
}

trait ProductMeasure extends Measure
{
  val multiplicand: Measure

  val multiplier: Measure

  lazy val dimension = ProductDimension(multiplicand.dimension, multiplier.dimension)

  lazy val system = if (multiplicand.system == multiplier.system) Derived(multiplicand.system) else Hybrid(multiplicand.system, multiplier.system)
}

object ProductMeasure
{
  def apply(multiplicand: Measure, multiplier: Measure): ProductMeasure =
  {
    val params = (multiplicand, multiplier)

    new ProductMeasure
    {
      override val multiplicand: Measure = params._1

      override val multiplier: Measure = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: ProductMeasure => this.multiplicand == that.multiplicand && this.multiplier == that.multiplier
        case _ => false
      }

      override def hashCode(): Int = 41 * multiplicand.hashCode() + multiplier.hashCode()

      override def toString = s"$multiplicand * $multiplier"
    }
  }

  def unapply(upm: ProductMeasure): Option[(Measure, Measure)] = Some((upm.multiplicand, upm.multiplier))
}

trait RatioMeasure extends Measure
{
  val numerator: Measure

  val denominator: Measure

  lazy val dimension: Dimension = RatioDimension(numerator.dimension, denominator.dimension)

  lazy val system = if (numerator.system == denominator.system) Derived(numerator.system) else Hybrid(numerator.system, denominator.system)
}

object RatioMeasure
{
  def apply(numerator: Measure, denominator: Measure): RatioMeasure =
  {
    val params = (numerator, denominator)

    new RatioMeasure
    {
      override val numerator: Measure = params._1

      override val denominator: Measure = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: RatioMeasure => this.numerator == that.numerator && this.denominator == that.denominator
        case _ => false
      }

      override def hashCode(): Int = 41 * numerator.hashCode() + denominator.hashCode()

      override def toString = s"$numerator / $denominator"
    }
  }

  def unapply(urm: RatioMeasure): Option[(Measure, Measure)] = Some((urm.numerator, urm.denominator))
}

trait ExponentialMeasure extends Measure
{
  val expBase: Measure

  lazy val dimension: Dimension = ExponentialDimension(expBase.dimension, exponent)

  lazy val system = expBase.system
}

object ExponentialMeasure
{
  def apply(base: Measure, exponent: Double): ExponentialMeasure =
  {
    val params = (base, exponent)

    new ExponentialMeasure
    {
      override val expBase: Measure = params._1

      override val exponent: Double = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: ExponentialMeasure => this.expBase == that.expBase && this.exponent == that.exponent
        case _ => false
      }

      override def hashCode(): Int = 41 * base.hashCode() + exponent.hashCode()

      override def toString = s"$base ^ $exponent"
    }
  }

  def unapply(uem: ExponentialMeasure): Option[(Measure, Double)] = Some((uem.expBase, uem.exponent))
}