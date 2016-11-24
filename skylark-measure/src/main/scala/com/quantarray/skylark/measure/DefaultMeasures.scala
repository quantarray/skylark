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

  final val percent = "%" := 0.01 * Unit

  // http://en.wikipedia.org/wiki/Basis_point
  final val bp = "bp" := 0.0001 * Unit

  // http://en.wikipedia.org/wiki/Radian
  final val rad = DimensionlessMeasure("rad", Derived(SI), 1 / (2 * scala.math.Pi))

  // http://en.wikipedia.org/wiki/Steradian
  final val sr = DimensionlessMeasure("sr", Derived(SI), 1 / (4 * scala.math.Pi))

  /**
    * Time.
    */
  final val s = TimeMeasure("s", SI)
  final val sec = s
  final val secs = s
  final val min = "min" := 60 * s
  final val mins = min
  final val h = "h" := 60 * min
  final val (hour, hours) = (h, h)
  final val day = "day" := 24 * h
  final val days = day
  final val year365 = "Year[365]" := 365 * day
  final val years = year365
  final val year360 = "Year[360]" := 360 * day

  final val ms = Milli * s
  final val ns = Nano * s

  val fortnight = day.composes("Fortnight", Imperial(), 14)

  /**
    * Mass.
    */
  final val g = MassMeasure("g", SI)
  final val kg = Kilo * g
  final val cg = Centi * g
  final val mg = Milli * g
  final val t = MassMeasure("t", SI)
  final val oz_metric = MassMeasure("metric oz", SI) // http://en.wikipedia.org/wiki/Ounce#Metric_ounces

  final val oz = MassMeasure("oz", US)
  final val lb = MassMeasure("lb", US)

  final val mt = "mt" := 2204.625 * lb
  final val ton = mt

  // http://en.wikipedia.org/wiki/Grain_(unit)
  final val gr = MassMeasure("grain", Imperial())
  // http://en.wikipedia.org/wiki/Pennyweight
  final val dwt = MassMeasure("dwt", Imperial())
  final val oz_troy = MassMeasure("troy oz", Imperial())
  final val lb_troy = MassMeasure("troy lb", Imperial())

  /**
    * Length.
    */
  final val m = LengthMeasure("m", SI)
  final val km = Kilo * m
  final val hm = Hecto * m
  final val dam = Deka * m
  final val dm = Deci * m
  final val cm = Centi * m
  final val mm = Milli * m
  final val nm = Nano * m

  final val in = LengthMeasure("in", Imperial())
  final val ft = "ft" := 12 * in
  final val yd = "yd" := 3 * ft
  final val rd = "rod" := 16.5 * ft
  final val fur = "fur" := 40 * rd
  final val mi = "mi" := 132 * fur

  final val nmi = "nmi" := 1852 * m

  // http://en.wikipedia.org/wiki/Thou_(length)
  final val thou = "thou" := 0.001 * in

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
  final val m2 = m ^ 2
  final val km2 = km ^ 2
  // Hectometer
  final val hm2 = m ^ 2
  final val ha = m2.composes("Hectare", 10000)

  final val ft2 = ft ^ 2
  final val acre = ft2.composes("Acre", 43560)

  /**
    * Volume.
    */
  final val m3 = m ^ 3
  final val cm3 = cm ^ 3
  final val liter = "liter" := 0.001 * m3

  final val in3 = in ^ 3

  // Liquid
  final val pi_liquid = in3.composes("pint", 28.875)
  final val qt_liquid = pi_liquid.composes("quart", 2.0)
  final val gal = qt_liquid.composes("gal", US, 4.0)

  final val bbl = VolumeMeasure("bbl", Imperial())

  // Dry
  final val pi_dry = in3.composes("pint", US, 33.6003125)
  final val qt_dry = pi_dry.composes("quart", 2)
  final val peck = qt_dry.composes("peck", 8)
  final val bushel = peck.composes("bushel", 4)

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

  final val MMBtu = EnergyMeasure("MMBtu", Imperial())

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
  final val CAD = Currency("CAD")
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
  final val USD = Currency("USD")
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
  final val USC = "USC" := 0.01 * USD
}
