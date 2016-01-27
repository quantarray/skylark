package com.quantarray.skylark.measure

import com.quantarray.skylark.measure.untyped.UntypedMeasure

/**
 * Measure provider.
 *
 * @author Araik Grigoryan
 */
trait MeasureProvider
{
  trait MeasureReader
  {
    def apply(name: String): Option[UntypedMeasure]
  }

  def read: MeasureReader
}
