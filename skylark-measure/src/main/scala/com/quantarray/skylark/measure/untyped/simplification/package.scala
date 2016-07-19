package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.CanSimplify

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object simplification
{
  object Implicits
  {
    implicit object DefaultCanSimplify extends CanSimplify[Measure, Measure]
    {
      override def simplify(inflated: Measure): Measure = inflated match
      {
        case ProductMeasure(md, mr) if md == Unit && mr == Unit => Unit
        case ProductMeasure(md, mr) if md == Unit => mr.simplify
        case ProductMeasure(md, mr) if mr == Unit => md.simplify
        case ProductMeasure(md, mr@RatioMeasure(nr, dr)) if md == dr => nr.simplify
        case ProductMeasure(md@RatioMeasure(nr, dr), mr) if dr == mr => nr.simplify
        case ExponentialMeasure(base, exponent) if exponent == 1.0 => base.simplify
        case _ => inflated
      }
    }
  }
}
