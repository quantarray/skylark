package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object conversion
{

  trait DefaultImplicits
  {
    implicit def defaultCanConvert[M <: Measure[M]] = CanConvert(SameTypeConverter[M])
  }

  trait DimensionessImplicits extends DefaultImplicits
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

  trait SameTypeImplicits extends DimensionessImplicits
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

  object default extends SameTypeImplicits
}
