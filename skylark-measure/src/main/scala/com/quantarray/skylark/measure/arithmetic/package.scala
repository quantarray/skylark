package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object arithmetic
{
  val * = ProductMeasure

  val / = RatioMeasure

  val ^ = ExponentialMeasure

  trait SafeArithmeticImplicits
  {
    implicit def productCanMultiply[M1 <: Measure[M1], M2 <: Measure[M2]] = new CanMultiply[M1, M2, ProductMeasure[M1, M2]]
    {
      override def times(multiplicand: M1, multiplier: M2): ProductMeasure[M1, M2] = ProductMeasure(multiplicand, multiplier)
    }

    implicit def ratioCanDivide[N <: Measure[N], D <: Measure[D]] = new CanDivide[N, D, RatioMeasure[N, D]]
    {
      override def divide(numerator: N, denominator: D): RatioMeasure[N, D] = RatioMeasure(numerator, denominator)
    }

    implicit def exponentialCanExponentiate[B <: Measure[B]] = new CanExponentiate[B, ExponentialMeasure[B]]
    {
      override def pow(base: B, exponent: Double): ExponentialMeasure[B] = ExponentialMeasure(base, exponent)
    }

    implicit def lhsCanAdd[M1 <: Measure[M1], M2 <: Measure[M2]] = new CanAdd[M1, M2]
    {
      type R = M1

      override def plus(addend1: M1, addend2: M2): R = addend1
    }

    implicit def lhsCanAddQuantity[M <: Measure[M], A1 <: Quantity[Double, M], A2 <: Quantity[Double, M]] = new CanAddQuantity[Double, M, A1, M, A2, M]
    {
      type R = M

      type QR = Option[Quantity[Double, M]]

      override def plus(addend1: M, addend2: M): M = addend1

      override def plus(addend1: A1, addend2: A2)(implicit cc1: CanConvert[M, M], cc2: CanConvert[M, M]): QR =
      {
        val targetMeasure = plus(addend1.measure, addend2.measure)

        val a1 = cc1.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
        val a2 = cc2.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

        a1.flatMap(aa1 => a2.map(_ + aa1)).map(v => Quantity(v, targetMeasure))
      }
    }
  }

  object safe extends SafeArithmeticImplicits

  object unsafe extends SafeArithmeticImplicits
  {
    implicit def lhsCanAddQuantityUnsafe[M <: Measure[M], A1 <: Quantity[Double, M], A2 <: Quantity[Double, M]] = new CanAddQuantity[Double, M, A1, M, A2, M]
    {
      type R = M

      type QR = Quantity[Double, M]

      override def plus(addend1: M, addend2: M): M = addend1

      override def plus(addend1: A1, addend2: A2)(implicit cc1: CanConvert[M, M], cc2: CanConvert[M, M]): QR =
      {
        val targetMeasure = plus(addend1.measure, addend2.measure)

        val a1 = cc1.convert(addend1.measure, targetMeasure).map(_ * addend1.value)
        val a2 = cc2.convert(addend2.measure, targetMeasure).map(_ * addend2.value)

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
