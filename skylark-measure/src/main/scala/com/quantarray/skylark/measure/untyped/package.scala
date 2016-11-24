package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object untyped
{
  val * = untyped.ProductMeasure
  val / = untyped.RatioMeasure
  val ^ = untyped.ExponentialMeasure

  type Conversion[From, To] = com.quantarray.skylark.measure.Conversion[From, To]

  val Conversion = com.quantarray.skylark.measure.Conversion

  type ⤇[From, To] = Conversion[From, To]

  val ⤇ = Conversion

  type ConvertException = com.quantarray.skylark.measure.ConvertException

  type CanAdd[A1, A2] = com.quantarray.skylark.measure.CanAdd[A1, A2]

  type CanConvert[From, To] = com.quantarray.skylark.measure.CanConvert[From, To]

  type Converter[From, To] = com.quantarray.skylark.measure.Converter[From, To]

  // TODO: Generate via macro

  /**
    * Dimensionless.
    */
  type DimensionlessMeasure = com.quantarray.skylark.measure.DimensionlessMeasure

  final val Unit: untyped.AnyMeasure = com.quantarray.skylark.measure.Unit

  val percent: untyped.AnyMeasure = com.quantarray.skylark.measure.percent

  val bp: untyped.AnyMeasure = com.quantarray.skylark.measure.bp

  /**
    * Time.
    */
  val s: untyped.AnyMeasure = com.quantarray.skylark.measure.s
  val min: untyped.AnyMeasure = com.quantarray.skylark.measure.min
  val mins: untyped.AnyMeasure = com.quantarray.skylark.measure.mins
  val h: untyped.AnyMeasure = com.quantarray.skylark.measure.h
  val hour: untyped.AnyMeasure = com.quantarray.skylark.measure.hour
  val day: untyped.AnyMeasure = com.quantarray.skylark.measure.day
  val days: untyped.AnyMeasure = com.quantarray.skylark.measure.days
  val year365: untyped.AnyMeasure = com.quantarray.skylark.measure.year365
  val years: untyped.AnyMeasure = com.quantarray.skylark.measure.years

  /**
    * Mass.
    */
  val g: untyped.AnyMeasure = com.quantarray.skylark.measure.g

  val kg: untyped.AnyMeasure = com.quantarray.skylark.measure.kg

  val mt: untyped.AnyMeasure = com.quantarray.skylark.measure.mt

  /**
    * Volume.
    */
  val bbl: untyped.AnyMeasure = com.quantarray.skylark.measure.bbl

  val gal: untyped.AnyMeasure = com.quantarray.skylark.measure.gal

  /**
    * Energy.
    */
  val J: untyped.AnyMeasure = com.quantarray.skylark.measure.J
  val kJ: untyped.AnyMeasure = com.quantarray.skylark.measure.kJ
  val MJ: untyped.AnyMeasure = com.quantarray.skylark.measure.MJ
  val GJ: untyped.AnyMeasure = com.quantarray.skylark.measure.GJ

  val MMBtu: untyped.AnyMeasure = com.quantarray.skylark.measure.MMBtu

  /**
    * Length.
    */
  val m: untyped.AnyMeasure = com.quantarray.skylark.measure.m

  /**
    * Currency.
    */
  val CAD: untyped.AnyMeasure = com.quantarray.skylark.measure.CAD

  val USD: untyped.AnyMeasure = com.quantarray.skylark.measure.USD

  val USC: untyped.AnyMeasure = com.quantarray.skylark.measure.USC

  object arithmetic
  {

    trait SafeArithmeticImplicits
    {
      implicit def exponentialCanExponentiate = new CanExponentiate[untyped.AnyMeasure, untyped.AnyMeasure]
      {
        override def pow(base: untyped.AnyMeasure, exponent: Double): untyped.AnyMeasure = ExponentialMeasure(base, exponent)
      }

      implicit def productCanMultiply = new CanMultiply[untyped.AnyMeasure, untyped.AnyMeasure, untyped.AnyMeasure]
      {
        override def times(multiplicand: untyped.AnyMeasure, multiplier: untyped.AnyMeasure): untyped.AnyMeasure = ProductMeasure(multiplicand, multiplier)
      }

      implicit def ratioCanDivide = new CanDivide[untyped.AnyMeasure, untyped.AnyMeasure, untyped.AnyMeasure]
      {
        override def divide(numerator: untyped.AnyMeasure, denominator: untyped.AnyMeasure): untyped.AnyMeasure = RatioMeasure(numerator, denominator)
      }

      implicit def lhsCanAddQuantity = new untyped.CanAddAnyQuantity[Double, untyped.AnyQuantity[Double], untyped.AnyQuantity[Double]]
      {
        type R = untyped.AnyMeasure

        type QR = Option[untyped.AnyQuantity[Double]]

        override def plus(addend1: untyped.AnyMeasure, addend2: untyped.AnyMeasure): untyped.AnyMeasure = addend1

        override def plus(addend1: untyped.AnyQuantity[Double], addend2: untyped.AnyQuantity[Double])(implicit cc: CanConvert[untyped.AnyMeasure, untyped.AnyMeasure]): QR =
        {
          val targetMeasure = plus(addend1.measure, addend2.measure)

          val a1 = cc.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
          val a2 = cc.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

          a1.flatMap(aa1 => a2.map(_ + aa1)).map(v => AnyQuantity(v, targetMeasure))
        }
      }
    }

    object safe extends SafeArithmeticImplicits

    object unsafe extends SafeArithmeticImplicits
    {
      implicit def lhsCanAddQuantityUnsafe = new untyped.CanAddAnyQuantity[Double, untyped.AnyQuantity[Double], untyped.AnyQuantity[Double]]
      {
        type R = untyped.AnyMeasure

        type QR = untyped.AnyQuantity[Double]

        override def plus(addend1: untyped.AnyMeasure, addend2: untyped.AnyMeasure): untyped.AnyMeasure = addend1

        override def plus(addend1: untyped.AnyQuantity[Double], addend2: untyped.AnyQuantity[Double])(implicit cc: CanConvert[untyped.AnyMeasure, untyped.AnyMeasure]): QR =
        {
          val targetMeasure = plus(addend1.measure, addend2.measure)

          val a1 = cc.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
          val a2 = cc.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

          (a1, a2) match
          {
            case (Some(aa1), Some(aa2)) => AnyQuantity(aa1 + aa2, targetMeasure)
            case (Some(_), _) => throw ConvertException(addend2.measure, targetMeasure)
            case (_, Some(_)) => throw ConvertException(addend1.measure, targetMeasure)
            case _ => throw ConvertException(s"Cannot convert to $targetMeasure.")
          }
        }
      }
    }

  }

  object conversion
  {

    trait DefaultMeasureConverter extends AnyMeasureConverter with com.quantarray.skylark.measure.conversion.DefaultConversionImplicits
    {
      override def convert(from: untyped.AnyMeasure, to: untyped.AnyMeasure): Option[Double] = ⤇(from, to) match
      {
        case (dm1: DimensionlessMeasure) ⤇ (dm2: DimensionlessMeasure) => dimensionlessCanConvert.convert(dm1, dm2)
        case (tm1: TimeMeasure) ⤇ (tm2: TimeMeasure) => timeCanConvert.convert(tm1, tm2)
        case (mm1: MassMeasure) ⤇ (mm2: MassMeasure) => massCanConvert.convert(mm1, mm2)
        case (lm1: LengthMeasure) ⤇ (lm2: LengthMeasure) => lengthCanConvert.convert(lm1, lm2)
        case (em1: EnergyMeasure) ⤇ (em2: EnergyMeasure) => energyCanConvert.convert(em1, em2)
        case (ccy1: Currency) ⤇ (ccy2: Currency) => currencyCanConvert.convert(ccy1, ccy2)
        case _ => super.convert(from, to)
      }
    }

    trait DefaultConversionImplicits
    {

      implicit val measureCanConvert = new CanConvert[untyped.AnyMeasure, untyped.AnyMeasure]
      {

        implicit val canConvert: CanConvert[untyped.AnyMeasure, untyped.AnyMeasure] = implicitly(this)

        override def convert: Converter[untyped.AnyMeasure, untyped.AnyMeasure] = new DefaultMeasureConverter
        {
          override def convert(from: untyped.AnyMeasure, to: untyped.AnyMeasure): Option[Double] = ⤇(from, to) match
          {
            case (diff1 * (same1 / diff2)) ⤇ same2 if same1 == same2 => diff1.to(diff2)
            case _ => super.convert(from, to)
          }
        }
      }

    }

    object default extends DefaultConversionImplicits

  }

  object simplification
  {

    import scala.collection.immutable.SortedMap

    case object DefaultReducer extends Reducer[untyped.AnyMeasure, untyped.AnyMeasure]
    {

      implicit val measureOrdering = Ordering.by
      {
        measure: untyped.AnyMeasure => measure.name
      }

      type ExponentMap = SortedMap[untyped.AnyMeasure, Double]

      val ExponentMap = SortedMap

      override def apply(from: untyped.AnyMeasure): untyped.AnyMeasure =
      {
        val es = exponentials(deflate(from), 1.0, ExponentMap.empty[untyped.AnyMeasure, Double])

        val productOfExponentials = es.groupBy(_._1).values.map(x => x.reduce((x, y) => (x._1, x._2 + y._2))).filter(_._2 != 0).toList

        val reduced = productOfExponentials.size match
        {
          case 0 => Unit
          case 1 => exponential(productOfExponentials.head)
          case 2 => untyped.ProductMeasure(exponential(productOfExponentials.head), exponential(productOfExponentials(1)))
          case _ =>
            val z = untyped.ProductMeasure(exponential(productOfExponentials.head), exponential(productOfExponentials(1)))
            productOfExponentials.takeRight(productOfExponentials.size - 2).foldLeft(z)((r, p) => ProductMeasure(exponential(p), r))
        }

        reduced
      }

      private def deflate(inflated: untyped.AnyMeasure): untyped.AnyMeasure =
      {
        def deflateProduct(inflated: untyped.ProductMeasure): untyped.AnyMeasure = deflateNonRecompose.orElse(giveUp)(inflated)

        def deflateNonRecompose: PartialFunction[untyped.AnyMeasure, untyped.AnyMeasure] =
        {
          case untyped.ProductMeasure(md, mr) if md == Unit && mr == Unit => Unit
          case untyped.ProductMeasure(md, mr) if md == Unit => deflate(mr)
          case untyped.ProductMeasure(md, mr) if mr == Unit => deflate(md)
          case untyped.ProductMeasure(md, mr@untyped.RatioMeasure(nr, dr)) if md == dr => deflate(nr)
          case untyped.ProductMeasure(md@untyped.RatioMeasure(nr, dr), mr) if dr == mr => deflate(nr)
          case untyped.RatioMeasure(nr, Unit) => deflate(nr)
          case untyped.ExponentialMeasure(Unit, _) => Unit
          case untyped.ExponentialMeasure(base, exponent) if exponent == 1.0 => deflate(base)
        }

        def deflateRecompose: PartialFunction[untyped.AnyMeasure, untyped.AnyMeasure] =
        {
          case untyped.ProductMeasure(md, mr) => deflateProduct(ProductMeasure(deflate(md), deflate(mr)))
          case untyped.ExponentialMeasure(base, exponent) => ExponentialMeasure(deflate(base), exponent)
        }

        def giveUp: PartialFunction[untyped.AnyMeasure, untyped.AnyMeasure] =
        {
          case measure => measure
        }

        deflateNonRecompose.orElse(deflateRecompose).orElse(giveUp)(inflated)
      }

      private def exponentials(measure: untyped.AnyMeasure, outerExponent: Double, map: ExponentMap): ExponentMap = measure match
      {
        case untyped.ProductMeasure(md, mr) => exponentials(md, outerExponent, exponentials(mr, outerExponent, map))
        case untyped.RatioMeasure(nr, dr) => exponentials(nr, outerExponent, exponentials(dr, -outerExponent, map))
        case untyped.ExponentialMeasure(base, exponent) => exponentials(base, exponent * outerExponent, map)
        case _ => map + (measure -> (map.getOrElse(measure, 0.0) + measure.exponent * outerExponent))
      }

      private def exponential(measure: (untyped.AnyMeasure, Double)): untyped.AnyMeasure = measure match
      {
        case (x, 1.0) => x
        case _ => untyped.ExponentialMeasure(measure._1, measure._2)
      }
    }

    trait DefaultSimplificationImplicits
    {

      case object ProductOfExponentials
      {
        def apply(from: untyped.AnyMeasure): untyped.ProductMeasure = product(from, 1)

        private def product(from: untyped.AnyMeasure, outerExponent: Double): untyped.ProductMeasure = from match
        {
          case untyped.ProductMeasure(md, mr) => ProductMeasure(product(md, outerExponent), product(mr, outerExponent))
          case untyped.RatioMeasure(nr, dr) => ProductMeasure(product(nr, outerExponent), product(dr, -outerExponent))
          case untyped.ExponentialMeasure(base, exponent) => ProductMeasure(product(base, exponent * outerExponent), Unit)
          case _ if outerExponent == 1.0 => ProductMeasure(from, Unit)
          case _ => ProductMeasure(ExponentialMeasure(from, outerExponent), Unit)
        }
      }

      implicit val defaultCanSimplify = new CanSimplify[untyped.AnyMeasure, untyped.AnyMeasure]
      {
        override def simplify(inflated: untyped.AnyMeasure): untyped.AnyMeasure =
        {
          val productOfExponentials = ProductOfExponentials(inflated)

          DefaultReducer(productOfExponentials)
        }
      }

    }

    object default extends DefaultSimplificationImplicits

  }

  trait Measures[@specialized(Double, Int) N] extends Any
  {
    implicit def qn: QuasiNumeric[N]

    def value: N

    /**
      * Composes a quantity of supplied measure.
      */
    def apply(measure: untyped.AnyMeasure) = untyped.AnyQuantity(value, measure)

    /**
      * Composes a quantity of supplied measure.
      */
    def *(measure: untyped.AnyMeasure): untyped.AnyQuantity[N] = apply(measure)

    /**
      * Dimensionless.
      */
    def unit = apply(com.quantarray.skylark.measure.Unit)

    def units = unit

    def percent = apply(com.quantarray.skylark.measure.percent)

    def bp = apply(com.quantarray.skylark.measure.bp)

    def rad = apply(com.quantarray.skylark.measure.rad)

    def sr = apply(com.quantarray.skylark.measure.sr)

    /**
      * Time.
      */
    def day = apply(com.quantarray.skylark.measure.day)

    def days = day

    /**
      * Mass.
      */
    def g = apply(com.quantarray.skylark.measure.g)

    def kg = apply(com.quantarray.skylark.measure.kg)

    def cg = apply(com.quantarray.skylark.measure.cg)

    def mg = apply(com.quantarray.skylark.measure.mg)

    def t = apply(com.quantarray.skylark.measure.t)

    def oz_metric = apply(com.quantarray.skylark.measure.oz_metric)

    def oz = apply(com.quantarray.skylark.measure.oz)

    def lb = apply(com.quantarray.skylark.measure.lb)

    def mt = apply(com.quantarray.skylark.measure.mt)

    def ton = apply(com.quantarray.skylark.measure.ton)

    def gr = apply(com.quantarray.skylark.measure.gr)

    def dwt = apply(com.quantarray.skylark.measure.dwt)

    def lb_troy = apply(com.quantarray.skylark.measure.lb_troy)

    def oz_troy = apply(com.quantarray.skylark.measure.oz_troy)

    /**
      * Length.
      */
    def m = apply(com.quantarray.skylark.measure.m)

    def in = apply(com.quantarray.skylark.measure.in)

    def ft = apply(com.quantarray.skylark.measure.ft)

    def yd = apply(com.quantarray.skylark.measure.yd)

    /**
      * Area.
      */
    def km2 = apply(com.quantarray.skylark.measure.km2)

    def ha = apply(com.quantarray.skylark.measure.ha)

    /**
      * Volume.
      */
    def in3 = apply(com.quantarray.skylark.measure.in3)

    def gal = apply(com.quantarray.skylark.measure.gal)

    def bbl = apply(com.quantarray.skylark.measure.bbl)

    /**
      * Energy.
      */
    def MMBtu = apply(com.quantarray.skylark.measure.MMBtu)

    /**
      * Currency.
      */
    def USD = apply(com.quantarray.skylark.measure.USD)
  }

  implicit final class DoubleQuantity(val value: Double) extends AnyVal with Measures[Double]
  {
    implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)
  }

  implicit final class IntQuantity(private val intValue: Int) extends AnyVal with Measures[Double]
  {
    implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)

    override def value: Double = intValue
  }

  object implicits extends AnyRef
                           with arithmetic.SafeArithmeticImplicits
                           with conversion.DefaultConversionImplicits
                           with simplification.DefaultSimplificationImplicits

}
