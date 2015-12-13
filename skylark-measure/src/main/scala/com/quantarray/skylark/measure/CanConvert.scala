package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
 * Can convert type class.
 *
 * @author Araik Grigoryan
 */
@implicitNotFound("Cannot find CanConvert implementation that converts from ${From} to ${To}.")
trait CanConvert[From, To]
{
  def convert: Converter[From, To]
}
