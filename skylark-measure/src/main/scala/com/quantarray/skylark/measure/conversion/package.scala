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
}
