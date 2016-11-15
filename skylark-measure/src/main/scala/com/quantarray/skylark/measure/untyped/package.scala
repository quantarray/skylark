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
  final val Unit: untyped.Measure = com.quantarray.skylark.measure.Unit

  val bp: untyped.Measure = com.quantarray.skylark.measure.bp

  /**
    * Time.
    */
  val s: untyped.Measure = com.quantarray.skylark.measure.s

  /**
    * Mass.
    */
  val g: untyped.Measure = com.quantarray.skylark.measure.g

  val kg: untyped.Measure = com.quantarray.skylark.measure.kg

  val mt: untyped.Measure = com.quantarray.skylark.measure.mt

  /**
    * Volume.
    */
  val bbl: untyped.Measure = com.quantarray.skylark.measure.bbl

  val gal: untyped.Measure = com.quantarray.skylark.measure.gal

  /**
    * Length.
    */
  val m: untyped.Measure = com.quantarray.skylark.measure.m

  /**
    * Currency.
    */
  val USD: untyped.Measure = com.quantarray.skylark.measure.USD

  object arithmetic
  {

    trait SafeArithmeticImplicits
    {
      implicit def exponentialCanExponentiate = new CanExponentiate[untyped.Measure, untyped.Measure]
      {
        override def pow(base: untyped.Measure, exponent: Double): untyped.Measure = ExponentialMeasure(base, exponent)
      }

      implicit def productCanMultiply = new CanMultiply[untyped.Measure, untyped.Measure, untyped.Measure]
      {
        override def times(multiplicand: untyped.Measure, multiplier: untyped.Measure): untyped.Measure = ProductMeasure(multiplicand, multiplier)
      }

      implicit def ratioCanDivide = new CanDivide[untyped.Measure, untyped.Measure, untyped.Measure]
      {
        override def divide(numerator: untyped.Measure, denominator: untyped.Measure): untyped.Measure = RatioMeasure(numerator, denominator)
      }

      implicit def lhsCanAddQuantity = new untyped.CanAddQuantity[Double, untyped.Quantity[Double], untyped.Quantity[Double]]
      {
        type R = untyped.Measure

        type QR = Option[untyped.Quantity[Double]]

        override def plus(addend1: untyped.Measure, addend2: untyped.Measure): untyped.Measure = addend1

        override def plus(addend1: untyped.Quantity[Double], addend2: untyped.Quantity[Double])(implicit cc: CanConvert[untyped.Measure, untyped.Measure]): QR =
        {
          val targetMeasure = plus(addend1.measure, addend2.measure)

          val a1 = cc.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
          val a2 = cc.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

          a1.flatMap(aa1 => a2.map(_ + aa1)).map(v => Quantity(v, targetMeasure))
        }
      }
    }

    object safe extends SafeArithmeticImplicits
    {
    }

    object unsafe extends SafeArithmeticImplicits
    {
      implicit def lhsCanAddQuantityUnsafe = new untyped.CanAddQuantity[Double, untyped.Quantity[Double], untyped.Quantity[Double]]
      {
        type R = untyped.Measure

        type QR = untyped.Quantity[Double]

        override def plus(addend1: untyped.Measure, addend2: untyped.Measure): untyped.Measure = addend1

        override def plus(addend1: untyped.Quantity[Double], addend2: untyped.Quantity[Double])(implicit cc: CanConvert[untyped.Measure, untyped.Measure]): QR =
        {
          val targetMeasure = plus(addend1.measure, addend2.measure)

          val a1 = cc.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
          val a2 = cc.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

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

  object conversion
  {

    trait DefaultMeasureConverter extends MeasureConverter with com.quantarray.skylark.measure.conversion.DefaultConversionImplicits
    {
      override def convert(from: untyped.Measure, to: untyped.Measure): Option[Double] = ⤇(from, to) match
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

      implicit val measureCanConvert = new CanConvert[untyped.Measure, untyped.Measure]
      {

        implicit val canConvert: CanConvert[untyped.Measure, untyped.Measure] = implicitly(this)

        override def convert: Converter[untyped.Measure, untyped.Measure] = new DefaultMeasureConverter
        {
          override def convert(from: untyped.Measure, to: untyped.Measure): Option[Double] = ⤇(from, to) match
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

    case object DefaultReducer extends Reducer[untyped.Measure, untyped.Measure]
    {

      implicit val measureOrdering = Ordering.by
      {
        measure: untyped.Measure => measure.name
      }

      type ExponentMap = SortedMap[untyped.Measure, Double]

      val ExponentMap = SortedMap

      override def apply(from: untyped.Measure): untyped.Measure =
      {
        val es = exponentials(deflate(from), 1.0, ExponentMap.empty[untyped.Measure, Double])

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

      private def deflate(inflated: untyped.Measure): untyped.Measure =
      {
        def deflateProduct(inflated: untyped.ProductMeasure): untyped.Measure = deflateNonRecompose.orElse(giveUp)(inflated)

        def deflateNonRecompose: PartialFunction[untyped.Measure, untyped.Measure] =
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

        def deflateRecompose: PartialFunction[untyped.Measure, untyped.Measure] =
        {
          case untyped.ProductMeasure(md, mr) => deflateProduct(ProductMeasure(deflate(md), deflate(mr)))
          case untyped.ExponentialMeasure(base, exponent) => ExponentialMeasure(deflate(base), exponent)
        }

        def giveUp: PartialFunction[untyped.Measure, untyped.Measure] =
        {
          case measure => measure
        }

        deflateNonRecompose.orElse(deflateRecompose).orElse(giveUp)(inflated)
      }

      private def exponentials(measure: untyped.Measure, outerExponent: Double, map: ExponentMap): ExponentMap = measure match
      {
        case untyped.ProductMeasure(md, mr) => exponentials(md, outerExponent, exponentials(mr, outerExponent, map))
        case untyped.RatioMeasure(nr, dr) => exponentials(nr, outerExponent, exponentials(dr, -outerExponent, map))
        case untyped.ExponentialMeasure(base, exponent) => exponentials(base, exponent * outerExponent, map)
        case _ => map + (measure -> (map.getOrElse(measure, 0.0) + measure.exponent * outerExponent))
      }

      private def exponential(measure: (untyped.Measure, Double)): untyped.Measure = measure match
      {
        case (x, 1.0) => x
        case _ => untyped.ExponentialMeasure(measure._1, measure._2)
      }
    }

    trait DefaultSimplificationImplicits
    {

      case object ProductOfExponentials
      {
        def apply(from: untyped.Measure): untyped.ProductMeasure = product(from, 1)

        private def product(from: untyped.Measure, outerExponent: Double): untyped.ProductMeasure = from match
        {
          case untyped.ProductMeasure(md, mr) => ProductMeasure(product(md, outerExponent), product(mr, outerExponent))
          case untyped.RatioMeasure(nr, dr) => ProductMeasure(product(nr, outerExponent), product(dr, -outerExponent))
          case untyped.ExponentialMeasure(base, exponent) => ProductMeasure(product(base, exponent * outerExponent), Unit)
          case _ if outerExponent == 1.0 => ProductMeasure(from, Unit)
          case _ => ProductMeasure(ExponentialMeasure(from, outerExponent), Unit)
        }
      }

      implicit val defaultCanSimplify = new CanSimplify[untyped.Measure, untyped.Measure]
      {
        override def simplify(inflated: untyped.Measure): untyped.Measure =
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
    def apply(measure: untyped.Measure) = untyped.Quantity(value, measure)

    /**
      * Composes a quantity of supplied measure.
      */
    def *(measure: untyped.Measure): untyped.Quantity[N] = apply(measure)

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

  object implicits extends AnyRef
                           with arithmetic.SafeArithmeticImplicits
                           with conversion.DefaultConversionImplicits
                           with simplification.DefaultSimplificationImplicits
  {

    implicit final class DoubleQuantity(val value: Double) extends AnyVal with Measures[Double]
    {
      implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)
    }

    implicit final class IntQuantity(private val intValue: Int) extends AnyVal with Measures[Double]
    {
      implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)

      override def value: Double = intValue
    }

  }

}
