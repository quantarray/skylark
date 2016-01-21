package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */
package object conversion
{
  trait SameTypeConverter[T] extends Converter[T, T]
  {
    override def apply(from: T, to: T): Option[Double] = convert(from, to)

    final def convert(from: T, to: T, attemptInverse: Boolean = true): Option[Double] =
    {
      if (convert.isDefinedAt(from, to))
        convert.lift.apply((from, to))
      else if (attemptInverse)
        convert(to, from, attemptInverse = false).fold(super.apply(from, to))(x => Some(1 / x))
      else
        super.apply(from, to)
    }

    protected def convert: PartialFunction[(T, T), Double] = PartialFunction.empty
  }

  /**
   * () -> ().
   */
  object DimensionlessConverter extends SameTypeConverter[DimensionlessMeasure]
  {
    override protected def convert: PartialFunction[(DimensionlessMeasure, DimensionlessMeasure), Double] =
    {
      case (from, to) => from.base / to.base
    }
  }

  implicit object DimensionlessCanConvert extends CanConvert[DimensionlessMeasure, DimensionlessMeasure]
  {
    override def convert: Converter[DimensionlessMeasure, DimensionlessMeasure] = DimensionlessConverter
  }

  /**
   * Time -> Time.
   */
  object TimeConverter extends SameTypeConverter[TimeMeasure]
  {
    override protected def convert: PartialFunction[(TimeMeasure, TimeMeasure), Double] =
    {
      case (`h`, `s`) => 3600
    }
  }

  implicit object TimeCanConvert extends CanConvert[TimeMeasure, TimeMeasure]
  {
    override def convert: Converter[TimeMeasure, TimeMeasure] = TimeConverter
  }

  /**
   * Mass -> Mass.
   */
  object MassConverter extends SameTypeConverter[MassMeasure]
  {
    protected override def convert: PartialFunction[(MassMeasure, MassMeasure), Double] =
    {
      case (`kg`, `lb`) => 2.204625
      case (`kg`, `g`) => 1000.0
    }
  }

  implicit object MassCanConvert extends CanConvert[MassMeasure, MassMeasure]
  {
    override def convert: Converter[MassMeasure, MassMeasure] = MassConverter

    override def toString: String = "MassCanConvert"
  }

  /**
   * Length -> Length.
   */
  object LengthConverter extends SameTypeConverter[LengthMeasure]
  {
    protected override def convert: PartialFunction[(LengthMeasure, LengthMeasure), Double] =
    {
      case (`ft`, `in`) => 12
      case (`yd`, `ft`) => 3
      case (`mi`, `m`) => 1609.34
    }
  }

  implicit object LengthCanConvert extends CanConvert[LengthMeasure, LengthMeasure]
  {
    override def convert: Converter[LengthMeasure, LengthMeasure] = LengthConverter
  }

  /**
   * Energy -> Energy.
   */
  object EnergyConverter extends SameTypeConverter[EnergyMeasure]
  {
    override protected def convert: PartialFunction[(EnergyMeasure, EnergyMeasure), Double] =
    {
      case (MMBtu, GJ) => 1.055056
    }
  }

  implicit object EnergyCanConvert extends CanConvert[EnergyMeasure, EnergyMeasure]
  {
    override def convert: Converter[EnergyMeasure, EnergyMeasure] = EnergyConverter
  }

  object ExponentialLengthConverter extends SameTypeConverter[ExponentialLengthMeasure]
  {
    override protected def convert: PartialFunction[(ExponentialLengthMeasure, ExponentialLengthMeasure), Double] =
    {
      case (`gal`, `in3`) => 231
      case (`ha`, `km2`) => 0.01
    }
  }

  implicit object ExponentialLengthCanConvert extends CanConvert[ExponentialLengthMeasure, ExponentialLengthMeasure]
  {
    override def convert: Converter[ExponentialLengthMeasure, ExponentialLengthMeasure] = ExponentialLengthConverter
  }

  /**
   * Volume -> Length^3^.
   */
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

  /**
   * EnergyPrice -> EnergyPrice.
   */
  object EnergyPriceConverter extends SameTypeConverter[EnergyPriceMeasure]
  {
    override protected def convert: PartialFunction[(EnergyPriceMeasure, EnergyPriceMeasure), Double] =
    {
      case _ => 1.0
    }
  }

  implicit object EnergyPriceCanConvert extends CanConvert[EnergyPriceMeasure, EnergyPriceMeasure]
  {
    override def convert: Converter[EnergyPriceMeasure, EnergyPriceMeasure] = EnergyPriceConverter
  }

  /**
   * EnergyPrice/() -> EnergyPrice/().
   */
  object EnergyPricePerDimensionlessConverter extends SameTypeConverter[EnergyPricePerDimensionlessMeasure]
  {
    override protected def convert: PartialFunction[(EnergyPricePerDimensionlessMeasure, EnergyPricePerDimensionlessMeasure), Double] =
    {
      case (from, to) => to.denominator.base / from.denominator.base
    }
  }

  implicit object EnergyPricePerDimensionlessCanConvert extends CanConvert[EnergyPricePerDimensionlessMeasure, EnergyPricePerDimensionlessMeasure]
  {
    override def convert: Converter[EnergyPricePerDimensionlessMeasure, EnergyPricePerDimensionlessMeasure] = EnergyPricePerDimensionlessConverter
  }

}
