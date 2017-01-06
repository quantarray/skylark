package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can multiply measure type class.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanMultiplyMeasure implementation that multiplies ${M1} and ${M2}, resulting in ${R}.")
trait CanMultiplyMeasure[M1, M2, R]
{
  def times(multiplicand: M1, multiplier: M2): R
}