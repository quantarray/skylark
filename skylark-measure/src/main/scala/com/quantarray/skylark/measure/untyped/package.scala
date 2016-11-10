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

  type ⤇[From, To] = Conversion[From, To]

  val ⤇ = Conversion

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

    trait DefaultConversionImplicits extends com.quantarray.skylark.measure.conversion.DefaultConversionImplicits
    {

      implicit object MeasureCanConvert extends CanConvert[untyped.Measure, untyped.Measure]
      {

        override def convert: Converter[untyped.Measure, untyped.Measure] = new MeasureConverter
        {
          override def convert(from: untyped.Measure, to: untyped.Measure): Option[Double] = ⤇(from, to) match
          {
            case (dm1: DimensionlessMeasure) ⤇ (dm2: DimensionlessMeasure) => dimensionlessCanConvert.convert(dm1, dm2)
            case (tm1: TimeMeasure) ⤇ (tm2: TimeMeasure) => timeCanConvert.convert(tm1, tm2)
            case (mm1: MassMeasure) ⤇ (mm2: MassMeasure) => massCanConvert.convert(mm1, mm2)
            case (lm1: LengthMeasure) ⤇ (lm2: LengthMeasure) => lengthCanConvert.convert(lm1, lm2)
            case (em1: EnergyMeasure) ⤇ (em2: EnergyMeasure) => energyCanConvert.convert(em1, em2)
            case (ccy1: Currency) ⤇ (ccy2: Currency) => currencyCanConvert.convert(ccy1, ccy2)
            case (diff1 * (same1 / diff2)) ⤇ same2 if same1 == same2 => diff1.to(diff2)
            case _ => super.convert(from, to)
          }
        }
      }

    }

    object default extends DefaultConversionImplicits

  }

  object implicits extends AnyRef
                           with arithmetic.SafeArithmeticImplicits
                           with conversion.DefaultConversionImplicits

}
