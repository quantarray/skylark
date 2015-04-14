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

  case object NoDimension extends Dimension

  case object Time extends Dimension

  case object Length extends Dimension

  case object Mass extends Dimension

  case object Temperature extends Dimension

  case object Amount extends Dimension

  case object ElectricCurrent extends Dimension

  case object LuminousIntensity extends Dimension

  // E.g. digital information, such as bit
  case object Information extends Dimension

  // E.g. Currency, such as USD
  case object Money extends Dimension

  val Force = (Mass * Length) / (Time ^ 2)

  val Energy = (Mass * (Length ^ 2)) / (Time ^ 2)

  val Power = (Mass * (Length ^ 2)) / (Time ^ 3)

  val Pressure = Mass / (Length * (Time ^ 2))

  val LuminousFlux = LuminousIntensity * NoDimension

  val Voltage = (Mass * (Length ^ 2)) / ((Time ^ 3) * ElectricCurrent)

  val TemporalFrequency = NoDimension / Time

  val SpatialFrequency = NoDimension / Length

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

  /**
   * Unitless.
   */
  val percent = UnitlessMeasure("%", (0.01, UnitMeasure))

  // http://en.wikipedia.org/wiki/Basis_point
  val bp = UnitlessMeasure("bp", (0.0001, UnitMeasure))

  // http://en.wikipedia.org/wiki/Radian
  val rad = UnitlessMeasure("rad", Derived(SI), (1 / (2 * scala.math.Pi), UnitMeasure))

  // http://en.wikipedia.org/wiki/Steradian
  val sr = UnitlessMeasure("sr", Derived(SI), (1 / (4 * scala.math.Pi), UnitMeasure))

  /**
   * Time.
   */
  val s = TimeMeasure("s", SI)
  val min = TimeMeasure("min", (60.0, s))
  val h = TimeMeasure("h", (60.0, min))
  val day = TimeMeasure("day", (24.0, h))
  val year365 = TimeMeasure("Year[365]", (365.0, day))
  val year360 = TimeMeasure("Year[360]", (360.0, day))

  val ms = Milli * s
  val ns = Nano * s

  val fortnight = TimeMeasure("Fortnight", Imperial(), (14.0, day))

  /**
   * Mass.
   */
  val g = MassMeasure("g", SI)
  val kg = Kilo * g
  val cg = Centi * g
  val mg = Milli * g
  val t = MassMeasure("Tonnne", (1000.0, kg))
  val oz_metric = MassMeasure("Metric Ounce", (25.0, g)) // http://en.wikipedia.org/wiki/Ounce#Metric_ounces

  val oz = MassMeasure("Ounce", US)
  val lb = MassMeasure("Pound", (16.0, oz))
  // http://en.wikipedia.org/wiki/Short_ton
  val ton = MassMeasure("Ton", (2000.0, lb))

  // http://en.wikipedia.org/wiki/Grain_(unit)
  val gr = MassMeasure("Grain", Imperial(), (64.79891, mg))
  // http://en.wikipedia.org/wiki/Pennyweight
  val dwt = MassMeasure("Pennyweight", (24.0, gr))
  val oz_troy = MassMeasure("Troy Ounce", (20.0, dwt))
  val lb_troy = MassMeasure("Troy Pound", (12.0, oz_troy))

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
  val ft = LengthMeasure("Foot", (12.0, in))
  val yd = LengthMeasure("Yard", (3.0, ft))
  val rd = LengthMeasure("Rod", (16.5, ft))
  val fur = LengthMeasure("Furlong", (40.0, rd))
  val mi = LengthMeasure("Mile", (132.0, fur))

  val nmi = LengthMeasure("Nautical mile", (1852.0, m))

  // http://en.wikipedia.org/wiki/Thou_(length)
  val thou = LengthMeasure("Thou", (0.001, in))

  //http://en.wikipedia.org/wiki/Astronomical_unit
  val astronomicalUnit = LengthMeasure("au", (149597870700.0, m))
  val au = astronomicalUnit

  // http://en.wikipedia.org/wiki/Light-year
  val lightYear = LengthMeasure("ly", (9460730472580800.0, m))
  val ly = lightYear

  // http://en.wikipedia.org/wiki/Parsec
  val parsec = LengthMeasure("pc", (648000.0 / scala.math.Pi, au))
  val pc = parsec

  // http://en.wikipedia.org/wiki/List_of_unusual_units_of_measurement#Siriometer
  val siriometer = LengthMeasure("Siriometer", (1E6, au))

  // http://en.wikipedia.org/wiki/List_of_humorous_units_of_measurement#Beard-second
  val beardSecond = LengthMeasure("Beard-second", (5.0, nm))

  /**
   * Area.
   */
  val m2 = m ^ 2
  val km2 = km ^ 2
  // Hectometer
  val hm2 = m ^ 2
  val ha = AreaMeasure("Hectare", (10000.0, m2))

  val ft2 = ft ^ 2
  val acre = AreaMeasure("Acre", (43560.0, ft2))

  /**
   * Volume.
   */
  val m3 = m ^ 3
  val cm3 = cm ^ 3
  val liter = VolumeMeasure("Liter", (0.001, m3))

  val in3 = in ^ 3

  // Liquid
  val pi_liquid = VolumeMeasure("Pint", (28.875, in3))
  val qt_liquid = VolumeMeasure("Quart", (2.0, pi_liquid))
  val gal = VolumeMeasure("gal", US, (4.0, qt_liquid))

  val bbl = VolumeMeasure("bbl", Imperial())

  // Dry
  val pi_dry = VolumeMeasure("Pint", US, (33.6003125, in3))
  val qt_dry = VolumeMeasure("Quart", (2.0, pi_dry))
  val peck = VolumeMeasure("Peck", (8.0, qt_dry))
  val bushel = VolumeMeasure("Bushel", (4.0, peck))

  /**
   * Force.
   */
  val N = ForceMeasure("Newton", SI)

  val kip = Kilo * lb // http://en.wikipedia.org/wiki/Kip_(unit)

  /**
   * Power.
   */
  val W = PowerMeasure("Watt", SI)
  val MW = Mega * W
  val GW = Giga * W

  /**
   * Energy.
   */
  val J = EnergyMeasure("Joule", Derived(SI))

  val MWh = MW * h
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
  val B = InformationMeasure("Byte", (8.0, b))

  /**
   * Luminous flux.
   */
  val lm = LuminousFluxMeasure("Lumen", Derived(SI), None)

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

  trait Units extends Any
  {
    def toDouble: Double

    def *[M <: Measure](measure: M) = Quantity(toDouble, measure)

    /**
     * Unitless.
     */
    def percent = Quantity(toDouble, measure.percent)

    def bp = Quantity(toDouble, measure.bp)

    def rad = Quantity(toDouble, measure.rad)

    def sr = Quantity(toDouble, measure.sr)

    /**
     * Mass.
     */
    def g = Quantity(toDouble, measure.g)

    def kg = Quantity(toDouble, measure.kg)

    def cg = Quantity(toDouble, measure.cg)

    def mg = Quantity(toDouble, measure.mg)

    def t = Quantity(toDouble, measure.t)

    def oz_metric = Quantity(toDouble, measure.oz_metric)

    def oz = Quantity(toDouble, measure.oz)

    def lb = Quantity(toDouble, measure.lb)

    def ton = Quantity(toDouble, measure.ton)

    def gr = Quantity(toDouble, measure.gr)

    def dwt = Quantity(toDouble, measure.dwt)

    def lb_troy = Quantity(toDouble, measure.lb_troy)

    def oz_troy = Quantity(toDouble, measure.oz_troy)

    /**
     * Length.
     */
    def m = Quantity(toDouble, measure.m)

    /**
     * Area.
     */
    def m2 = Quantity(toDouble, measure.m2)

    def km2 = Quantity(toDouble, measure.km2)

    def hm2 = Quantity(toDouble, measure.hm2)

    def ha = Quantity(toDouble, measure.ha)

    def ft2 = Quantity(toDouble, measure.ft2)

    def acre = Quantity(toDouble, measure.acre)

    /**
     * Volume.
     */
    def m3 = Quantity(toDouble, measure.m3)

    def cm3 = Quantity(toDouble, measure.cm3)

    def liter = Quantity(toDouble, measure.liter)

    def in3 = Quantity(toDouble, measure.in3)

    // Liquid
    def pi_liquid = Quantity(toDouble, measure.pi_liquid)

    def qt_liquid = Quantity(toDouble, measure.qt_liquid)

    def gal = Quantity(toDouble, measure.gal)

    def bbl = Quantity(toDouble, measure.bbl)

    // Dry
    def pi_dry = Quantity(toDouble, measure.pi_dry)

    def qt_dry = Quantity(toDouble, measure.qt_dry)

    def peck = Quantity(toDouble, measure.peck)

    def bushel = Quantity(toDouble, measure.bushel)

    /**
     * Currency.
     */
    def AED = Quantity(toDouble, measure.AED)

    def AFN = Quantity(toDouble, measure.AFN)

    def ALL = Quantity(toDouble, measure.ALL)

    def AMD = Quantity(toDouble, measure.AMD)

    def ANG = Quantity(toDouble, measure.ANG)

    def AOA = Quantity(toDouble, measure.AOA)

    def ARS = Quantity(toDouble, measure.ARS)

    def AUD = Quantity(toDouble, measure.AUD)

    def AWG = Quantity(toDouble, measure.AWG)

    def AZN = Quantity(toDouble, measure.AZN)

    def BAM = Quantity(toDouble, measure.BAM)

    def BBD = Quantity(toDouble, measure.BBD)

    def BDT = Quantity(toDouble, measure.BDT)

    def BGN = Quantity(toDouble, measure.BGN)

    def BHD = Quantity(toDouble, measure.BHD)

    def BIF = Quantity(toDouble, measure.BIF)

    def BMD = Quantity(toDouble, measure.BMD)

    def BND = Quantity(toDouble, measure.BND)

    def BOB = Quantity(toDouble, measure.BOB)

    def BOV = Quantity(toDouble, measure.BOV)

    def BRL = Quantity(toDouble, measure.BRL)

    def BSD = Quantity(toDouble, measure.BSD)

    def BTN = Quantity(toDouble, measure.BTN)

    def BWP = Quantity(toDouble, measure.BWP)

    def BYR = Quantity(toDouble, measure.BYR)

    def BZD = Quantity(toDouble, measure.BZD)

    def CAD = Quantity(toDouble, measure.CAD)

    def CDF = Quantity(toDouble, measure.CDF)

    def CHF = Quantity(toDouble, measure.CHF)

    def CLF = Quantity(toDouble, measure.CLF)

    def CLP = Quantity(toDouble, measure.CLP)

    def CNY = Quantity(toDouble, measure.CNY)

    def COP = Quantity(toDouble, measure.COP)

    def CRC = Quantity(toDouble, measure.CRC)

    def CUC = Quantity(toDouble, measure.CUC)

    def CUP = Quantity(toDouble, measure.CUP)

    def CVE = Quantity(toDouble, measure.CVE)

    def CZK = Quantity(toDouble, measure.CZK)

    def DJF = Quantity(toDouble, measure.DJF)

    def DKK = Quantity(toDouble, measure.DKK)

    def DOP = Quantity(toDouble, measure.DOP)

    def DZD = Quantity(toDouble, measure.DZD)

    def EGP = Quantity(toDouble, measure.EGP)

    def ERN = Quantity(toDouble, measure.ERN)

    def ETB = Quantity(toDouble, measure.ETB)

    def EUR = Quantity(toDouble, measure.EUR)

    def FJD = Quantity(toDouble, measure.FJD)

    def FKP = Quantity(toDouble, measure.FKP)

    def GBP = Quantity(toDouble, measure.GBP)

    def GEL = Quantity(toDouble, measure.GEL)

    def GHS = Quantity(toDouble, measure.GHS)

    def GIP = Quantity(toDouble, measure.GIP)

    def GMD = Quantity(toDouble, measure.GMD)

    def GNF = Quantity(toDouble, measure.GNF)

    def GTQ = Quantity(toDouble, measure.GTQ)

    def GYD = Quantity(toDouble, measure.GYD)

    def HKD = Quantity(toDouble, measure.HKD)

    def HNL = Quantity(toDouble, measure.HNL)

    def HRK = Quantity(toDouble, measure.HRK)

    def HTG = Quantity(toDouble, measure.HTG)

    def HUF = Quantity(toDouble, measure.HUF)

    def IDR = Quantity(toDouble, measure.IDR)

    def ILS = Quantity(toDouble, measure.ILS)

    def INR = Quantity(toDouble, measure.INR)

    def IQD = Quantity(toDouble, measure.IQD)

    def IRR = Quantity(toDouble, measure.IRR)

    def ISK = Quantity(toDouble, measure.ISK)

    def JMD = Quantity(toDouble, measure.JMD)

    def JOD = Quantity(toDouble, measure.JOD)

    def JPY = Quantity(toDouble, measure.JPY)

    def KES = Quantity(toDouble, measure.KES)

    def KGS = Quantity(toDouble, measure.KGS)

    def KHR = Quantity(toDouble, measure.KHR)

    def KMF = Quantity(toDouble, measure.KMF)

    def KPW = Quantity(toDouble, measure.KPW)

    def KRW = Quantity(toDouble, measure.KRW)

    def KWD = Quantity(toDouble, measure.KWD)

    def KYD = Quantity(toDouble, measure.KYD)

    def KZT = Quantity(toDouble, measure.KZT)

    def LAK = Quantity(toDouble, measure.LAK)

    def LBP = Quantity(toDouble, measure.LBP)

    def LKR = Quantity(toDouble, measure.LKR)

    def LRD = Quantity(toDouble, measure.LRD)

    def LSL = Quantity(toDouble, measure.LSL)

    def LTL = Quantity(toDouble, measure.LTL)

    def LVL = Quantity(toDouble, measure.LVL)

    def LYD = Quantity(toDouble, measure.LYD)

    def MAD = Quantity(toDouble, measure.MAD)

    def MDL = Quantity(toDouble, measure.MDL)

    def MGA = Quantity(toDouble, measure.MGA)

    def MKD = Quantity(toDouble, measure.MKD)

    def MMK = Quantity(toDouble, measure.MMK)

    def MNT = Quantity(toDouble, measure.MNT)

    def MOP = Quantity(toDouble, measure.MOP)

    def MRO = Quantity(toDouble, measure.MRO)

    def MUR = Quantity(toDouble, measure.MUR)

    def MVR = Quantity(toDouble, measure.MVR)

    def MWK = Quantity(toDouble, measure.MWK)

    def MXN = Quantity(toDouble, measure.MXN)

    def MXV = Quantity(toDouble, measure.MXV)

    def MYR = Quantity(toDouble, measure.MYR)

    def MZN = Quantity(toDouble, measure.MZN)

    def NAD = Quantity(toDouble, measure.NAD)

    def NGN = Quantity(toDouble, measure.NGN)

    def NIO = Quantity(toDouble, measure.NIO)

    def NOK = Quantity(toDouble, measure.NOK)

    def NPR = Quantity(toDouble, measure.NPR)

    def NZD = Quantity(toDouble, measure.NZD)

    def OMR = Quantity(toDouble, measure.OMR)

    def PAB = Quantity(toDouble, measure.PAB)

    def PEN = Quantity(toDouble, measure.PEN)

    def PGK = Quantity(toDouble, measure.PGK)

    def PHP = Quantity(toDouble, measure.PHP)

    def PKR = Quantity(toDouble, measure.PKR)

    def PLN = Quantity(toDouble, measure.PLN)

    def PYG = Quantity(toDouble, measure.PYG)

    def QAR = Quantity(toDouble, measure.QAR)

    def RON = Quantity(toDouble, measure.RON)

    def RSD = Quantity(toDouble, measure.RSD)

    def RUB = Quantity(toDouble, measure.RUB)

    def RWF = Quantity(toDouble, measure.RWF)

    def SAR = Quantity(toDouble, measure.SAR)

    def SBD = Quantity(toDouble, measure.SBD)

    def SCR = Quantity(toDouble, measure.SCR)

    def SDG = Quantity(toDouble, measure.SDG)

    def SEK = Quantity(toDouble, measure.SEK)

    def SGD = Quantity(toDouble, measure.SGD)

    def SHP = Quantity(toDouble, measure.SHP)

    def SLL = Quantity(toDouble, measure.SLL)

    def SOS = Quantity(toDouble, measure.SOS)

    def SRD = Quantity(toDouble, measure.SRD)

    def STD = Quantity(toDouble, measure.STD)

    def SYP = Quantity(toDouble, measure.SYP)

    def SZL = Quantity(toDouble, measure.SZL)

    def THB = Quantity(toDouble, measure.THB)

    def TJS = Quantity(toDouble, measure.TJS)

    def TMT = Quantity(toDouble, measure.TMT)

    def TND = Quantity(toDouble, measure.TND)

    def TOP = Quantity(toDouble, measure.TOP)

    def TRY = Quantity(toDouble, measure.TRY)

    def TTD = Quantity(toDouble, measure.TTD)

    def TWD = Quantity(toDouble, measure.TWD)

    def TZS = Quantity(toDouble, measure.TZS)

    def UAH = Quantity(toDouble, measure.UAH)

    def UGX = Quantity(toDouble, measure.UGX)

    def USD = Quantity(toDouble, measure.USD)

    def USN = Quantity(toDouble, measure.USN)

    def USS = Quantity(toDouble, measure.USS)

    def UYU = Quantity(toDouble, measure.UYU)

    def UZS = Quantity(toDouble, measure.UZS)

    def VEF = Quantity(toDouble, measure.VEF)

    def VND = Quantity(toDouble, measure.VND)

    def VUV = Quantity(toDouble, measure.VUV)

    def WST = Quantity(toDouble, measure.WST)

    def XAF = Quantity(toDouble, measure.XAF)

    def XAG = Quantity(toDouble, measure.XAG)

    def XAU = Quantity(toDouble, measure.XAU)

    def XBA = Quantity(toDouble, measure.XBA)

    def XBB = Quantity(toDouble, measure.XBB)

    def XBC = Quantity(toDouble, measure.XBC)

    def XBD = Quantity(toDouble, measure.XBD)

    def XCD = Quantity(toDouble, measure.XCD)

    def XDR = Quantity(toDouble, measure.XDR)

    def XFU = Quantity(toDouble, measure.XFU)

    def XOF = Quantity(toDouble, measure.XOF)

    def XPD = Quantity(toDouble, measure.XPD)

    def XPF = Quantity(toDouble, measure.XPF)

    def XPT = Quantity(toDouble, measure.XPT)

    def XTS = Quantity(toDouble, measure.XTS)

    def XXX = Quantity(toDouble, measure.XXX)

    def YER = Quantity(toDouble, measure.YER)

    def ZAR = Quantity(toDouble, measure.ZAR)

    def ZMW = Quantity(toDouble, measure.ZMW)
  }

  implicit final class IntQuantity(private val value: Int) extends AnyVal with Units
  {
    override def toDouble: Double = value
  }

  implicit final class LongQuantity(private val value: Long) extends AnyVal with Units
  {
    override def toDouble: Double = value
  }

  implicit final class DoubleQuantity(private val value: Double) extends AnyVal with Units
  {
    override def toDouble: Double = value
  }

}
