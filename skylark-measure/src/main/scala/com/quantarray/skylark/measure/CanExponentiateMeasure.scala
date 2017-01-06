package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can exponentiate measure type class.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanExponentiateMeasure implementation that raises ${B} to some exponent, resulting in ${R}.")
trait CanExponentiateMeasure[B, R]
{
  def pow(base: B, exponent: Double): R
}
