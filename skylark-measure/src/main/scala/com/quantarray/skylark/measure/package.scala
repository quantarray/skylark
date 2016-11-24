/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2016 Quantarray, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quantarray.skylark

package object measure extends DefaultMeasures
{
  measuresScope =>

  @Quantify[DefaultMeasures, Quantity[Double, _]](measuresScope)
  class QuantifiedMeasures(val value: Double)

  val * = ProductMeasure
  val / = RatioMeasure
  val ^ = ExponentialMeasure

  type ⤇[From, To] = Conversion[From, To]

  val ⤇ = Conversion

  object simplification
  {
    type EnergyPriceTimesCurrencyPriceMeasure = ProductMeasure[EnergyPrice, CurrencyPrice]

    trait DefaultSimplificationImplicits
    {

      implicit val energyPriceTimesCurrencyPriceCanSimplify = new CanSimplify[EnergyPriceTimesCurrencyPriceMeasure, Option[EnergyPrice]]
      {
        override def simplify(inflated: EnergyPriceTimesCurrencyPriceMeasure): Option[EnergyPrice] =
        {
          if (inflated.multiplicand.numerator == inflated.multiplier.denominator)
          {
            Some(RatioMeasure(inflated.multiplier.numerator, inflated.multiplicand.denominator))
          }
          else
          {
            None
          }
        }
      }

    }

    object default extends AnyRef with DefaultSimplificationImplicits

  }

  type ExponentialLengthMeasure = ExponentialMeasure[LengthMeasure]

  type Price[M <: Measure[M], N <: Measure[N]] = RatioMeasure[M, N]

  type EnergyPrice = RatioMeasure[Currency, EnergyMeasure]

  type CurrencyPrice = RatioMeasure[Currency, Currency]

  object commodity
  {

    object us
    {

      import composition._

      object commercial
      {

        object grains
        {

          object corn
          {

            object shelled
            {
              val bushel = "bushel" := 56 * lb
            }

          }

        }

      }

    }

  }

  object conversion
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
        override def apply(from: VolumeMeasure, to: ExponentialLengthMeasure): Option[Double] = Conversion(from, to) match
        {
          case `bbl` ⤇ `gal` => Some(42.0)
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

  object implicits extends AnyRef
                           with arithmetic.SafeArithmeticImplicits
                           with simplification.DefaultSimplificationImplicits
                           with conversion.DefaultConversionImplicits

}
