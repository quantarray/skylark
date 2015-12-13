package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can divide type class.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanDivide implementation that divide ${N} by ${D}, resulting in ${R}.")
trait CanDivide[N, D, R]
{
  def divide(n: N, d: D): R

  def unit(n: N, d: D): Double = 1.0
}
