package com.quantarray.skylark.measure.conversion

import com.quantarray.skylark.measure._

/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */
package object commodity
{
  object VolumeToExponentialLengthConverter extends Converter[VolumeMeasure, ExponentialLengthMeasure]
  {
    override def apply(from: VolumeMeasure, to: ExponentialLengthMeasure): Option[Double] = (from, to) match
    {
      case (`bbl`, `gal`) => Some(42.0)
    }
  }

  implicit object VolumeToExponentialLengthCanConvert extends CanConvert[VolumeMeasure, ExponentialLengthMeasure]
  {
    override def convert: Converter[VolumeMeasure, ExponentialLengthMeasure] = VolumeToExponentialLengthConverter
  }
}
