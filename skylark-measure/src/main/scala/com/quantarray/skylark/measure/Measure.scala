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

import com.quantarray.skylark.measure.conversion.ConstantConversionProvider

import scala.annotation.tailrec
import scala.language.{existentials, implicitConversions}

/**
 * Measure.
 *
 * The guiding principle(s) of design is and should be:
 *
 * 1. Construction of a measure should be fast, without any recursion/iteration to perform simplification.
 * 2. Compute-intensive methods, such as reduce, perform simplification and should be called only when necessary. Results can be declared as lazy val.
 *
 * @author Araik Grigoryan
 */
trait Measure
{
  self =>

  type D <: Dimension

  type Repr <: Measure

  def repr: Repr = this.asInstanceOf[Repr]

  def name: String

  /**
   * Gets dimension of this measure.
   *
   * Due to type erasure, we need this method to pattern-match on various measures.
   */
  def dimension: D

  /**
   * Gets system of units.
   */
  def system: SystemOfUnits

  /**
   * Gets the declared multiplicative relationship with some base measure, which may not be the ultimate multiplicative relationship.
   */
  def declMultBase: Option[(Double, Measure)]

  /**
   * Gets ultimate multiplicative relationship with base measure, e.g. 0.001(Kilogram) for Gram.
   */
  lazy val multBase = Measure.multiplicativeBase(declMultBase)

  lazy val multBaseValue = multBase match
  {
    case None => 1.0
    case Some((multiple, _)) => multiple
  }

  /**
   * Determines if this measure can be decomposed into constituent measures.
   */
  def isStructuralAtom = true

  /**
   * Gets exponential base of this measure.
   */
  def expBase: Measure = this

  /**
   * Gets exponent of this measure.
   */
  def exp: Double = 1.0

  /**
   * Gets an inverse of this measure.
   */
  def inverse: Measure = ExponentialMeasure(this, -exp)

  /**
   * Turn this measure into a general RatioMeasure.
   */
  def /(denominator: Measure) = RatioMeasure(this, denominator)

  /**
   * Turn this measure into a general ProductMeasure.
   */
  def *(multiplier: Measure) = ProductMeasure(this, multiplier)

  /**
   * Creates a new measure that is multiplier times larger (or smaller) than this measure.
   */
  def *(multiplier: Double): Repr = *((s"$multiplier $name", multiplier))

  /**
   * Creates a new measure that is multiple times larger (or smaller) than this measure.
   */
  def *(nameMultiple: (String, Double)): Repr =
  {
    nameMultiple match
    {
      case (_, 1.0) => repr
      case _ => multBase match
      {
        case Some(x) => build(nameMultiple._1, (x._1 * nameMultiple._2, x._2))
        case _ => build(nameMultiple._1, (nameMultiple._2, this))
      }
    }
  }

  /**
   * Gets exponential measure with no multiplicative factor, e.g. 1 sq meter = m^^2.
   */
  def ^(exponent: Double): ExponentialMeasure = ExponentialMeasure(this, exponent)

  /**
   * Gets exponential measure with multiplicative factor, e.g. 1 hectare = 10000 m^^2.
   */
  def ^(exponent: Double, dmb: Option[(Double, Measure)]) = ExponentialMeasure(this, exponent, dmb)

  def to(toMeasure: Measure)(implicit conversion: ConstantConversionProvider): Option[Double] = conversion.factor(toMeasure, this)

  /**
   * Compacts this measure.
   */
  lazy val compact: Measure =
  {
    def compact(measure: Measure): Measure =
    {
      measure match
      {
        case (ProductMeasure(multiplicand, multiplier)) if multiplier == UnitMeasure => compact(multiplicand)
        case (ProductMeasure(multiplicand, multiplier)) if multiplicand == UnitMeasure => compact(multiplier)
        case (ProductMeasure(multiplicand, multiplier)) => compact(multiplicand) * compact(multiplier)
        case (RatioMeasure(numerator, denominator)) if denominator == UnitMeasure => compact(numerator)
        case (RatioMeasure(numerator, denominator)) if numerator == denominator => UnitMeasure
        case (RatioMeasure(numerator, denominator)) => compact(numerator) / compact(denominator)
        case (ExponentialMeasure(base, exponent, _)) if base == UnitMeasure => UnitMeasure
        case _ => measure
      }
    }

    compact(this)
  }

  /**
   * Gets a multiple of this measure.
   */
  protected[measure] def build(name: String, mb: (Double, Measure)): Repr
}

case object UnitMeasure extends Measure
{
  type D = NoDimension.type

  type Repr = UnitMeasure.type

  def name = "1"

  /**
   * Gets dimension of this measure.
   *
   * Due to type erasure, we need this method to pattern-match on various measures.
   */
  def dimension = NoDimension

  /**
   * Gets system of units.
   */
  def system: SystemOfUnits = Universal()

  /**
   * Gets multiplicative relationship with base measure, e.g. (0.001, Kilogram) for Gram.
   */
  def declMultBase: Option[(Double, Measure)] = None

  /**
   * Gets a multiple of this measure.
   */
  override protected[measure] def build(name: String, mb: (Double, Measure)): Repr = repr

  override def toString = name
}

/**
 * Product measure.
 */
case class ProductMeasure(multiplicand: Measure, multiplier: Measure) extends Measure
{
  type D = ProductDimension

  type Repr = ProductMeasure

  val name = s"${Measure.name(multiplicand)} * ${Measure.name(multiplier)}"

  val dimension = multiplicand.dimension * multiplier.dimension

  val system = if (multiplicand.system == multiplier.system) Derived(multiplicand.system) else Hybrid(multiplicand.system, multiplier.system)

  val declMultBase = None

  override def isStructuralAtom = false

  /**
   * Gets a multiple of this measure.
   */
  override protected[measure] def build(name: String, mb: (Double, Measure)): Repr = repr

  override def toString = name
}

/**
 * Ratio measure.
 */
case class RatioMeasure(numerator: Measure, denominator: Measure) extends Measure
{
  type D = RatioDimension

  type Repr = RatioMeasure

  val name = s"${Measure.name(numerator)} / ${Measure.name(denominator)}"

  val dimension = numerator.dimension / denominator.dimension

  val system = if (numerator.system == denominator.system) Derived(numerator.system) else Hybrid(numerator.system, denominator.system)

  val declMultBase = None

  override def isStructuralAtom = false

  /**
   * Gets a multiple of this measure.
   */
  override protected[measure] def build(name: String, mb: (Double, Measure)): Repr = repr

  override def toString = name
}


/**
 * Exponential measure.
 */
case class ExponentialMeasure(base: Measure, override val exp: Double, dmb: Option[(Double, Measure)] = None)
  extends Measure
{
  type D = ExponentialDimension[base.D]

  type Repr = ExponentialMeasure

  val name = exp match
  {
    case 1.0 => s"$base"
    case _ => s"${Measure.name(base)} ^ $exp"
  }

  val dimension = ExponentialDimension(base.dimension, exp)

  val system = base.system

  val declMultBase =
  {
    dmb match
    {
      case Some(d) => Some(d)
      case _ =>
        // TODO: Recurse down to the ultimate base
        base.multBase match
        {
          case Some(x) => Some((scala.math.pow(x._1, exp), x._2 ^ exp))
          case _ => None
        }
    }
  }

  override def expBase = base

  override def isStructuralAtom = false

  override protected[measure] def build(name: String, mb: (Double, Measure)): Repr = repr

  override def toString = name
}

object Measure
{

  def name(measure: Measure) = if (measure.isStructuralAtom) measure else s"($measure)"

  def multiplicativeBase(mb: Option[(Double, Measure)]): Option[(Double, Measure)] =
  {
    @tailrec
    def multBase(m: Double, b: Measure): (Double, Measure) =
    {
      b.multBase match
      {
        case None => (m, b)
        case Some(x) => multBase(m * x._1, x._2)
      }
    }

    mb match
    {
      case Some(x) => Some(multBase(x._1, x._2))
      case None => None
    }
  }
}
