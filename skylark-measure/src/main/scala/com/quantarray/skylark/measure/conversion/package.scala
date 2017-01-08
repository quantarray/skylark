package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2017, Quantarray
 * http://skylark.io
 */
package object conversion
{
  trait BaseConversionImplicits
  {
    implicit def defaultCanConvert[M <: Measure[M]] = CanConvert(SameTypeConverter[M])
  }

  trait DimensionessConversionImplicits extends BaseConversionImplicits
  {
    type P_[M <: Measure[M]] = ProductMeasure[M, DimensionlessMeasure]

    implicit def p_[M <: Measure[M]]: CanConvert[P_[M], M] = new CanConvert[P_[M], M]
    {
      override def convert: Converter[P_[M], M] = new Converter[P_[M], M]
      {
        override def apply(from: P_[M], to: M): Option[Double] = Some(from.multiplier.immediateBase)
      }
    }

    type R_[M <: Measure[M]] = RatioMeasure[M, DimensionlessMeasure]

    implicit def r_[M <: Measure[M]]: CanConvert[R_[M], M] = new CanConvert[R_[M], M]
    {
      override def convert: Converter[R_[M], M] = new Converter[R_[M], M]
      {
        override def apply(from: R_[M], to: M): Option[Double] = Some(1 / from.denominator.immediateBase)
      }
    }

    type E_ = ExponentialMeasure[DimensionlessMeasure]

    implicit def e_[M <: Measure[M]]: CanConvert[E_, M] = new CanConvert[E_, M]
    {
      override def convert: Converter[E_, M] = new Converter[E_, M]
      {
        override def apply(from: E_, to: M): Option[Double] = Some(math.pow(from.expBase.immediateBase, from.exponent))
      }
    }
  }

  trait DefaultConversionImplicits extends DimensionessConversionImplicits
  {

    /**
      * () -> ().
      */
    implicit val dimensionlessCanConvert = CanConvert(DimensionlessConverter)

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
      * Currency -> Currency.
      */
    implicit val currencyCanConvert: CanConvert[Currency, Currency] = CanConvert(FixedCurrencyConverter())
  }

  object default extends DefaultConversionImplicits

  object commodity
  {

    object VolumeToExponentialLengthConverter extends Converter[VolumeMeasure, ExponentialLengthMeasure]
    {
      import com.quantarray.skylark.measure.measures._

      override def apply(from: VolumeMeasure, to: ExponentialLengthMeasure): Option[Double] = Conversion(from, to) match
      {
        case `bbl` ⤇ `gal` => Some(42.0)
        case _ => None
      }
    }

    trait BaseCommodityConversionImplicits
    {

      implicit val volumeToExponentialLengthCanConvert = new CanConvert[VolumeMeasure, ExponentialLengthMeasure]
      {
        override def convert: Converter[VolumeMeasure, ExponentialLengthMeasure] = VolumeToExponentialLengthConverter
      }

    }

    object default extends BaseCommodityConversionImplicits

  }
}
