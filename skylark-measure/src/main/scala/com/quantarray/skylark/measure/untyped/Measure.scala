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

import scala.language.dynamics

/**
  * Untyped measure.
  *
  * @author Araik Grigoryan
  */
trait Measure extends Product with Serializable with Dynamic
{
  self =>

  /**
    * Measure name.
    */
  def name: String

  /**
    * Gets dimension of this measure.
    */
  def dimension: Dimension

  /**
    * Gets system of units.
    */
  def system: SystemOfUnits

  /**
    * Gets ultimate base.
    */
  def ultimateBase: Option[(Measure, Double)]

  /**
    * Determines if this measure can be decomposed into constituent measures.
    */
  def isStructuralAtom: Boolean = true

  /**
    * Gets structural name of this measure.
    */
  final def structuralName = if (isStructuralAtom) name else s"($name)"

  /**
    * Gets exponent of this measure.
    */
  def exponent: Double = 1.0

  @inline
  def collect[B](pf: PartialFunction[Measure, B]): B = pf(this)

  def ^(exponent: Double)(implicit ce: CanExponentiate[Measure, Measure]): Measure = ce.pow(this, exponent)

  def *(measure: Measure)(implicit cm: CanMultiply[Measure, Measure, Measure]): Measure = cm.times(this, measure)

  def /(measure: Measure)(implicit cm: CanDivide[Measure, Measure, Measure]): Measure = cm.divide(this, measure)

  def to(target: Measure)(implicit cc: CanConvert[Measure, Measure]): Option[Double] = cc.convert(this, target)

  def toOrElse[B >: Double](target: Measure, default: B)(implicit cc: CanConvert[Measure, Measure]): B = to(target).getOrElse(default)

  def simplify(implicit cs: CanSimplify[Measure, Measure]): Measure = cs.simplify(this)
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

      override val name = s"${multiplicand.structuralName} * ${multiplier.structuralName}"

      override val ultimateBase = None

      override val isStructuralAtom: Boolean = false

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: ProductMeasure => this.multiplicand == that.multiplicand && this.multiplier == that.multiplier
        case _ => false
      }

      override def hashCode(): Int = 41 * multiplicand.hashCode() + multiplier.hashCode()

      private val productElements = Seq(multiplicand, multiplier)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[ProductMeasure]

      override def toString = name
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

      val name = s"${numerator.structuralName} / ${denominator.structuralName}"

      override val ultimateBase = None

      override val isStructuralAtom: Boolean = false

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: RatioMeasure => this.numerator == that.numerator && this.denominator == that.denominator
        case _ => false
      }

      override def hashCode(): Int = 41 * numerator.hashCode() + denominator.hashCode()

      private val productElements = Seq(numerator, denominator)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[RatioMeasure]

      override def toString = name
    }
  }

  def unapply(urm: RatioMeasure): Option[(Measure, Measure)] = Some((urm.numerator, urm.denominator))
}

trait ExponentialMeasure extends Measure
{
  def expBase: Measure

  lazy val dimension: Dimension = ExponentialDimension(expBase.dimension, exponent)

  lazy val system = expBase.system

  lazy val baseName = exponent match
  {
    case 1.0 => s"$expBase"
    case _ => s"${expBase.structuralName} ^ $exponent"
  }
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

      override val name = baseName

      override val ultimateBase = None

      override val isStructuralAtom: Boolean = false

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: ExponentialMeasure => this.expBase == that.expBase && this.exponent == that.exponent
        case _ => false
      }

      override def hashCode(): Int = 41 * base.hashCode() + exponent.hashCode()

      private val productElements = Seq(base, exponent)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[ExponentialMeasure]

      override def toString = name
    }
  }

  def unapply(uem: ExponentialMeasure): Option[(Measure, Double)] = Some((uem.expBase, uem.exponent))
}