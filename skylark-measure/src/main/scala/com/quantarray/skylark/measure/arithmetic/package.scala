package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2017, Quantarray
 * http://skylark.io
 */
package object arithmetic
{
  val * = ProductMeasure

  val / = RatioMeasure

  val ^ = ExponentialMeasure

  trait SafeArithmeticImplicits
  {
    implicit def productCanMultiply[M1 <: Measure[M1], M2 <: Measure[M2]] = new CanMultiplyMeasure[M1, M2, ProductMeasure[M1, M2]]
    {
      override def times(multiplicand: M1, multiplier: M2): ProductMeasure[M1, M2] = ProductMeasure(multiplicand, multiplier)
    }

    implicit def ratioCanDivide[N <: Measure[N], D <: Measure[D]] = new CanDivideMeasure[N, D, RatioMeasure[N, D]]
    {
      override def divide(numerator: N, denominator: D): RatioMeasure[N, D] = RatioMeasure(numerator, denominator)
    }

    implicit def exponentialCanExponentiate[B <: Measure[B]] = new CanExponentiateMeasure[B, ExponentialMeasure[B]]
    {
      override def pow(base: B, exponent: Double): ExponentialMeasure[B] = ExponentialMeasure(base, exponent)
    }

    implicit def lhsCanAdd[M1 <: Measure[M1], M2 <: Measure[M2]] = new CanAddMeasure[M1, M2]
    {
      type R = M1

      override def plus(addend1: M1, addend2: M2): R = addend1
    }

    implicit def lhsCanAddQuantity[N, M <: Measure[M]](implicit qn: QuasiNumeric[N]) = new CanAddQuantity[N, M, Quantity, M, Quantity, M]
    {
      type R = M

      type QR = Option[Quantity[N, M]]

      override def plus(addend1: M, addend2: M): M = addend1

      override def plus(addend1: Quantity[N, M], addend2: Quantity[N, M])(implicit cc1: CanConvert[M, M], cc2: CanConvert[M, M]): QR =
      {
        val targetMeasure = plus(addend1.measure, addend2.measure)

        val a1 = cc1.convert(addend1.measure, targetMeasure).map(cf => qn.timesConstant(addend1.value, cf))
        val a2 = cc2.convert(addend2.measure, targetMeasure).map(cf => qn.timesConstant(addend2.value, cf))

        a1.flatMap(aa1 => a2.map(aa2 => qn.plus(aa1, aa2))).map(v => Quantity(v, targetMeasure))
      }
    }

    implicit def canDivideQuantity[N, M1 <: Measure[M1], M2 <: Measure[M2]](implicit qn: QuasiNumeric[N]) =
      new CanDivideQuantity[N, M1, Quantity, M2, Quantity, RatioMeasure[M1, M2]]
      {
        type QR = Quantity[N, RatioMeasure[M1, M2]]

        override def divide(numerator: M1, denominator: M2): RatioMeasure[M1, M2] = RatioMeasure(numerator, denominator)

        override def divideQuantity(numerator: Quantity[N, M1], denominator: Quantity[N, M2]): QR =
          Quantity(qn.divide(numerator.value, denominator.value), divide(numerator.measure, denominator.measure))
      }

    implicit def canMultiplyQuantity[N, M1 <: Measure[M1], M2 <: Measure[M2]](implicit qn: QuasiNumeric[N]) =
      new CanMultiplyQuantity[N, M1, Quantity, M2, Quantity, ProductMeasure[M1, M2]]
      {
        type QR = Quantity[N, ProductMeasure[M1, M2]]

        override def times(numerator: M1, denominator: M2): ProductMeasure[M1, M2] = ProductMeasure(numerator, denominator)

        override def timesQuantity(numerator: Quantity[N, M1], denominator: Quantity[N, M2]): QR = Quantity(qn.times(numerator.value, denominator.value),
          times(numerator.measure, denominator.measure))
      }

    implicit def canExponentiateQuantity[N, B <: Measure[B]](implicit qn: QuasiNumeric[N]) = new CanExponentiateQuantity[N, B, Quantity, ExponentialMeasure[B]]
    {
      type QR = Quantity[N, ExponentialMeasure[B]]

      override def pow(base: B, exponent: Double): ExponentialMeasure[B] = ExponentialMeasure(base, exponent)

      override def powQuantity(base: Quantity[N, B], exponent: Double): QR = Quantity(qn.pow(base.value, exponent), pow(base.measure, exponent))
    }
  }

  object safe extends SafeArithmeticImplicits

  object unsafe extends SafeArithmeticImplicits
  {
    implicit def lhsCanAddQuantityUnsafe[N, M <: Measure[M]](implicit qn: QuasiNumeric[N]) = new CanAddQuantity[N, M, Quantity, M, Quantity, M]
    {
      type R = M

      type QR = Quantity[N, M]

      override def plus(addend1: M, addend2: M): M = addend1

      override def plus(addend1: Quantity[N, M], addend2: Quantity[N, M])(implicit cc1: CanConvert[M, M], cc2: CanConvert[M, M]): QR =
      {
        val targetMeasure = plus(addend1.measure, addend2.measure)

        val a1 = cc1.convert(addend1.measure, targetMeasure).map(cf => qn.timesConstant(addend1.value, cf))
        val a2 = cc2.convert(addend2.measure, targetMeasure).map(cf => qn.timesConstant(addend2.value, cf))

        (a1, a2) match
        {
          case (Some(aa1), Some(aa2)) => Quantity(qn.plus(aa1, aa2), targetMeasure)
          case (Some(_), _) => throw ConvertException(addend2.measure, targetMeasure)
          case (_, Some(_)) => throw ConvertException(addend1.measure, targetMeasure)
          case _ => throw ConvertException(s"Cannot convert to $targetMeasure.")
        }
      }
    }
  }

}
