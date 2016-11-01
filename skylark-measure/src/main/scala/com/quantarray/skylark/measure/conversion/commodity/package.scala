package com.quantarray.skylark.measure.conversion

import com.quantarray.skylark.measure._

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object commodity
{

  object VolumeToExponentialLengthConverter extends Converter[VolumeMeasure, ExponentialLength]
  {
    override def apply(from: VolumeMeasure, to: ExponentialLength): Option[Double] = Conversion(from, to) match
    {
      case `bbl` ⤇ `gal` => Some(42.0)
    }
  }

  object default
  {

    implicit object VolumeToExponentialLengthCanConvert extends CanConvert[VolumeMeasure, ExponentialLength]
    {
      override def convert: Converter[VolumeMeasure, ExponentialLength] = VolumeToExponentialLengthConverter
    }

  }

}
