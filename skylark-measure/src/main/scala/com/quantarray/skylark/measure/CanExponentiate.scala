package com.quantarray.skylark.measure

/**
 * Can exponentiate type class.
 *
 * @author Araik Grigoryan
 */
trait CanExponentiate[B, R]
{
  def pow(base: B, exponent: Double): R
}
