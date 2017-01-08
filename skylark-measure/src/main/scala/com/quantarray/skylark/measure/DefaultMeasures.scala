/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2017 Quantarray, LLC
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

package com.quantarray.skylark.measure

/**
  * Default measures.
  *
  * @author Araik Grigoryan
  */
trait DefaultMeasures extends DefaultDimensions
{
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

  import composition._
  import arithmetic.safe._

  /**
    * Dimensionless.
    */
  final val Unit = DimensionlessMeasure.Unit

  final val percent: DimensionlessMeasure = "%" := 0.01 * Unit

  // http://en.wikipedia.org/wiki/Basis_point
  final val bp: DimensionlessMeasure = "bp" := 0.0001 * Unit

  // http://en.wikipedia.org/wiki/Radian
  final val rad = DimensionlessMeasure("rad", Derived(SI), 1 / (2 * scala.math.Pi))

  // http://en.wikipedia.org/wiki/Steradian
  final val sr = DimensionlessMeasure("sr", Derived(SI), 1 / (4 * scala.math.Pi))

  /**
    * Time.
    */
  final val s = TimeMeasure("s", SI)
  final val sec: TimeMeasure = s
  final val second: TimeMeasure = s
  final val secs: TimeMeasure = s
  final val seconds: TimeMeasure = s

  final val min: TimeMeasure = "min" := 60 * s
  final val minute: TimeMeasure = min
  final val mins: TimeMeasure = min
  final val minutes: TimeMeasure = min

  final val h: TimeMeasure = "h" := 60 * min
  final val hour: TimeMeasure = h
  final val hours: TimeMeasure = h

  final val day: TimeMeasure = "day" := 24 * h
  final val days: TimeMeasure = day

  final val year365: TimeMeasure = "Year[365]" := 365 * day
  final val years365: TimeMeasure = year365
  final val year: TimeMeasure = year365
  final val years: TimeMeasure = year
  final val year360: TimeMeasure = "Year[360]" := 360 * day
  final val years360: TimeMeasure = year360

  final val ms: TimeMeasure = Milli * s
  final val msec: TimeMeasure = ms
  final val millisec: TimeMeasure = ms
  final val millisecond: TimeMeasure = ms
  final val msecs: TimeMeasure = ms
  final val millisecs: TimeMeasure = ms
  final val milliseconds: TimeMeasure = ms

  final val μs: TimeMeasure = Micro * s
  final val μsec: TimeMeasure = μs
  final val μsecond: TimeMeasure = μs
  final val microsec: TimeMeasure = μs
  final val microsecond: TimeMeasure = μs
  final val μsecs: TimeMeasure = μs
  final val μseconds: TimeMeasure = μs
  final val microsecs: TimeMeasure = μs
  final val microseconds: TimeMeasure = μs

  final val ns: TimeMeasure = Nano * s
  final val nsec: TimeMeasure = ns
  final val nanosec: TimeMeasure = ns
  final val nanosecond: TimeMeasure = ns
  final val nsecs: TimeMeasure = ns
  final val nanosecs: TimeMeasure = ns
  final val nanoseconds: TimeMeasure = ns

  final val fortnight: TimeMeasure = day.composes("Fortnight", Imperial(), 14)

  /**
    * Mass.
    */
  final val g = MassMeasure("g", SI)
  final val kg: MassMeasure = Kilo * g
  final val cg: MassMeasure = Centi * g
  final val mg: MassMeasure = Milli * g
  final val t = MassMeasure("t", SI)
  final val oz_metric = MassMeasure("metric oz", SI) // http://en.wikipedia.org/wiki/Ounce#Metric_ounces

  final val oz = MassMeasure("oz", US)
  final val lb = MassMeasure("lb", US)

  final val mt: MassMeasure = "mt" := 2204.625 * lb
  final val ton: MassMeasure = mt

  final val gr = MassMeasure("grain", Imperial()) // http://en.wikipedia.org/wiki/Grain_(unit)

  final val dwt = MassMeasure("dwt", Imperial())   // http://en.wikipedia.org/wiki/Pennyweight

  final val oz_troy = MassMeasure("troy oz", Imperial())
  final val lb_troy = MassMeasure("troy lb", Imperial())

  /**
    * Length.
    */
  final val m = LengthMeasure("m", SI)
  final val km: LengthMeasure = Kilo * m
  final val hm: LengthMeasure = Hecto * m
  final val dam: LengthMeasure = Deka * m
  final val dm: LengthMeasure = Deci * m
  final val cm: LengthMeasure = Centi * m
  final val mm: LengthMeasure = Milli * m
  final val nm: LengthMeasure = Nano * m

  final val in = LengthMeasure("in", Imperial())
  final val ft: LengthMeasure = "ft" := 12 * in
  final val yd: LengthMeasure = "yd" := 3 * ft
  final val rd: LengthMeasure = "rod" := 16.5 * ft
  final val fur: LengthMeasure = "fur" := 40 * rd
  final val mi: LengthMeasure = "mi" := 132 * fur

  final val nmi: LengthMeasure = "nmi" := 1852 * m

  // http://en.wikipedia.org/wiki/Thou_(length)
  final val thou: LengthMeasure = "thou" := 0.001 * in

  //http://en.wikipedia.org/wiki/Astronomical_unit
  final val au: LengthMeasure = "au" := 149597870700.0 * m

  // http://en.wikipedia.org/wiki/Light-year
  final val ly: LengthMeasure = "ly" := 9460730472580800.0 * m

  // http://en.wikipedia.org/wiki/Parsec
  final val parsec: LengthMeasure = au.composes("pc", 648000.0 / scala.math.Pi)
  final val pc: LengthMeasure = parsec

  // http://en.wikipedia.org/wiki/List_of_unusual_units_of_measurement#Siriometer
  final val siriometer: LengthMeasure = au.composes("Siriometer", 1E6)

  // http://en.wikipedia.org/wiki/List_of_humorous_units_of_measurement#Beard-second
  final val beardSecond: LengthMeasure = nm.composes("Beard-second", 5)

  /**
    * Area.
    */
  final val m2: ExponentialMeasure[LengthMeasure] = m ^ 2
  final val km2: ExponentialMeasure[LengthMeasure] = km ^ 2

  // Hectometer
  final val hm2: ExponentialMeasure[LengthMeasure] = m ^ 2
  final val ha: ExponentialMeasure[LengthMeasure] = m2.composes("Hectare", 10000)

  final val ft2: ExponentialMeasure[LengthMeasure] = ft ^ 2
  final val acre: ExponentialMeasure[LengthMeasure] = ft2.composes("Acre", 43560)

  /**
    * Volume.
    */
  final val m3: ExponentialMeasure[LengthMeasure] = m ^ 3
  final val cm3: ExponentialMeasure[LengthMeasure] = cm ^ 3
  final val liter: ExponentialMeasure[LengthMeasure] = "liter" := 0.001 * m3

  final val in3: ExponentialMeasure[LengthMeasure] = in ^ 3

  // Liquid
  final val pi_liquid: ExponentialMeasure[LengthMeasure] = in3.composes("pint", 28.875)
  final val qt_liquid: ExponentialMeasure[LengthMeasure] = pi_liquid.composes("quart", 2.0)
  final val gal: ExponentialMeasure[LengthMeasure] = qt_liquid.composes("gal", US, 4.0)

  final val bbl = VolumeMeasure("bbl", Imperial())

  // Dry
  final val pi_dry: ExponentialMeasure[LengthMeasure] = in3.composes("pint", US, 33.6003125)
  final val qt_dry: ExponentialMeasure[LengthMeasure] = pi_dry.composes("quart", 2)
  final val peck: ExponentialMeasure[LengthMeasure] = qt_dry.composes("peck", 8)
  final val bushel: ExponentialMeasure[LengthMeasure] = peck.composes("bushel", 4)

  /**
    * Force.
    */
  final val N = ForceMeasure("N", SI)

  final val kip: MassMeasure = Kilo * lb // http://en.wikipedia.org/wiki/Kip_(unit)

  /**
    * Power.
    */
  final val W = PowerMeasure("W", SI)
  final val kW: PowerMeasure = Kilo * W
  final val MW: PowerMeasure = Mega * W
  final val GW: PowerMeasure = Giga * W
  final val TW: PowerMeasure = Tera * W
  final val PW: PowerMeasure = Peta * W

  /**
    * Energy.
    */
  val J = EnergyMeasure("J", Derived(SI))
  final val kJ: EnergyMeasure = Kilo * J
  final val MJ: EnergyMeasure = Mega * J
  final val GJ: EnergyMeasure = Giga * J
  final val TJ: EnergyMeasure = Tera * J
  final val PJ: EnergyMeasure = Peta * J

  final val MMBtu = EnergyMeasure("MMBtu", Imperial())

  /**
    * Pressure.
    */
  final val Pa = PressureMeasure("Pa", Derived(SI))
  final val kPa: PressureMeasure = Kilo * Pa
  final val MPa: PressureMeasure = Mega * Pa
  final val GPa: PressureMeasure = Giga * Pa
  final val TPa: PressureMeasure = Tera * Pa
  final val PPa: PressureMeasure = Peta * Pa

  /**
    * Electric current.
    */
  final val A = ElectricCurrentMeasure("A", SI)

  /**
    * Luminous intensity.
    */
  final val cd = LuminousIntensityMeasure("cd", SI)

  /**
    * Information.
    */
  // Bit
  final val b = InformationMeasure("b", SI)

  // Byte
  final val B: InformationMeasure = "B" := 8 * b

  /**
    * Luminous flux.
    */
  // Lumen
  final val lm = LuminousFluxMeasure("lm", Derived(SI))

  /**
    * Frequency.
    */
  val Hz = TemporalFrequencyMeasure("Hz", Derived(SI))

  /**
    * Currency.
    */
  // United Arab Emirates dirham
  final val AED = Currency("AED")
  // Afghan afghani
  final val AFN = Currency("AFN")
  // Albanian lek
  final val ALL = Currency("ALL")
  // Armenian dram
  final val AMD = Currency("AMD")
  // Netherlands Antillean guilder
  final val ANG = Currency("ANG")
  // Angolan kwanza
  final val AOA = Currency("AOA")
  // Argentine peso
  final val ARS = Currency("ARS")
  // Australian dollar
  final val AUD = Currency("AUD")
  // Aruban florin
  final val AWG = Currency("AWG")
  // Azerbaijani manat
  final val AZN = Currency("AZN")
  // Bosnia and Herzegovina convertible mark
  final val BAM = Currency("BAM")
  // Barbados dollar
  final val BBD = Currency("BBD")
  // Bangladeshi taka
  final val BDT = Currency("BDT")
  // Bulgarian lev
  final val BGN = Currency("BGN")
  // Bahraini dinar
  final val BHD = Currency("BHD")
  // Burundian franc
  final val BIF = Currency("BIF")
  // Bermudian dollar
  final val BMD = Currency("BMD")
  // Brunei dollar
  final val BND = Currency("BND")
  // Boliviano
  final val BOB = Currency("BOB")
  // Bolivian Mvdol (funds code)
  final val BOV = Currency("BOV")
  // Brazilian real
  final val BRL = Currency("BRL")
  // Bahamian dollar
  final val BSD = Currency("BSD")
  // Bhutanese ngultrum
  final val BTN = Currency("BTN")
  // Botswana pula
  final val BWP = Currency("BWP")
  // Belarusian ruble
  final val BYR = Currency("BYR")
  // Belize dollar
  final val BZD = Currency("BZD")
  // Canadian dollar
  final val CAD = Currency("CAD")
  // Congolese franc
  final val CDF = Currency("CDF")
  // Swiss franc
  final val CHF = Currency("CHF")
  // Unidad de Fomento (funds code)
  final val CLF = Currency("CLF")
  // Chilean peso
  final val CLP = Currency("CLP")
  // Chinese yuan
  final val CNY = Currency("CNY")
  // Colombian peso
  final val COP = Currency("COP")
  // Costa Rican colon
  final val CRC = Currency("CRC")
  // Cuban convertible peso
  final val CUC = Currency("CUC")
  // Cuban peso
  final val CUP = Currency("CUP")
  // Cape Verde escudo
  final val CVE = Currency("CVE")
  // Czech koruna
  final val CZK = Currency("CZK")
  // Djiboutian franc
  final val DJF = Currency("DJF")
  // Danish krone
  final val DKK = Currency("DKK")
  // Dominican peso
  final val DOP = Currency("DOP")
  // Algerian dinar
  final val DZD = Currency("DZD")
  // Egyptian pound
  final val EGP = Currency("EGP")
  // Eritrean nakfa
  final val ERN = Currency("ERN")
  // Ethiopian birr
  final val ETB = Currency("ETB")
  // Euro
  final val EUR = Currency("EUR")
  // Fiji dollar
  final val FJD = Currency("FJD")
  // Falkland Islands pound
  final val FKP = Currency("FKP")
  // Pound sterling
  final val GBP = Currency("GBP")
  // Georgian lari
  final val GEL = Currency("GEL")
  // Ghanaian cedi
  final val GHS = Currency("GHS")
  // Gibraltar pound
  final val GIP = Currency("GIP")
  // Gambian dalasi
  final val GMD = Currency("GMD")
  // Guinean franc
  final val GNF = Currency("GNF")
  // Guatemalan quetzal
  final val GTQ = Currency("GTQ")
  // Guyanese dollar
  final val GYD = Currency("GYD")
  // Hong Kong dollar
  final val HKD = Currency("HKD")
  // Honduran lempira
  final val HNL = Currency("HNL")
  // Croatian kuna
  final val HRK = Currency("HRK")
  // Haitian gourde
  final val HTG = Currency("HTG")
  // Hungarian forint
  final val HUF = Currency("HUF")
  // Indonesian rupiah
  final val IDR = Currency("IDR")
  // Israeli new shekel
  final val ILS = Currency("ILS")
  // Indian rupee
  final val INR = Currency("INR")
  // Iraqi dinar
  final val IQD = Currency("IQD")
  // Iranian rial
  final val IRR = Currency("IRR")
  // Icelandic króna
  final val ISK = Currency("ISK")
  // Jamaican dollar
  final val JMD = Currency("JMD")
  // Jordanian dinar
  final val JOD = Currency("JOD")
  // Japanese yen
  final val JPY = Currency("JPY")
  // Kenyan shilling
  final val KES = Currency("KES")
  // Kyrgyzstani som
  final val KGS = Currency("KGS")
  // Cambodian riel
  final val KHR = Currency("KHR")
  // Comoro franc
  final val KMF = Currency("KMF")
  // North Korean won
  final val KPW = Currency("KPW")
  // South Korean won
  final val KRW = Currency("KRW")
  // Kuwaiti dinar
  final val KWD = Currency("KWD")
  // Cayman Islands dollar
  final val KYD = Currency("KYD")
  // Kazakhstani tenge
  final val KZT = Currency("KZT")
  // Lao kip
  final val LAK = Currency("LAK")
  // Lebanese pound
  final val LBP = Currency("LBP")
  // Sri Lankan rupee
  final val LKR = Currency("LKR")
  // Liberian dollar
  final val LRD = Currency("LRD")
  // Lesotho loti
  final val LSL = Currency("LSL")
  // Lithuanian litas
  final val LTL = Currency("LTL")
  // Latvian lats
  final val LVL = Currency("LVL")
  // Libyan dinar
  final val LYD = Currency("LYD")
  // Moroccan dirham
  final val MAD = Currency("MAD")
  // Moldovan leu
  final val MDL = Currency("MDL")
  // Malagasy ariary
  final val MGA = Currency("MGA")
  // Macedonian denar
  final val MKD = Currency("MKD")
  // Myanma kyat
  final val MMK = Currency("MMK")
  // Mongolian tugrik
  final val MNT = Currency("MNT")
  // Macanese pataca
  final val MOP = Currency("MOP")
  // Mauritanian ouguiya
  final val MRO = Currency("MRO")
  // Mauritian rupee
  final val MUR = Currency("MUR")
  // Maldivian rufiyaa
  final val MVR = Currency("MVR")
  // Malawian kwacha
  final val MWK = Currency("MWK")
  // Mexican peso
  final val MXN = Currency("MXN")
  // Mexican Unidad de Inversion (UDI) (funds code)
  final val MXV = Currency("MXV")
  // Malaysian ringgit
  final val MYR = Currency("MYR")
  // Mozambican metical
  final val MZN = Currency("MZN")
  // Namibian dollar
  final val NAD = Currency("NAD")
  // Nigerian naira
  final val NGN = Currency("NGN")
  // Nicaraguan córdoba
  final val NIO = Currency("NIO")
  // Norwegian krone
  final val NOK = Currency("NOK")
  // Nepalese rupee
  final val NPR = Currency("NPR")
  // New Zealand dollar
  final val NZD = Currency("NZD")
  // Omani rial
  final val OMR = Currency("OMR")
  // Panamanian balboa
  final val PAB = Currency("PAB")
  // Peruvian nuevo sol
  final val PEN = Currency("PEN")
  // Papua New Guinean kina
  final val PGK = Currency("PGK")
  // Philippine peso
  final val PHP = Currency("PHP")
  // Pakistani rupee
  final val PKR = Currency("PKR")
  // Polish złoty
  final val PLN = Currency("PLN")
  // Paraguayan guaraní
  final val PYG = Currency("PYG")
  // Qatari riyal
  final val QAR = Currency("QAR")
  // Romanian new leu
  final val RON = Currency("RON")
  // Serbian dinar
  final val RSD = Currency("RSD")
  // Russian rouble
  final val RUB = Currency("RUB")
  // Rwandan franc
  final val RWF = Currency("RWF")
  // Saudi riyal
  final val SAR = Currency("SAR")
  // Solomon Islands dollar
  final val SBD = Currency("SBD")
  // Seychelles rupee
  final val SCR = Currency("SCR")
  // Sudanese pound
  final val SDG = Currency("SDG")
  // Swedish krona/kronor
  final val SEK = Currency("SEK")
  // Singapore dollar
  final val SGD = Currency("SGD")
  // Saint Helena pound
  final val SHP = Currency("SHP")
  // Sierra Leonean leone
  final val SLL = Currency("SLL")
  // Somali shilling
  final val SOS = Currency("SOS")
  // Surinamese dollar
  final val SRD = Currency("SRD")
  // São Tomé and Príncipe dobra
  final val STD = Currency("STD")
  // Syrian pound
  final val SYP = Currency("SYP")
  // Swazi lilangeni
  final val SZL = Currency("SZL")
  // Thai baht
  final val THB = Currency("THB")
  // Tajikistani somoni
  final val TJS = Currency("TJS")
  // Turkmenistani manat
  final val TMT = Currency("TMT")
  // Tunisian dinar
  final val TND = Currency("TND")
  // Tongan paʻanga
  final val TOP = Currency("TOP")
  // Turkish lira
  final val TRY = Currency("TRY")
  // Trinidad and Tobago dollar
  final val TTD = Currency("TTD")
  // New Taiwan dollar
  final val TWD = Currency("TWD")
  // Tanzanian shilling
  final val TZS = Currency("TZS")
  // Ukrainian hryvnia
  final val UAH = Currency("UAH")
  // Ugandan shilling
  final val UGX = Currency("UGX")
  // United States dollar
  final val USD = Currency("USD")
  // United States dollar (next day) (funds code)
  final val USN = Currency("USN")
  // United States dollar (same day) (funds code)
  final val USS = Currency("USS")
  // Uruguayan peso
  final val UYU = Currency("UYU")
  // Uzbekistan som
  final val UZS = Currency("UZS")
  // Venezuelan bolívar
  final val VEF = Currency("VEF")
  // Vietnamese dong
  final val VND = Currency("VND")
  // Vanuatu vatu
  final val VUV = Currency("VUV")
  // Samoan tala
  final val WST = Currency("WST")
  // CFA franc BEAC
  final val XAF = Currency("XAF")
  // Silver (one troy ounce)
  final val XAG = Currency("XAG")
  // Gold (one troy ounce)
  final val XAU = Currency("XAU")
  // European Composite Unit (EURCO) (bond market unit)
  final val XBA = Currency("XBA")
  // European Monetary Unit (E.M.U.-6) (bond market unit)
  final val XBB = Currency("XBB")
  // European Unit of Account 9 (E.U.A.-9) (bond market unit)
  final val XBC = Currency("XBC")
  // European Unit of Account 17 (E.U.A.-17) (bond market unit)
  final val XBD = Currency("XBD")
  // East Caribbean dollar
  final val XCD = Currency("XCD")
  // Special drawing rights
  final val XDR = Currency("XDR")
  // UIC franc (special settlement currency)
  final val XFU = Currency("XFU")
  // CFA franc BCEAO
  final val XOF = Currency("XOF")
  // Palladium (one troy ounce)
  final val XPD = Currency("XPD")
  // CFP franc (Franc du Pacifique)
  final val XPF = Currency("XPF")
  // Platinum (one troy ounce)
  final val XPT = Currency("XPT")
  // Code reserved for testing purposes
  final val XTS = Currency("XTS")
  // No currency
  final val XXX = Currency("XXX")
  // Yemeni rial
  final val YER = Currency("YER")
  // South African rand
  final val ZAR = Currency("ZAR")
  // Zambian kwacha
  final val ZMW = Currency("ZMW")

  // United States cent
  final val USC: Currency = "USC" := 0.01 * USD
}
