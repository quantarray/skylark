package com.quantarray.skylark.measure

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
