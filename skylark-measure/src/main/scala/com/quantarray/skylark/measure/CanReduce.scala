package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can reduce type class. Determines the shape of reduced (deflated) measure.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanReduce implementation that reduces ${I} to ${D}.")
trait CanReduce[I, D]
{
  def reduce(inflated: I): D
}
