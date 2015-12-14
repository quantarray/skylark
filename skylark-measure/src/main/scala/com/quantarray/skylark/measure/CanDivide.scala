package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can divide type class.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanDivide implementation that divides ${N} by ${D}, resulting in ${R}.")
trait CanDivide[N, D, R]
{
  def divide(numerator: N, denominator: D): R

  def unit(numerator: N, denominator: D): Double = 1.0
}
