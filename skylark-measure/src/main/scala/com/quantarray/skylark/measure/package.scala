/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2015 Quantarray, LLC
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
  // TODO: Transfer conversion constants to CanCoverts
  // TODO: Fix QuantitySpec

  case class NoDimension() extends Dimension[NoDimension]

  val Dimensionless = NoDimension()

  case class TimeDimension() extends Dimension[TimeDimension]

  val Time = TimeDimension()

  case class LengthDimension() extends Dimension[LengthDimension]

  val Length = LengthDimension()

  case class MassDimension() extends Dimension[MassDimension]

  val Mass = MassDimension()

  case class TemperatureDimension() extends Dimension[TemperatureDimension]

  val Temperature = TemperatureDimension()

  case class AmountDimension() extends Dimension[AmountDimension]

  val Amount = AmountDimension()

  case class ElectricCurrentDimension() extends Dimension[ElectricCurrentDimension]

  val ElectricCurrent = ElectricCurrentDimension()

  case class LuminousIntensityDimension() extends Dimension[LuminousIntensityDimension]

  val LuminousIntensity = LuminousIntensityDimension()

  // E.g. digital information, such as bit
  case class InformationDimension() extends Dimension[InformationDimension]

  val Information = InformationDimension()

  // E.g. Currency, such as USD
  case class MoneyDimension() extends Dimension[MoneyDimension]

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

  type VoltageType = RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]],
    ProductDimension[ExponentialDimension[TimeDimension],  ElectricCurrentDimension]]

  val Voltage = (Mass * (Length ^ 2)) / ((Time ^ 3) * ElectricCurrent)

  type TemporalFrequencyType = RatioDimension[measure.NoDimension, measure.TimeDimension]

  val TemporalFrequency = Dimensionless / Time

  val SpatialFrequency = Dimensionless / Length

  val AngularFrequency = TemporalFrequency

  val ElectricCharge = ElectricCurrent * Time

  /**
   * SI prefixes.
   */
  val Yotta = new DecadicMultiple("Y", 1E24)

  val Zetta = new DecadicMultiple("Z", 1E21)

  val Exa = new DecadicMultiple("E", 1E18)

  val Peta = new DecadicMultiple("P", 1E15)

  val Tera = new DecadicMultiple("T", 1E12)

  val Giga = new DecadicMultiple("G", 1E9)

  val Mega = new DecadicMultiple("M", 1E6)

  val Kilo = new DecadicMultiple("k", 1000.0)

  val Hecto = new DecadicMultiple("h", 100.0)

  val Deka = new DecadicMultiple("da", 10.0)

  val Deci = new DecadicMultiple("d", 0.1)

  val Centi = new DecadicMultiple("c", 0.01)

  val Milli = new DecadicMultiple("m", 0.001)

  val Micro = new DecadicMultiple("µ", 1E-6)

  val Nano = new DecadicMultiple("n", 1E-9)

  val Pico = new DecadicMultiple("p", 1E-12)

  val Femto = new DecadicMultiple("f", 1E-15)

  val Atto = new DecadicMultiple("a", 1E-18)

  val Zepto = new DecadicMultiple("z", 1E-21)

  val Yocto = new DecadicMultiple("y", 1E-24)

  /**
   * IEC (http://en.wikipedia.org/wiki/International_Electrotechnical_Commission) prefixes.
   */
  val Ki = new BinaryMultiple("Ki", 10)

  val Mi = new BinaryMultiple("Mi", 11)

  val Gi = new BinaryMultiple("Gi", 12)

  val Ti = new BinaryMultiple("Ti", 13)

  val Pi = new BinaryMultiple("Pi", 14)

  val Ei = new BinaryMultiple("Ei", 15)

  val Zi = new BinaryMultiple("Zi", 16)

  val Yi = new BinaryMultiple("Yi", 17)

  val UnitMeasure = DimensionlessMeasure("1", Universal())

  implicit object MassCanDivide extends CanDivide[MassMeasure, MassMeasure, DimensionlessMeasure]
  {
    override def divide(n: MassMeasure, d: MassMeasure): DimensionlessMeasure = UnitMeasure
  }

  implicit object MassCanExponentiate extends CanExponentiate[MassMeasure, ExponentialMeasure[MassMeasure]]
  {
    override def pow(base: MassMeasure, exponent: Double): ExponentialMeasure[MassMeasure] = ExponentialMeasure(base, exponent)
  }

  implicit object MassTimeCanMultiply extends CanMultiply[MassMeasure, TimeMeasure, ProductMeasure[MassMeasure, TimeMeasure]]
  {
    override def times(multiplicand: MassMeasure, multiplier: TimeMeasure): ProductMeasure[MassMeasure, TimeMeasure] = ProductMeasure(multiplicand, multiplier)
  }

  implicit object MassLengthCanMultiply extends CanMultiply[MassMeasure, LengthMeasure, ProductMeasure[MassMeasure, LengthMeasure]]
  {
    override def times(multiplicand: MassMeasure, multiplier: LengthMeasure): ProductMeasure[MassMeasure, LengthMeasure] =
      ProductMeasure(multiplicand, multiplier)
  }

  implicit object MassLengthCanDivide extends CanDivide[MassMeasure, LengthMeasure, RatioMeasure[MassMeasure, LengthMeasure]]
  {
    override def divide(n: MassMeasure, d: LengthMeasure): RatioMeasure[MassMeasure, LengthMeasure] = RatioMeasure(n, d)
  }

  implicit object MassDimensionlessCanMultiply extends CanMultiply[MassMeasure, DimensionlessMeasure, MassMeasure]
  {
    override def times(multiplicand: MassMeasure, multiplier: DimensionlessMeasure): MassMeasure = multiplicand

    override def unit(multiplicand: MassMeasure, multiplier: DimensionlessMeasure): Double = multiplier.base
  }

  implicit object LengthCanExponentiate extends CanExponentiate[LengthMeasure, ExponentialMeasure[LengthMeasure]]
  {
    override def pow(base: LengthMeasure, exponent: Double): ExponentialMeasure[LengthMeasure] = ExponentialMeasure(base, exponent)
  }

  implicit object CurrencyEnergyCanDivide extends CanDivide[Currency, EnergyMeasure, RatioMeasure[Currency, EnergyMeasure]]
  {
    override def divide(n: Currency, d: EnergyMeasure): RatioMeasure[Currency, EnergyMeasure] = RatioMeasure(n, d)
  }

  implicit object CurrencyDimensionlessCanDivide extends CanDivide[Currency, DimensionlessMeasure, Currency]
  {
    override def divide(n: Currency, d: DimensionlessMeasure): Currency = n

    override def unit(n: Currency, d: DimensionlessMeasure): Double = 1 / d.base
  }

  /**
   * Dimensionless.
   */
  val percent = UnitMeasure.composes("%", 0.01)

  // http://en.wikipedia.org/wiki/Basis_point
  val bp = UnitMeasure.composes("bp", 0.0001)

  // http://en.wikipedia.org/wiki/Radian
  val rad = DimensionlessMeasure("rad", Derived(SI), 1 / (2 * scala.math.Pi))

  // http://en.wikipedia.org/wiki/Steradian
  val sr = DimensionlessMeasure("sr", Derived(SI), 1 / (4 * scala.math.Pi))

  /**
   * Time.
   */
  val s = TimeMeasure("s", SI)
  val min = s.composes("min", 60)
  val h = min.composes("h", 60.0)
  val day = h.composes("day", 24.0)
  val year365 = day.composes("Year[365]", 365.0)
  val year360 = day.composes("Year[360]", 360.0)

  val ms = Milli * s
  val ns = Nano * s

  val fortnight = day.composes("Fortnight", Imperial(), 14.0)

  /**
   * Mass.
   */
  val g = MassMeasure("g", SI)
  val kg = Kilo * g
  val cg = Centi * g
  val mg = Milli * g
  val t = MassMeasure("Tonnne", SI)
  val oz_metric = MassMeasure("Metric Ounce", SI) // http://en.wikipedia.org/wiki/Ounce#Metric_ounces

  val oz = MassMeasure("Ounce", US)
  val lb = MassMeasure("Pound", US)
  // http://en.wikipedia.org/wiki/Short_ton
  val ton = MassMeasure("Ton", US)

  // http://en.wikipedia.org/wiki/Grain_(unit)
  val gr = MassMeasure("Grain", Imperial())
  // http://en.wikipedia.org/wiki/Pennyweight
  val dwt = MassMeasure("Pennyweight", Imperial())
  val oz_troy = MassMeasure("Troy Ounce", Imperial())
  val lb_troy = MassMeasure("Troy Pound", Imperial())

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

  val in = LengthMeasure("Inch", Imperial())
  val ft = in.composes("Foot", 12.0)
  val yd = ft.composes("Yard", 3.0)
  val rd = ft.composes("Rod", 16.5)
  val fur = rd.composes("Furlong", 40.0)
  val mi = fur.composes("Mile", 132.0)

  val nmi = m.composes("Nautical mile", 1852.0)

  // http://en.wikipedia.org/wiki/Thou_(length)
  val thou = in.composes("Thou", 0.001)

  //http://en.wikipedia.org/wiki/Astronomical_unit
  val astronomicalUnit = m.composes("au", 149597870700.0)
  val au = astronomicalUnit

  // http://en.wikipedia.org/wiki/Light-year
  val lightYear = m.composes("ly", 9460730472580800.0)
  val ly = lightYear

  // http://en.wikipedia.org/wiki/Parsec
  val parsec = au.composes("pc", 648000.0 / scala.math.Pi)
  val pc = parsec

  // http://en.wikipedia.org/wiki/List_of_unusual_units_of_measurement#Siriometer
  val siriometer = au.composes("Siriometer", 1E6)

  // http://en.wikipedia.org/wiki/List_of_humorous_units_of_measurement#Beard-second
  val beardSecond = nm.composes("Beard-second", 5.0)

  /**
   * Area.
   */
  val m2 = m ^ 2
  val km2 = km ^ 2
  // Hectometer
  val hm2 = m ^ 2
  val ha = m2.composes("Hectare", 10000.0)

  val ft2 = ft ^ 2
  val acre = ft2.composes("Acre", 43560.0)

  /**
   * Volume.
   */
  val m3 = m ^ 3
  val cm3 = cm ^ 3
  val liter = m3.composes("Liter", 0.001)

  val in3 = in ^ 3

  // Liquid
  val pi_liquid = in3.composes("Pint", 28.875)
  val qt_liquid = pi_liquid.composes("Quart", 2.0)
  val gal = qt_liquid.composes("gal", US, 4.0)

  val bbl = VolumeMeasure("bbl", Imperial())

  // Dry
  val pi_dry = in3.composes("Pint", US, 33.6003125)
  val qt_dry = pi_dry.composes("Quart", 2.0)
  val peck = qt_dry.composes("Peck", 8.0)
  val bushel = peck.composes("Bushel", 4.0)

  /**
   * Force.
   */
  val N = ForceMeasure("Newton", SI)

  val kip = Kilo * lb // http://en.wikipedia.org/wiki/Kip_(unit)

  /**
   * Power.
   */
  val W = PowerMeasure("Watt", SI)
  val MW: PowerMeasure = Mega * W
  val GW: PowerMeasure = Giga * W

  /**
   * Energy.
   */
  val J = EnergyMeasure("Joule", Derived(SI))
  val GJ = Giga * J

  val MMBtu = EnergyMeasure("MMBtu", Imperial())

  /**
   * Pressure.
   */
  val Pa = PressureMeasure("Pascal", Derived(SI))
  val kPa = Kilo * Pa

  /**
   * Electric current.
   */
  val A = ElectricCurrentMeasure("Ampere", SI)

  /**
   * Luminous intensity.
   */
  val cd = LuminousIntensityMeasure("Candela", SI)

  /**
   * Information.
   */
  val b = InformationMeasure("Bit", SI)
  val B = b.composes("Byte", 8.0)

  /**
   * Luminous flux.
   */
  val lm = LuminousFluxMeasure("Lumen", Derived(SI))

  /**
   * Frequency.
   */
  val Hz = TemporalFrequencyMeasure("Hz", Derived(SI))

  type Price[M <: Measure[M]] = RatioMeasure[Currency, M]

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
      case (`gal`, `in3`) => Some(231)
      case (`ha`, `km2`) => Some(0.01)
      case _ => super.apply(from, to)
    }
  }

  implicit object ExponentialLengthCanConvert extends CanConvert[ExponentialLengthMeasure, ExponentialLengthMeasure]
  {
    override def convert: Converter[ExponentialLengthMeasure, ExponentialLengthMeasure] = ExponentialLengthConverter
  }
}
