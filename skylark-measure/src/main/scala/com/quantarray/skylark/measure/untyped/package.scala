package com.quantarray.skylark.measure

import com.quantarray.skylark.measure.untyped.arithmetic.SafeArithmeticImplicits

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object untyped
{
  val * = untyped.ProductMeasure
  val / = untyped.RatioMeasure
  val ^ = untyped.ExponentialMeasure

  type ⤇[From, To] = Conversion[From, To]

  val ⤇ = Conversion

  object implicits extends SafeArithmeticImplicits
}
