package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.untyped.reduction.DefaultReducer

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object simplification
{

  object default
  {
    case object ProductOfExponentials
    {
      def apply(from: Measure): ProductMeasure = product(from, 1)

      private def product(from: Measure, outerExponent: Double): ProductMeasure = from match
      {
        case ProductMeasure(md, mr) => ProductMeasure(product(md, outerExponent), product(mr, outerExponent))
        case RatioMeasure(nr, dr) => ProductMeasure(product(nr, outerExponent), product(dr, -outerExponent))
        case ExponentialMeasure(base, exponent) => ProductMeasure(product(base, exponent * outerExponent), Unit)
        case _ if outerExponent == 1.0 => ProductMeasure(from, Unit)
        case _ => ProductMeasure(ExponentialMeasure(from, outerExponent), Unit)
      }
    }

    implicit object DefaultCanSimplify extends CanSimplify[Measure, Measure]
    {
      override def simplify(inflated: Measure): Measure =
      {
        val productOfExponentials = ProductOfExponentials(inflated)

        DefaultReducer(productOfExponentials)
      }
    }

  }

}
