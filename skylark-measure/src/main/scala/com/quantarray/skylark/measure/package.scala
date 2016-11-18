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
  val * = ProductMeasure
  val / = RatioMeasure
  val ^ = ExponentialMeasure

  type ⤇[From, To] = Conversion[From, To]

  val ⤇ = Conversion

  object simplification
  {
    type EnergyPriceTimesCurrencyPriceMeasure = ProductMeasure[EnergyPrice, CurrencyPrice]

    trait DefaultSimplification
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

    object default extends AnyRef with DefaultSimplification

  }

  case class NoDimension() extends Dimension[NoDimension]
  {
    override def toString: String = "\uD835\uDFD9"
  }

  val Dimensionless = NoDimension()

  case class TimeDimension() extends Dimension[TimeDimension]
  {
    override def toString: String = "Time"
  }

  val Time = TimeDimension()

  case class LengthDimension() extends Dimension[LengthDimension]
  {
    override def toString: String = "Length"
  }

  val Length = LengthDimension()

  case class MassDimension() extends Dimension[MassDimension]
  {
    override def toString: String = "Mass"
  }

  val Mass = MassDimension()

  case class TemperatureDimension() extends Dimension[TemperatureDimension]
  {
    override def toString: String = "Temperature"
  }

  val Temperature = TemperatureDimension()

  case class AmountDimension() extends Dimension[AmountDimension]
  {
    override def toString: String = "Amount"
  }

  val Amount = AmountDimension()

  case class ElectricCurrentDimension() extends Dimension[ElectricCurrentDimension]
  {
    override def toString: String = "Electric Current"
  }

  val ElectricCurrent = ElectricCurrentDimension()

  case class LuminousIntensityDimension() extends Dimension[LuminousIntensityDimension]
  {
    override def toString: String = "Luminous Intensity"
  }

  val LuminousIntensity = LuminousIntensityDimension()

  // E.g. digital information, such as bit
  case class InformationDimension() extends Dimension[InformationDimension]
  {
    override def toString: String = "Information"
  }

  val Information = InformationDimension()

  // E.g. Currency, such as USD
  case class MoneyDimension() extends Dimension[MoneyDimension]
  {
    override def toString: String = "Money"
  }

  val Money = MoneyDimension()

  type ForceDimension = RatioDimension[ProductDimension[MassDimension, LengthDimension], ExponentialDimension[TimeDimension]]

  val Force = (Mass * Length) / (Time ^ 2)

  type EnergyDimension = RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]], ExponentialDimension[TimeDimension]]

  val Energy = (Mass * (Length ^ 2)) / (Time ^ 2)

  type PowerDimension = RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]], ExponentialDimension[TimeDimension]]

  val Power = (Mass * (Length ^ 2)) / (Time ^ 3)

  type PressureDimension = RatioDimension[MassDimension, ProductDimension[LengthDimension, ExponentialDimension[TimeDimension]]]

  val Pressure = Mass / (Length * (Time ^ 2))

  type LuminousFluxDimension = ProductDimension[LuminousIntensityDimension, NoDimension]

  val LuminousFlux = LuminousIntensity * Dimensionless

  type VoltageDimension = RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]],
    ProductDimension[ExponentialDimension[TimeDimension], ElectricCurrentDimension]]

  val Voltage = (Mass * (Length ^ 2)) / ((Time ^ 3) * ElectricCurrent)

  type TemporalFrequencyDimension = RatioDimension[NoDimension, TimeDimension]

  val TemporalFrequency = Dimensionless / Time

  type SpatialFrequencyDimension = RatioDimension[NoDimension, LengthDimension]

  val SpatialFrequency = Dimensionless / Length

  type AngularFrequencyDimension = RatioDimension[NoDimension, TimeDimension]

  val AngularFrequency = TemporalFrequency

  type ElectricChargeDimension = ProductDimension[ElectricCurrentDimension, TimeDimension]

  val ElectricCharge = ElectricCurrent * Time

  /**
    * SI prefixes.
    */
  val Yotta = DecadicMultiple("Y", 1E24)

  val Zetta = DecadicMultiple("Z", 1E21)

  val Exa = DecadicMultiple("E", 1E18)

  val Peta = DecadicMultiple("P", 1E15)

  val Tera = DecadicMultiple("T", 1E12)

  val Giga = DecadicMultiple("G", 1E9)

  val Mega = DecadicMultiple("M", 1E6)

  val Kilo = DecadicMultiple("k", 1000)

  val Hecto = DecadicMultiple("h", 100)

  val Deka = DecadicMultiple("da", 10)

  val Deci = DecadicMultiple("d", 0.1)

  val Centi = DecadicMultiple("c", 0.01)

  val Milli = DecadicMultiple("m", 0.001)

  val Micro = DecadicMultiple("µ", 1E-6)

  val Nano = DecadicMultiple("n", 1E-9)

  val Pico = DecadicMultiple("p", 1E-12)

  val Femto = DecadicMultiple("f", 1E-15)

  val Atto = DecadicMultiple("a", 1E-18)

  val Zepto = DecadicMultiple("z", 1E-21)

  val Yocto = DecadicMultiple("y", 1E-24)

  /**
    * IEC (http://en.wikipedia.org/wiki/International_Electrotechnical_Commission) prefixes.
    */
  val Ki = BinaryMultiple("Ki", 10)

  val Mi = BinaryMultiple("Mi", 11)

  val Gi = BinaryMultiple("Gi", 12)

  val Ti = BinaryMultiple("Ti", 13)

  val Pi = BinaryMultiple("Pi", 14)

  val Ei = BinaryMultiple("Ei", 15)

  val Zi = BinaryMultiple("Zi", 16)

  val Yi = BinaryMultiple("Yi", 17)

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

  trait Units[@specialized(Double, Int) N] extends Any
  {
    implicit def qn: QuasiNumeric[N]

    def value: N

    /**
      * Composes a quantity of supplied measure.
      */
    def apply[M <: Measure[M]](measure: M): Quantity[N, M] = Quantity(value, measure)

    /**
      * Composes a quantity of supplied measure.
      */
    def *[M <: Measure[M]](measure: M): Quantity[N, M] = apply(measure)

    /**
      * Dimensionless.
      */
    def unit = apply(measure.Unit)

    def units = unit

    def percent = apply(measure.percent)

    def bp = apply(measure.bp)

    def rad = apply(measure.rad)

    def sr = apply(measure.sr)

    /**
      * Time.
      */
    def day = apply(measure.day)

    def days = day

    /**
      * Mass.
      */
    def g = apply(measure.g)

    def kg = apply(measure.kg)

    def cg = apply(measure.cg)

    def mg = apply(measure.mg)

    def t = apply(measure.t)

    def oz_metric = apply(measure.oz_metric)

    def oz = apply(measure.oz)

    def lb = apply(measure.lb)

    def mt = apply(measure.mt)

    def ton = apply(measure.ton)

    def gr = apply(measure.gr)

    def dwt = apply(measure.dwt)

    def lb_troy = apply(measure.lb_troy)

    def oz_troy = apply(measure.oz_troy)

    /**
      * Length.
      */
    def m = apply(measure.m)

    def in = apply(measure.in)

    def ft = apply(measure.ft)

    def yd = apply(measure.yd)

    /**
      * Area.
      */
    def km2 = apply(measure.km2)

    def ha = apply(measure.ha)

    /**
      * Volume.
      */
    def in3 = apply(measure.in3)

    def gal = apply(measure.gal)

    def bbl = apply(measure.bbl)

    /**
      * Energy.
      */
    def MMBtu = apply(measure.MMBtu)

    /**
      * Currency.
      */
    def USD = apply(measure.USD)
  }

  object implicits extends AnyRef
                           with arithmetic.SafeArithmeticImplicits
                           with simplification.DefaultSimplification
                           with conversion.DefaultConversionImplicits
  {

    implicit final class DoubleQuantity(val value: Double) extends AnyVal with Units[Double]
    {
      implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)
    }

    implicit final class IntQuantity(private val intValue: Int) extends AnyVal with Units[Double]
    {
      implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)

      override def value: Double = intValue
    }

  }

}
