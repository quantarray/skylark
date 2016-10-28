package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure._

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */

package object arithmetic
{

  trait DefaultImplicits
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

    implicit def lhsCanAddQuantity[A1 <: Quantity[Double], A2 <: Quantity[Double]] = new CanAddQuantity[Double, A1, A2]
    {
      type R = untyped.Measure

      type QR = Option[Quantity[Double]]

      override def plus(addend1: Measure, addend2: Measure): Measure = addend1

      override def plus(addend1: A1, addend2: A2)(implicit cc: CanConvert[untyped.Measure, untyped.Measure]): QR =
      {
        val targetMeasure = plus(addend1.measure, addend2.measure)

        val a1 = cc.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
        val a2 = cc.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

        a1.flatMap(aa1 => a2.map(_ + aa1)).map(v => Quantity(v, targetMeasure))
      }
    }
  }

  object default extends DefaultImplicits
  {
  }

  object unsafe extends DefaultImplicits
  {
    implicit def lhsCanAddQuantityUnsafe[A1 <: Quantity[Double], A2 <: Quantity[Double]] = new CanAddQuantity[Double, A1, A2]
    {
      type R = untyped.Measure

      type QR = Quantity[Double]

      override def plus(addend1: Measure, addend2: Measure): Measure = addend1

      override def plus(addend1: A1, addend2: A2)(implicit cc: CanConvert[untyped.Measure, untyped.Measure]): QR =
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