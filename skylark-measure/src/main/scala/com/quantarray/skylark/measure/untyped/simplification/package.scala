package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.untyped.morphing.ToProductOfExponentialsMorphism
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

    implicit object DefaultCanSimplify extends CanSimplify[Measure, Measure]
    {
      override def simplify(inflated: Measure): Measure =
      {
        val productOfExponentials = ToProductOfExponentialsMorphism(inflated)

        DefaultReducer(productOfExponentials)
      }
    }

  }

}
