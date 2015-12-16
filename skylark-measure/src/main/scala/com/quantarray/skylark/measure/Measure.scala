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
 * Measure.
 *
 * The guiding principle(s) of design is and should be:
 *
 * 1. Construction of a measure should be fast, without any recursion/iteration to perform simplification.
 * 2. Compute-intensive methods, such as reduce, perform simplification and should be called only when necessary.
 *
 * @author Araik Grigoryan
 */
trait Measure[Self <: Measure[Self]] extends UntypedMeasure
{
  self: Self =>

  type D <: Dimension[D]

  /**
   * Measure name.
   */
  val name: String

  /**
   * Gets structural name of this measure.
   */
  final val structuralName = if (isStructuralAtom) name else s"($name)"

  /**
   * Gets dimension of this measure.
   */
  def dimension: D

  def composes(name: String, system: SystemOfUnits): Self

  def composes(name: String): Self = composes(name, system)

  def +[M2 <: Measure[M2]](that: M2)(implicit ev: Self =:= M2): Self = this

  def -[M2 <: Measure[M2]](that: M2)(implicit ev: Self =:= M2): Self = this

  /**
   * Turns this measure into a general RatioMeasure.
   */
  def /[M2 <: Measure[M2], R](denominator: M2)(implicit cd: CanDivide[Self, M2, R]): R = cd.divide(this, denominator)

  /**
   * Turns this measure into a general ProductMeasure.
   */
  def *[M2 <: Measure[M2], R](multiplier: M2)(implicit cm: CanMultiply[Self, M2, R]): R = cm.times(this, multiplier)

  /**
   * Turns this measure into a general ExponentialMeasure.
   */
  def ^[R](exponent: Double)(implicit ce: CanExponentiate[Self, R]): R = ce.pow(this, exponent)

  /**
   * Gets an inverse of this measure.
   */
  def inverse[R](implicit ce: CanExponentiate[Self, R]) = this ^ -exponent

  /**
   * Converts to target measure.
   */
  def to[M2 <: Measure[M2]](target: M2)(implicit cc: CanConvert[Self, M2]): Option[Double] = cc.convert(this, target)

  def reduce[R](implicit cr: CanReduce[Self, R]): R = cr.reduce(this)
}

/**
 * Product measure.
 */
trait ProductMeasure[M1 <: Measure[M1], M2 <: Measure[M2]] extends Measure[ProductMeasure[M1, M2]] with ProductUntypedMeasure
{
  val multiplicand: M1

  val multiplier: M2

  type D = ProductDimension[multiplicand.D, multiplier.D]

  override lazy val dimension = ProductDimension(multiplicand.dimension, multiplier.dimension)

  final override val isStructuralAtom = false
}

object ProductMeasure
{
  def apply[M1 <: Measure[M1], M2 <: Measure[M2]](multiplicand: M1, multiplier: M2): ProductMeasure[M1, M2] =
  {
    val params = (multiplicand, multiplier)

    new ProductMeasure[M1, M2]
    {
      lazy val multiplicand: M1 = params._1

      lazy val multiplier: M2 = params._2

      lazy val name = s"${multiplicand.structuralName} * ${multiplier.structuralName}"

      override def composes(name: String, system: SystemOfUnits): ProductMeasure[M1, M2] = this

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: ProductMeasure[_, _] => this.multiplicand == that.multiplicand && this.multiplier == that.multiplier
        case _ => false
      }

      override def hashCode(): Int = 41 * multiplicand.hashCode() + multiplier.hashCode()

      override def toString = name
    }
  }

  def unapply[M1 <: Measure[M1], M2 <: Measure[M2]](pm: ProductMeasure[M1, M2]): Option[(M1, M2)] = Some((pm.multiplicand, pm.multiplier))
}

/**
 * Ratio measure.
 */
trait RatioMeasure[M1 <: Measure[M1], M2 <: Measure[M2]] extends Measure[RatioMeasure[M1, M2]] with RatioUntypedMeasure
{
  val numerator: M1

  val denominator: M2

  type D = RatioDimension[numerator.D, denominator.D]

  override lazy val dimension = RatioDimension(numerator.dimension, denominator.dimension)

  final override val isStructuralAtom = false

  /**
   * Converts to target measure.
   */
  def to[M3 <: Measure[M3], M4 <: Measure[M4]](target: RatioMeasure[M3, M4])
                                              (implicit ccn: CanConvert[M1, M3], ccd: CanConvert[M2, M4]): Option[Double] =
  {
    (numerator.to(target.numerator), denominator.to(target.denominator)) match
    {
      case (Some(n), Some(d)) => Some(n / d)
      case _ => None
    }
  }
}

object RatioMeasure
{
  def apply[M1 <: Measure[M1], M2 <: Measure[M2]](numerator: M1, denominator: M2): RatioMeasure[M1, M2] =
  {
    val params = (numerator, denominator)

    new RatioMeasure[M1, M2]
    {
      lazy val numerator: M1 = params._1

      lazy val denominator: M2 = params._2

      lazy val name = s"${numerator.structuralName} / ${denominator.structuralName}"

      override def composes(name: String, system: SystemOfUnits): RatioMeasure[M1, M2] = this

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: RatioMeasure[_, _] => this.numerator == that.numerator && this.denominator == that.denominator
        case _ => false
      }

      override def hashCode(): Int = 41 * numerator.hashCode() + denominator.hashCode()

      override def toString = name
    }
  }

  def unapply[M1 <: Measure[M1], M2 <: Measure[M2]](rm: RatioMeasure[M1, M2]): Option[(M1, M2)] = Some((rm.numerator, rm.denominator))
}

/**
 * Exponential measure.
 */
trait ExponentialMeasure[B <: Measure[B]] extends Measure[ExponentialMeasure[B]] with ExponentialUntypedMeasure
{
  val base: B

  type D = ExponentialDimension[base.D]

  val baseName = exponent match
  {
    case 1.0 => s"$base"
    case _ => s"${base.structuralName} ^ $exponent"
  }

  override lazy val dimension = ExponentialDimension(base.dimension, exponent)

  final override val isStructuralAtom = false

  val lift: Option[B] = if (exponent == 1.0) Some(base) else None
}

object ExponentialMeasure
{
  def apply[B <: Measure[B]](base: B, exponent: Double, name: Option[String] = None): ExponentialMeasure[B] =
  {
    val params = (base, exponent, name)

    new ExponentialMeasure[B]
    {
      lazy val base: B = params._1

      override def exponent: Double = params._2

      lazy val name = params._3.getOrElse(baseName)

      override def composes(name: String, system: SystemOfUnits): ExponentialMeasure[B] = ExponentialMeasure(base, exponent, Some(name))

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: ExponentialMeasure[_] => this.base == that.base && this.exponent == that.exponent
        case _ => false
      }

      override def hashCode(): Int = 41 * base.hashCode() + exponent.hashCode()

      override def toString = name
    }
  }

  def unapply[B <: Measure[B]](em: ExponentialMeasure[B]): Option[(B, Double)] = Some((em.base, em.exponent))
}
