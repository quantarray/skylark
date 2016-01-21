package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */
package object conversion
{
  /**
    * () -> ().
    */
  implicit val dimensionlessCanConvert = CanConvert(DimensionlessConverter())

  /**
    * Time -> Time.
    */
  implicit val timeCanConvert = CanConvert(TimeConverter())

  /**
    * Mass -> Mass.
    */
  implicit val massCanConvert = CanConvert(MassConverter())

  /**
    * Length -> Length.
    */
  implicit val lengthCanConvert = CanConvert(LengthConverter())

  /**
    * Energy -> Energy.
    */
  implicit val energyCanConvert = CanConvert(EnergyConverter())

  /**
    * Length^n^ -> Length^n^.
    */
  implicit val exponentialLengthCanConvert = CanConvert(ExponentialLengthConverter())

  /**
    * Volume -> Length^3^.
    */

  implicit val volumeToExponentialLengthCanConvert = CanConvert(VolumeToExponentialLengthConverter())

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
