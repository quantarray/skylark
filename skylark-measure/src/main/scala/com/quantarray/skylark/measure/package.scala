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

package object measure
{
  type ⤇[From, To] = Conversion[From, To]

  val ⤇ = Conversion

  object composition
  {

    implicit final class IntQuantity(private val value: Int) extends AnyVal
    {
      def *[M <: Measure[M]](measure: M): (Double, M) = (value, measure)
    }

    implicit final class DoubleQuantity(private val value: Double) extends AnyVal
    {
      def *[M <: Measure[M]](measure: M): (Double, M) = (value, measure)
    }

    implicit final class StringMeasureComposition(private val name: String) extends AnyVal
    {
      def :=[M <: Measure[M]](quantity: (Double, M)): M = quantity._2.composes(name, quantity._1)

      def :=[M <: Measure[M]](measure: M): M = measure.composes(name, 1.0)
    }

  }

  object arithmetic
  {

    trait SafeArithmeticImplicits
    {
      implicit def productCanMultiply[M1 <: Measure[M1], M2 <: Measure[M2]] = new CanMultiply[M1, M2, ProductMeasure[M1, M2]]
      {
        override def times(multiplicand: M1, multiplier: M2): ProductMeasure[M1, M2] = ProductMeasure(multiplicand, multiplier)
      }

      implicit def ratioCanDivide[N <: Measure[N], D <: Measure[D]] = new CanDivide[N, D, RatioMeasure[N, D]]
      {
        override def divide(numerator: N, denominator: D): RatioMeasure[N, D] = RatioMeasure(numerator, denominator)
      }

      implicit def exponentialCanExponentiate[B <: Measure[B]] = new CanExponentiate[B, ExponentialMeasure[B]]
      {
        override def pow(base: B, exponent: Double): ExponentialMeasure[B] = ExponentialMeasure(base, exponent)
      }

      implicit def lhsCanAdd[M1 <: Measure[M1], M2 <: Measure[M2]] = new CanAdd[M1, M2]
      {
        type R = M1

        override def plus(addend1: M1, addend2: M2): R = addend1
      }

      implicit def lhsCanAddQuantity[M <: Measure[M], A1 <: Quantity[Double, M], A2 <: Quantity[Double, M]] = new CanAddQuantity[Double, M, A1, M, A2, M]
      {
        type R = M

        type QR = Option[Quantity[Double, M]]

        override def plus(addend1: M, addend2: M): M = addend1

        override def plus(addend1: A1, addend2: A2)(implicit cc1: CanConvert[M, M], cc2: CanConvert[M, M]): QR =
        {
          val targetMeasure = plus(addend1.measure, addend2.measure)

          val a1 = cc1.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
          val a2 = cc2.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

          a1.flatMap(aa1 => a2.map(_ + aa1)).map(v => Quantity(v, targetMeasure))
        }
      }
    }

    object safe extends SafeArithmeticImplicits

    object unsafe extends SafeArithmeticImplicits
    {
      implicit def lhsCanAddQuantityUnsafe[M <: Measure[M], A1 <: Quantity[Double, M], A2 <: Quantity[Double, M]] = new CanAddQuantity[Double, M, A1, M, A2, M]
      {
        type R = M

        type QR = Quantity[Double, M]

        override def plus(addend1: M, addend2: M): M = addend1

        override def plus(addend1: A1, addend2: A2)(implicit cc1: CanConvert[M, M], cc2: CanConvert[M, M]): QR =
        {
          val targetMeasure = plus(addend1.measure, addend2.measure)

          val a1 = cc1.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
          val a2 = cc2.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

          (a1, a2) match
          {
            case (Some(aa1), Some(aa2)) => Quantity(aa1 + aa2, targetMeasure)
            case (Some(_), _) => throw ConvertException(addend2.measure, targetMeasure)
            case (_, Some(_)) => throw ConvertException(addend1.measure, targetMeasure)
            case _ => throw ConvertException(s"Cannot convert to $targetMeasure.")
          }
        }
      }
    }

  }

  object simplification
  {
    type EnergyPriceTimesCurrencyPriceMeasure = ProductMeasure[EnergyPrice, CurrencyPrice]

    trait DefaultSimplification
    {

      implicit object EnergyPriceTimesCurrencyPriceCanSimplify extends CanSimplify[EnergyPriceTimesCurrencyPriceMeasure, Option[EnergyPrice]]
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

  final val Unit = DimensionlessMeasure("\uD835\uDFD9", Universal())

  type ExponentialLength = ExponentialMeasure[LengthMeasure]

  type Price[M <: Measure[M], N <: Measure[N]] = RatioMeasure[M, N]

  type EnergyPrice = RatioMeasure[Currency, EnergyMeasure]

  type CurrencyPrice = RatioMeasure[Currency, Currency]

  import arithmetic.safe._
  import composition._

  /**
    * Dimensionless.
    */
  val percent = "%" := 0.01 * Unit

  // http://en.wikipedia.org/wiki/Basis_point
  val bp = "bp" := 0.0001 * Unit

  // http://en.wikipedia.org/wiki/Radian
  val rad = DimensionlessMeasure("rad", Derived(SI), 1 / (2 * scala.math.Pi))

  // http://en.wikipedia.org/wiki/Steradian
  val sr = DimensionlessMeasure("sr", Derived(SI), 1 / (4 * scala.math.Pi))

  /**
    * Time.
    */
  val s = TimeMeasure("s", SI)
  val (sec, secs) = (s, s)
  val min = "min" := 60 * s
  val mins = min
  val h = "h" := 60 * min
  val (hour, hours) = (h, h)
  val day = "day" := 24 * h
  val days = day
  val year365 = "Year[365]" := 365 * day
  val years = year365
  val year360 = "Year[360]" := 360 * day

  val ms = Milli * s
  val ns = Nano * s

  val fortnight = day.composes("Fortnight", Imperial(), 14)

  /**
    * Mass.
    */
  val g = MassMeasure("g", SI)
  val kg = Kilo * g
  val cg = Centi * g
  val mg = Milli * g
  val t = MassMeasure("t", SI)
  val oz_metric = MassMeasure("metric oz", SI) // http://en.wikipedia.org/wiki/Ounce#Metric_ounces

  val oz = MassMeasure("oz", US)
  val lb = MassMeasure("lb", US)

  val mt = "mt" := 2204.625 * lb
  val ton = mt

  // http://en.wikipedia.org/wiki/Grain_(unit)
  val gr = MassMeasure("grain", Imperial())
  // http://en.wikipedia.org/wiki/Pennyweight
  val dwt = MassMeasure("dwt", Imperial())
  val oz_troy = MassMeasure("troy oz", Imperial())
  val lb_troy = MassMeasure("troy lb", Imperial())

  /**
    * Length.
    */
  val m = LengthMeasure("m", SI)
  val km = Kilo * m
  val hm = Hecto * m
  val dam = Deka * m
  val dm = Deci * m
  val cm = Centi * m
  val mm = Milli * m
  val nm = Nano * m

  val in = LengthMeasure("in", Imperial())
  val ft = "ft" := 12 * in
  val yd = "yd" := 3 * ft
  val rd = "rod" := 16.5 * ft
  val fur = "fur" := 40 * rd
  val mi = "mi" := 132 * fur

  val nmi = "nmi" := 1852 * m

  // http://en.wikipedia.org/wiki/Thou_(length)
  val thou = "thou" := 0.001 * in

  //http://en.wikipedia.org/wiki/Astronomical_unit
  val au = "au" := 149597870700.0 * m

  // http://en.wikipedia.org/wiki/Light-year
  val ly = "ly" := 9460730472580800.0 * m

  // http://en.wikipedia.org/wiki/Parsec
  val parsec = au.composes("pc", 648000.0 / scala.math.Pi)
  val pc = parsec

  // http://en.wikipedia.org/wiki/List_of_unusual_units_of_measurement#Siriometer
  val siriometer = au.composes("Siriometer", 1E6)

  // http://en.wikipedia.org/wiki/List_of_humorous_units_of_measurement#Beard-second
  val beardSecond = nm.composes("Beard-second", 5)

  /**
    * Area.
    */
  val m2 = m ^ 2
  val km2 = km ^ 2
  // Hectometer
  val hm2 = m ^ 2
  val ha = m2.composes("Hectare", 10000)

  val ft2 = ft ^ 2
  val acre = ft2.composes("Acre", 43560)

  /**
    * Volume.
    */
  val m3 = m ^ 3
  val cm3 = cm ^ 3
  val liter = "liter" := 0.001 * m3

  val in3 = in ^ 3

  // Liquid
  val pi_liquid = in3.composes("pint", 28.875)
  val qt_liquid = pi_liquid.composes("quart", 2.0)
  val gal = qt_liquid.composes("gal", US, 4.0)

  val bbl = VolumeMeasure("bbl", Imperial())

  // Dry
  val pi_dry = in3.composes("pint", US, 33.6003125)
  val qt_dry = pi_dry.composes("quart", 2)
  val peck = qt_dry.composes("peck", 8)
  val bushel = peck.composes("bushel", 4)

  /**
    * Force.
    */
  val N = ForceMeasure("N", SI)

  val kip = Kilo * lb // http://en.wikipedia.org/wiki/Kip_(unit)

  /**
    * Power.
    */
  val W = PowerMeasure("W", SI)
  val kW = Kilo * W
  val MW = Mega * W
  val GW = Giga * W

  /**
    * Energy.
    */
  val J = EnergyMeasure("J", Derived(SI))
  val kJ = Kilo * J
  val MJ = Mega * J
  val GJ = Giga * J

  val MMBtu = EnergyMeasure("MMBtu", Imperial())

  /**
    * Pressure.
    */
  val Pa = PressureMeasure("Pa", Derived(SI))
  val kPa = Kilo * Pa
  val MPa = Mega * Pa
  val GPa = Giga * Pa

  /**
    * Electric current.
    */
  val A = ElectricCurrentMeasure("A", SI)

  /**
    * Luminous intensity.
    */
  val cd = LuminousIntensityMeasure("cd", SI)

  /**
    * Information.
    */
  // Bit
  val b = InformationMeasure("b", SI)
  // Byte
  val B = "B" := 8 * b

  /**
    * Luminous flux.
    */
  // Lumen
  val lm = LuminousFluxMeasure("lm", Derived(SI))

  /**
    * Frequency.
    */
  val Hz = TemporalFrequencyMeasure("Hz", Derived(SI))

  /**
    * Currency.
    */
  // United Arab Emirates dirham
  val AED = Currency("AED")
  // Afghan afghani
  val AFN = Currency("AFN")
  // Albanian lek
  val ALL = Currency("ALL")
  // Armenian dram
  val AMD = Currency("AMD")
  // Netherlands Antillean guilder
  val ANG = Currency("ANG")
  // Angolan kwanza
  val AOA = Currency("AOA")
  // Argentine peso
  val ARS = Currency("ARS")
  // Australian dollar
  val AUD = Currency("AUD")
  // Aruban florin
  val AWG = Currency("AWG")
  // Azerbaijani manat
  val AZN = Currency("AZN")
  // Bosnia and Herzegovina convertible mark
  val BAM = Currency("BAM")
  // Barbados dollar
  val BBD = Currency("BBD")
  // Bangladeshi taka
  val BDT = Currency("BDT")
  // Bulgarian lev
  val BGN = Currency("BGN")
  // Bahraini dinar
  val BHD = Currency("BHD")
  // Burundian franc
  val BIF = Currency("BIF")
  // Bermudian dollar
  val BMD = Currency("BMD")
  // Brunei dollar
  val BND = Currency("BND")
  // Boliviano
  val BOB = Currency("BOB")
  // Bolivian Mvdol (funds code)
  val BOV = Currency("BOV")
  // Brazilian real
  val BRL = Currency("BRL")
  // Bahamian dollar
  val BSD = Currency("BSD")
  // Bhutanese ngultrum
  val BTN = Currency("BTN")
  // Botswana pula
  val BWP = Currency("BWP")
  // Belarusian ruble
  val BYR = Currency("BYR")
  // Belize dollar
  val BZD = Currency("BZD")
  // Canadian dollar
  val CAD = Currency("CAD")
  // Congolese franc
  val CDF = Currency("CDF")
  // Swiss franc
  val CHF = Currency("CHF")
  // Unidad de Fomento (funds code)
  val CLF = Currency("CLF")
  // Chilean peso
  val CLP = Currency("CLP")
  // Chinese yuan
  val CNY = Currency("CNY")
  // Colombian peso
  val COP = Currency("COP")
  // Costa Rican colon
  val CRC = Currency("CRC")
  // Cuban convertible peso
  val CUC = Currency("CUC")
  // Cuban peso
  val CUP = Currency("CUP")
  // Cape Verde escudo
  val CVE = Currency("CVE")
  // Czech koruna
  val CZK = Currency("CZK")
  // Djiboutian franc
  val DJF = Currency("DJF")
  // Danish krone
  val DKK = Currency("DKK")
  // Dominican peso
  val DOP = Currency("DOP")
  // Algerian dinar
  val DZD = Currency("DZD")
  // Egyptian pound
  val EGP = Currency("EGP")
  // Eritrean nakfa
  val ERN = Currency("ERN")
  // Ethiopian birr
  val ETB = Currency("ETB")
  // Euro
  val EUR = Currency("EUR")
  // Fiji dollar
  val FJD = Currency("FJD")
  // Falkland Islands pound
  val FKP = Currency("FKP")
  // Pound sterling
  val GBP = Currency("GBP")
  // Georgian lari
  val GEL = Currency("GEL")
  // Ghanaian cedi
  val GHS = Currency("GHS")
  // Gibraltar pound
  val GIP = Currency("GIP")
  // Gambian dalasi
  val GMD = Currency("GMD")
  // Guinean franc
  val GNF = Currency("GNF")
  // Guatemalan quetzal
  val GTQ = Currency("GTQ")
  // Guyanese dollar
  val GYD = Currency("GYD")
  // Hong Kong dollar
  val HKD = Currency("HKD")
  // Honduran lempira
  val HNL = Currency("HNL")
  // Croatian kuna
  val HRK = Currency("HRK")
  // Haitian gourde
  val HTG = Currency("HTG")
  // Hungarian forint
  val HUF = Currency("HUF")
  // Indonesian rupiah
  val IDR = Currency("IDR")
  // Israeli new shekel
  val ILS = Currency("ILS")
  // Indian rupee
  val INR = Currency("INR")
  // Iraqi dinar
  val IQD = Currency("IQD")
  // Iranian rial
  val IRR = Currency("IRR")
  // Icelandic króna
  val ISK = Currency("ISK")
  // Jamaican dollar
  val JMD = Currency("JMD")
  // Jordanian dinar
  val JOD = Currency("JOD")
  // Japanese yen
  val JPY = Currency("JPY")
  // Kenyan shilling
  val KES = Currency("KES")
  // Kyrgyzstani som
  val KGS = Currency("KGS")
  // Cambodian riel
  val KHR = Currency("KHR")
  // Comoro franc
  val KMF = Currency("KMF")
  // North Korean won
  val KPW = Currency("KPW")
  // South Korean won
  val KRW = Currency("KRW")
  // Kuwaiti dinar
  val KWD = Currency("KWD")
  // Cayman Islands dollar
  val KYD = Currency("KYD")
  // Kazakhstani tenge
  val KZT = Currency("KZT")
  // Lao kip
  val LAK = Currency("LAK")
  // Lebanese pound
  val LBP = Currency("LBP")
  // Sri Lankan rupee
  val LKR = Currency("LKR")
  // Liberian dollar
  val LRD = Currency("LRD")
  // Lesotho loti
  val LSL = Currency("LSL")
  // Lithuanian litas
  val LTL = Currency("LTL")
  // Latvian lats
  val LVL = Currency("LVL")
  // Libyan dinar
  val LYD = Currency("LYD")
  // Moroccan dirham
  val MAD = Currency("MAD")
  // Moldovan leu
  val MDL = Currency("MDL")
  // Malagasy ariary
  val MGA = Currency("MGA")
  // Macedonian denar
  val MKD = Currency("MKD")
  // Myanma kyat
  val MMK = Currency("MMK")
  // Mongolian tugrik
  val MNT = Currency("MNT")
  // Macanese pataca
  val MOP = Currency("MOP")
  // Mauritanian ouguiya
  val MRO = Currency("MRO")
  // Mauritian rupee
  val MUR = Currency("MUR")
  // Maldivian rufiyaa
  val MVR = Currency("MVR")
  // Malawian kwacha
  val MWK = Currency("MWK")
  // Mexican peso
  val MXN = Currency("MXN")
  // Mexican Unidad de Inversion (UDI) (funds code)
  val MXV = Currency("MXV")
  // Malaysian ringgit
  val MYR = Currency("MYR")
  // Mozambican metical
  val MZN = Currency("MZN")
  // Namibian dollar
  val NAD = Currency("NAD")
  // Nigerian naira
  val NGN = Currency("NGN")
  // Nicaraguan córdoba
  val NIO = Currency("NIO")
  // Norwegian krone
  val NOK = Currency("NOK")
  // Nepalese rupee
  val NPR = Currency("NPR")
  // New Zealand dollar
  val NZD = Currency("NZD")
  // Omani rial
  val OMR = Currency("OMR")
  // Panamanian balboa
  val PAB = Currency("PAB")
  // Peruvian nuevo sol
  val PEN = Currency("PEN")
  // Papua New Guinean kina
  val PGK = Currency("PGK")
  // Philippine peso
  val PHP = Currency("PHP")
  // Pakistani rupee
  val PKR = Currency("PKR")
  // Polish złoty
  val PLN = Currency("PLN")
  // Paraguayan guaraní
  val PYG = Currency("PYG")
  // Qatari riyal
  val QAR = Currency("QAR")
  // Romanian new leu
  val RON = Currency("RON")
  // Serbian dinar
  val RSD = Currency("RSD")
  // Russian rouble
  val RUB = Currency("RUB")
  // Rwandan franc
  val RWF = Currency("RWF")
  // Saudi riyal
  val SAR = Currency("SAR")
  // Solomon Islands dollar
  val SBD = Currency("SBD")
  // Seychelles rupee
  val SCR = Currency("SCR")
  // Sudanese pound
  val SDG = Currency("SDG")
  // Swedish krona/kronor
  val SEK = Currency("SEK")
  // Singapore dollar
  val SGD = Currency("SGD")
  // Saint Helena pound
  val SHP = Currency("SHP")
  // Sierra Leonean leone
  val SLL = Currency("SLL")
  // Somali shilling
  val SOS = Currency("SOS")
  // Surinamese dollar
  val SRD = Currency("SRD")
  // São Tomé and Príncipe dobra
  val STD = Currency("STD")
  // Syrian pound
  val SYP = Currency("SYP")
  // Swazi lilangeni
  val SZL = Currency("SZL")
  // Thai baht
  val THB = Currency("THB")
  // Tajikistani somoni
  val TJS = Currency("TJS")
  // Turkmenistani manat
  val TMT = Currency("TMT")
  // Tunisian dinar
  val TND = Currency("TND")
  // Tongan paʻanga
  val TOP = Currency("TOP")
  // Turkish lira
  val TRY = Currency("TRY")
  // Trinidad and Tobago dollar
  val TTD = Currency("TTD")
  // New Taiwan dollar
  val TWD = Currency("TWD")
  // Tanzanian shilling
  val TZS = Currency("TZS")
  // Ukrainian hryvnia
  val UAH = Currency("UAH")
  // Ugandan shilling
  val UGX = Currency("UGX")
  // United States dollar
  val USD = Currency("USD")
  // United States dollar (next day) (funds code)
  val USN = Currency("USN")
  // United States dollar (same day) (funds code)
  val USS = Currency("USS")
  // Uruguayan peso
  val UYU = Currency("UYU")
  // Uzbekistan som
  val UZS = Currency("UZS")
  // Venezuelan bolívar
  val VEF = Currency("VEF")
  // Vietnamese dong
  val VND = Currency("VND")
  // Vanuatu vatu
  val VUV = Currency("VUV")
  // Samoan tala
  val WST = Currency("WST")
  // CFA franc BEAC
  val XAF = Currency("XAF")
  // Silver (one troy ounce)
  val XAG = Currency("XAG")
  // Gold (one troy ounce)
  val XAU = Currency("XAU")
  // European Composite Unit (EURCO) (bond market unit)
  val XBA = Currency("XBA")
  // European Monetary Unit (E.M.U.-6) (bond market unit)
  val XBB = Currency("XBB")
  // European Unit of Account 9 (E.U.A.-9) (bond market unit)
  val XBC = Currency("XBC")
  // European Unit of Account 17 (E.U.A.-17) (bond market unit)
  val XBD = Currency("XBD")
  // East Caribbean dollar
  val XCD = Currency("XCD")
  // Special drawing rights
  val XDR = Currency("XDR")
  // UIC franc (special settlement currency)
  val XFU = Currency("XFU")
  // CFA franc BCEAO
  val XOF = Currency("XOF")
  // Palladium (one troy ounce)
  val XPD = Currency("XPD")
  // CFP franc (Franc du Pacifique)
  val XPF = Currency("XPF")
  // Platinum (one troy ounce)
  val XPT = Currency("XPT")
  // Code reserved for testing purposes
  val XTS = Currency("XTS")
  // No currency
  val XXX = Currency("XXX")
  // Yemeni rial
  val YER = Currency("YER")
  // South African rand
  val ZAR = Currency("ZAR")
  // Zambian kwacha
  val ZMW = Currency("ZMW")

  // United States cent
  val USC = "USC" := 0.01 * USD

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

      object VolumeToExponentialLengthConverter extends Converter[VolumeMeasure, ExponentialLength]
      {
        override def apply(from: VolumeMeasure, to: ExponentialLength): Option[Double] = Conversion(from, to) match
        {
          case `bbl` ⤇ `gal` => Some(42.0)
        }
      }

      trait BaseCommodityConversionImplicits
      {

        implicit object VolumeToExponentialLengthCanConvert extends CanConvert[VolumeMeasure, ExponentialLength]
        {
          override def convert: Converter[VolumeMeasure, ExponentialLength] = VolumeToExponentialLengthConverter
        }

      }

      object default extends BaseCommodityConversionImplicits

    }

  }

  trait Measures[@specialized(Double, Int) N] extends Any
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

    implicit final class DoubleQuantity(val value: Double) extends AnyVal with Measures[Double]
    {
      implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.DoubleQuasiNumeric)
    }

    implicit final class IntQuantity(private val intValue: Int) extends AnyVal with Measures[Double]
    {
      implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.DoubleQuasiNumeric)

      override def value: Double = intValue
    }

  }

}
