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

package com.quantarray.skylark.measure

import scala.language.dynamics

/**
  * Any measure.
  *
  * @author Araik Grigoryan
  */
trait AnyMeasure extends Product with Serializable with Dynamic
{
  self =>

  /**
    * Measure name.
    */
  def name: String

  /**
    * Gets dimension of this measure.
    */
  def dimension: AnyDimension

  /**
    * Gets system of units.
    */
  def system: SystemOfUnits

  /**
    * Gets ultimate base.
    */
  def ultimateBase: Option[(AnyMeasure, Double)]

  /**
    * Determines if this measure can be decomposed into constituent measures.
    */
  def isStructuralAtom: Boolean = true

  /**
    * Gets structural name of this measure.
    */
  final def structuralName: String = if (isStructuralAtom) name else s"($name)"

  /**
    * Gets exponent of this measure.
    */
  def exponent: Double = 1.0

  @inline
  def collect[B](pf: PartialFunction[AnyMeasure, B]): B = pf(this)

  def ^(exponent: Double)(implicit ce: CanExponentiateMeasure[AnyMeasure, AnyMeasure]): AnyMeasure = ce.pow(this, exponent)

  def *(measure: AnyMeasure)(implicit cm: CanMultiplyMeasure[AnyMeasure, AnyMeasure, AnyMeasure]): AnyMeasure = cm.times(this, measure)

  def /(measure: AnyMeasure)(implicit cd: CanDivideMeasure[AnyMeasure, AnyMeasure, AnyMeasure]): AnyMeasure = cd.divide(this, measure)

  def to(target: AnyMeasure)(implicit cc: CanConvert[AnyMeasure, AnyMeasure]): Option[Double] = cc.convert(this, target)

  def toOrElse[B >: Double](target: AnyMeasure, default: B)(implicit cc: CanConvert[AnyMeasure, AnyMeasure]): B = to(target).getOrElse(default)

  def simplify(implicit cs: CanSimplifyMeasure[AnyMeasure, AnyMeasure]): AnyMeasure = cs.simplify(this)
}

trait AnyProductMeasure extends AnyMeasure
{
  val multiplicand: AnyMeasure

  val multiplier: AnyMeasure

  lazy val dimension = AnyProductDimension(multiplicand.dimension, multiplier.dimension)

  lazy val system: SystemOfUnits = if (multiplicand.system == multiplier.system) Derived(multiplicand.system) else Hybrid(multiplicand.system, multiplier.system)
}

object AnyProductMeasure
{
  def apply(multiplicand: AnyMeasure, multiplier: AnyMeasure): AnyProductMeasure =
  {
    val params = (multiplicand, multiplier)

    new AnyProductMeasure
    {
      override val multiplicand: AnyMeasure = params._1

      override val multiplier: AnyMeasure = params._2

      override val name = s"${multiplicand.structuralName} * ${multiplier.structuralName}"

      override val ultimateBase = None

      override val isStructuralAtom: Boolean = false

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: AnyProductMeasure => this.multiplicand == that.multiplicand && this.multiplier == that.multiplier
        case _ => false
      }

      override def hashCode(): Int = 41 * multiplicand.hashCode() + multiplier.hashCode()

      private val productElements = Seq(multiplicand, multiplier)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[AnyProductMeasure]

      override def toString: String = name
    }
  }

  def unapply(upm: AnyProductMeasure): Option[(AnyMeasure, AnyMeasure)] = Some((upm.multiplicand, upm.multiplier))
}

trait AnyRatioMeasure extends AnyMeasure
{
  val numerator: AnyMeasure

  val denominator: AnyMeasure

  lazy val dimension: AnyDimension = AnyRatioDimension(numerator.dimension, denominator.dimension)

  lazy val system: SystemOfUnits = if (numerator.system == denominator.system) Derived(numerator.system) else Hybrid(numerator.system, denominator.system)
}

object AnyRatioMeasure
{
  def apply(numerator: AnyMeasure, denominator: AnyMeasure): AnyRatioMeasure =
  {
    val params = (numerator, denominator)

    new AnyRatioMeasure
    {
      override val numerator: AnyMeasure = params._1

      override val denominator: AnyMeasure = params._2

      val name = s"${numerator.structuralName} / ${denominator.structuralName}"

      override val ultimateBase = None

      override val isStructuralAtom: Boolean = false

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: AnyRatioMeasure => this.numerator == that.numerator && this.denominator == that.denominator
        case _ => false
      }

      override def hashCode(): Int = 41 * numerator.hashCode() + denominator.hashCode()

      private val productElements = Seq(numerator, denominator)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[AnyRatioMeasure]

      override def toString: String = name
    }
  }

  def unapply(urm: AnyRatioMeasure): Option[(AnyMeasure, AnyMeasure)] = Some((urm.numerator, urm.denominator))
}

trait AnyExponentialMeasure extends AnyMeasure
{
  def expBase: AnyMeasure

  lazy val dimension: AnyDimension = AnyExponentialDimension(expBase.dimension, exponent)

  lazy val system: SystemOfUnits = expBase.system

  lazy val baseName: String = exponent match
  {
    case 1.0 => s"$expBase"
    case _ => s"${expBase.structuralName} ^ $exponent"
  }
}

object AnyExponentialMeasure
{
  def apply(base: AnyMeasure, exponent: Double): AnyExponentialMeasure =
  {
    val params = (base, exponent)

    new AnyExponentialMeasure
    {
      override val expBase: AnyMeasure = params._1

      override val exponent: Double = params._2

      override val name: String = baseName

      override val ultimateBase = None

      override val isStructuralAtom: Boolean = false

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: AnyExponentialMeasure => this.expBase == that.expBase && this.exponent == that.exponent
        case _ => false
      }

      override def hashCode(): Int = 41 * base.hashCode() + exponent.hashCode()

      private val productElements = Seq(base, exponent)

      override def productElement(n: Int): Any = productElements(n)

      val productArity: Int = productElements.size

      override def canEqual(that: Any): Boolean = that.isInstanceOf[AnyExponentialMeasure]

      override def toString: String = name
    }
  }

  def unapply(uem: AnyExponentialMeasure): Option[(AnyMeasure, Double)] = Some((uem.expBase, uem.exponent))
}