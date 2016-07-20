package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object arithmetic
{
  trait DefaultImplicits
  {
    implicit def productCanMultiply[M1 <: Measure[M1], M2 <: Measure[M2]] = new CanMultiply[M1, M2, ProductMeasure[M1, M2]]
    {
      override def times(multiplicand: M1, multiplier: M2): ProductMeasure[M1, M2] = ProductMeasure(multiplicand, multiplier)
    }

    implicit def ratioCanDivide[N <: Measure[N], D <: Measure[D]] = new CanDivide[N, D, RatioMeasure[N, D]]
    {
      override def divide(numerator: N, denominator: D): RatioMeasure[N, D] = RatioMeasure(numerator, denominator)
    }

    implicit def exponentialCanExponentiate[B <: Measure[B]] = new CanExponentiate[B, ExponentialMeasure[B]]
    {
      override def pow(base: B, exponent: Double): ExponentialMeasure[B] = ExponentialMeasure(base, exponent)
    }
  }

  object default extends DefaultImplicits
  {

    implicit object MassCanDivide extends CanDivide[MassMeasure, MassMeasure, DimensionlessMeasure]
    {
      override def divide(numerator: MassMeasure, denominator: MassMeasure): DimensionlessMeasure = Unit
    }

  }
}
