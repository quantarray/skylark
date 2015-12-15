package com.quantarray.skylark.measure

import scala.language.dynamics

/**
 * Untyped measure.
 *
 * @author Araik Grigoryan
 */
trait UntypedMeasure extends Dynamic
{
  /**
   * Gets exponent of this measure.
   */
  def exponent: Double = 1.0
}

trait ProductUntypedMeasure extends UntypedMeasure
{
  val multiplicand: UntypedMeasure

  val multiplier: UntypedMeasure
}

object ProductUntypedMeasure
{
  def apply(multiplicand: UntypedMeasure, multiplier: UntypedMeasure): ProductUntypedMeasure =
  {
    val params = (multiplicand, multiplier)

    new ProductUntypedMeasure
    {
      override val multiplicand: UntypedMeasure = params._1

      override val multiplier: UntypedMeasure = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: ProductUntypedMeasure => this.multiplicand == that.multiplicand && this.multiplier == that.multiplier
        case _ => false
      }

      override def hashCode(): Int = 41 * multiplicand.hashCode() + multiplier.hashCode()

      override def toString = s"$multiplicand * $multiplier"
    }
  }
}

trait RatioUntypedMeasure extends UntypedMeasure
{
  val numerator: UntypedMeasure

  val denominator: UntypedMeasure
}

object RatioUntypedMeasure
{
  def apply(numerator: UntypedMeasure, denominator: UntypedMeasure): RatioUntypedMeasure =
  {
    val params = (numerator, denominator)

    new RatioUntypedMeasure
    {
      override val numerator: UntypedMeasure = params._1

      override val denominator: UntypedMeasure = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: RatioUntypedMeasure => this.numerator == that.numerator && this.denominator == that.denominator
        case _ => false
      }

      override def hashCode(): Int = 41 * numerator.hashCode() + denominator.hashCode()

      override def toString = s"$numerator / $denominator"
    }
  }
}

trait ExponentialUntypedMeasure extends UntypedMeasure
{
  val base: UntypedMeasure
}

object ExponentialUntypedMeasure
{
  def apply(base: UntypedMeasure, exponent: Double): ExponentialUntypedMeasure =
  {
    val params = (base, exponent)

    new ExponentialUntypedMeasure
    {
      override val base: UntypedMeasure = params._1

      override val exponent: Double = params._2

      override def equals(obj: scala.Any): Boolean = obj match
      {
        case that: ExponentialUntypedMeasure => this.base == that.base && this.exponent == that.exponent
        case _ => false
      }

      override def hashCode(): Int = 41 * base.hashCode() + exponent.hashCode()

      override def toString = s"$base ^ $exponent"
    }
  }
}