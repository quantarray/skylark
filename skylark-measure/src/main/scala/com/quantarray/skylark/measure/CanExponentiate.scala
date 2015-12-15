package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can exponentiate type class.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanExponentiate implementation that raises ${B} to some exponent, resulting in ${R}.")
trait CanExponentiate[B, R]
{
  def pow(base: B, exponent: Double): R
}
