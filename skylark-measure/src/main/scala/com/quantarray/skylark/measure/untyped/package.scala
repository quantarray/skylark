package com.quantarray.skylark.measure

import com.quantarray.skylark.measure.untyped.arithmetic.SafeArithmeticImplicits

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

  object implicits extends AnyRef with SafeArithmeticImplicits
}
