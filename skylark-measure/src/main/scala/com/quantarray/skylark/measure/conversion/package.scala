package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */
package object conversion
{

  object EnergyConverter extends Converter[EnergyMeasure, EnergyMeasure]

  implicit object EnergyCanConvert extends CanConvert[EnergyMeasure, EnergyMeasure]
  {
    override def convert: Converter[EnergyMeasure, EnergyMeasure] = EnergyConverter
  }

  object MassConverter extends Converter[MassMeasure, MassMeasure]
  {
    override def apply(from: MassMeasure, to: MassMeasure): Option[Double] = (from, to) match
    {
      case (`kg`, `lb`) => Some(2.204625)
      case (`kg`, `g`) => Some(1000)
      case _ => super.apply(from, to)
    }
  }

  implicit object MassCanConvert extends CanConvert[MassMeasure, MassMeasure]
  {
    override def convert: Converter[MassMeasure, MassMeasure] = MassConverter
  }

  object DimensionlessConverter extends Converter[DimensionlessMeasure, DimensionlessMeasure]
  {
    override def apply(from: DimensionlessMeasure, to: DimensionlessMeasure): Option[Double] = Some(from.base / to.base)
  }

  implicit object DimensionlessCanConvert extends CanConvert[DimensionlessMeasure, DimensionlessMeasure]
  {
    override def convert: Converter[DimensionlessMeasure, DimensionlessMeasure] = DimensionlessConverter
  }

  type ExponentialLengthMeasure = ExponentialMeasure[LengthMeasure]

  object ExponentialLengthConverter extends Converter[ExponentialLengthMeasure, ExponentialLengthMeasure]
  {
    override def apply(from: ExponentialLengthMeasure, to: ExponentialLengthMeasure): Option[Double] = (from, to) match
    {
      case (`gal`, `in3`) => Some(231.0)
      case (`ha`, `km2`) => Some(0.01)
      case _ => super.apply(from, to)
    }
  }

  implicit object ExponentialLengthCanConvert extends CanConvert[ExponentialLengthMeasure, ExponentialLengthMeasure]
  {
    override def convert: Converter[ExponentialLengthMeasure, ExponentialLengthMeasure] = ExponentialLengthConverter
  }

  object VolumeToExponentialLengthConverter extends Converter[VolumeMeasure, ExponentialLengthMeasure]
  {
    override def apply(from: VolumeMeasure, to: ExponentialLengthMeasure): Option[Double] = (from, to) match
    {
      case (`bbl`, `gal`) => Some(31.5)
    }
  }

  implicit object VolumeToExponentialLengthCanConvert extends CanConvert[VolumeMeasure, ExponentialLengthMeasure]
  {
    override def convert: Converter[VolumeMeasure, ExponentialLengthMeasure] = VolumeToExponentialLengthConverter
  }
}
