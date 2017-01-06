package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can divide measure type class.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanDivideMeasure implementation that divides ${N} by ${D}, resulting in ${R}.")
trait CanDivideMeasure[N, D, R]
{
  def divide(numerator: N, denominator: D): R
}
