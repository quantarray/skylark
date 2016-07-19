package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object untyped
{

  trait DefaultImplicits
  {
    implicit def exponentialCanExponentiate = new CanExponentiate[untyped.Measure, untyped.Measure]
    {
      override def pow(base: untyped.Measure, exponent: Double): untyped.Measure = ExponentialMeasure(base, exponent)
    }

    implicit def productCanMultiply = new CanMultiply[untyped.Measure, untyped.Measure, untyped.Measure]
    {
      override def times(multiplicand: untyped.Measure, multiplier: untyped.Measure): untyped.Measure = ProductMeasure(multiplicand, multiplier)
    }

    implicit def ratioCanDivide = new CanDivide[untyped.Measure, untyped.Measure, untyped.Measure]
    {
      override def divide(numerator: untyped.Measure, denominator: untyped.Measure): untyped.Measure = RatioMeasure(numerator, denominator)
    }
  }

  object Implicits extends DefaultImplicits
  {

  }

}
