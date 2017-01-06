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

package com.quantarray.skylark

package object measure extends DefaultDimensions
{
  type ⤇[From, To] = Conversion[From, To]

  val ⤇ = Conversion

  object measures extends DefaultMeasures
  {
    measuresScope =>

    @AscribeAnyMeasure[DefaultMeasures](measuresScope)
    object any

  }

  object quantities extends DefaultMeasures
  {

    measuresScope =>

    @QuantifyMeasure[DefaultMeasures, Quantity[Double, _]](measuresScope)
    class QuantifiedMeasures(val value: Double)

    object any
    {

      @QuantifyAnyMeasure[DefaultMeasures, Quantity[Double, AnyMeasure]](measuresScope)
      class QuantifiedAnyMeasures(val value: Double)

    }

  }

  type ExponentialLengthMeasure = ExponentialMeasure[LengthMeasure]

  type Price[M <: Measure[M], N <: Measure[N]] = RatioMeasure[M, N]

  type EnergyPrice = Price[Currency, EnergyMeasure]

  type FX = Price[Currency, Currency]

  object commodity
  {

    object us
    {

      import composition._

      object commercial
      {

        object grains
        {

          object corn
          {

            import com.quantarray.skylark.measure.measures._

            object shelled
            {
              val bushel: MassMeasure = "bushel" := 56 * lb
            }

          }

        }

      }

    }

  }

  object implicits extends AnyRef
                           with arithmetic.SafeArithmeticImplicits
                           with simplification.DefaultSimplificationImplicits
                           with conversion.DefaultConversionImplicits

  object any
  {

    val measures: com.quantarray.skylark.measure.measures.any.type = com.quantarray.skylark.measure.measures.any

    val quantities: com.quantarray.skylark.measure.quantities.any.type = com.quantarray.skylark.measure.quantities.any

    object arithmetic
    {

      val * = AnyProductMeasure

      val / = AnyRatioMeasure

      val ^ = AnyExponentialMeasure

      trait SafeArithmeticImplicits
      {
        implicit def exponentialCanExponentiate = new CanExponentiateMeasure[AnyMeasure, AnyMeasure]
        {
          override def pow(base: AnyMeasure, exponent: Double): AnyMeasure = AnyExponentialMeasure(base, exponent)
        }

        implicit def productCanMultiply = new CanMultiplyMeasure[AnyMeasure, AnyMeasure, AnyMeasure]
        {
          override def times(multiplicand: AnyMeasure, multiplier: AnyMeasure): AnyMeasure = AnyProductMeasure(multiplicand, multiplier)
        }

        implicit def ratioCanDivide = new CanDivideMeasure[AnyMeasure, AnyMeasure, AnyMeasure]
        {
          override def divide(numerator: AnyMeasure, denominator: AnyMeasure): AnyMeasure = AnyRatioMeasure(numerator, denominator)
        }

        implicit def lhsCanAddQuantity[N](implicit qn: QuasiNumeric[N]) = new CanAddQuantity[N, AnyMeasure, Quantity, AnyMeasure, Quantity, AnyMeasure]
        {
          type R = AnyMeasure

          type QR = Option[Quantity[N, AnyMeasure]]

          override def plus(addend1: AnyMeasure, addend2: AnyMeasure): AnyMeasure = addend1

          override def plus(addend1: Quantity[N, AnyMeasure], addend2: Quantity[N, AnyMeasure])
                           (implicit cc1: CanConvert[AnyMeasure, AnyMeasure], cc2: CanConvert[AnyMeasure, AnyMeasure]): QR =
          {
            val targetMeasure = plus(addend1.measure, addend2.measure)

            val a1 = cc1.convert(addend1.measure, targetMeasure).map(cf => qn.timesConstant(addend1.value, cf))
            val a2 = cc2.convert(addend2.measure, targetMeasure).map(cf => qn.timesConstant(addend2.value, cf))

            a1.flatMap(aa1 => a2.map(aa2 => qn.plus(aa1, aa2))).map(v => Quantity(v, targetMeasure))
          }
        }

        implicit def canDivideQuantity[N](implicit qn: QuasiNumeric[N]) = new CanDivideQuantity[N, AnyMeasure, Quantity, AnyMeasure, Quantity, AnyMeasure]
        {
          type QR = Quantity[N, AnyMeasure]

          override def divide(numerator: AnyMeasure, denominator: AnyMeasure): AnyMeasure = AnyRatioMeasure(numerator, denominator)

          override def divideQuantity(numerator: Quantity[N, AnyMeasure], denominator: Quantity[N, AnyMeasure]): QR =
            Quantity(qn.divide(numerator.value, denominator.value), divide(numerator.measure, denominator.measure))
        }

        implicit def canMultiplyQuantity[N](implicit qn: QuasiNumeric[N]) = new CanMultiplyQuantity[N, AnyMeasure, Quantity, AnyMeasure, Quantity, AnyMeasure]
        {
          type QR = Quantity[N, AnyMeasure]

          override def times(multiplicand: AnyMeasure, multiplier: AnyMeasure): AnyMeasure = AnyProductMeasure(multiplicand, multiplier)

          override def timesQuantity(multiplicand: Quantity[N, AnyMeasure], multiplier: Quantity[N, AnyMeasure]): QR =
            Quantity(qn.times(multiplicand.value, multiplier.value), times(multiplicand.measure, multiplier.measure))
        }

        implicit def canExponentiateQuantity[N](implicit qn: QuasiNumeric[N]) = new CanExponentiateQuantity[N, AnyMeasure, Quantity, AnyMeasure]
        {
          type QR = Quantity[N, AnyMeasure]

          override def pow(base: AnyMeasure, exponent: Double): AnyMeasure = AnyExponentialMeasure(base, exponent)

          override def powQuantity(base: Quantity[N, AnyMeasure], exponent: Double): QR = Quantity(qn.pow(base.value, exponent), pow(base.measure, exponent))
        }
      }

      object safe extends SafeArithmeticImplicits

      object unsafe extends SafeArithmeticImplicits
      {
        implicit def lhsCanAddQuantityUnsafe[N](implicit qn: QuasiNumeric[N]) = new CanAddQuantity[N, AnyMeasure, Quantity, AnyMeasure, Quantity, AnyMeasure]
        {
          type R = AnyMeasure

          type QR = Quantity[N, AnyMeasure]

          override def plus(addend1: AnyMeasure, addend2: AnyMeasure): AnyMeasure = addend1

          override def plus(addend1: Quantity[N, AnyMeasure], addend2: Quantity[N, AnyMeasure])
                           (implicit cc1: CanConvert[AnyMeasure, AnyMeasure], cc2: CanConvert[AnyMeasure, AnyMeasure]): QR =
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

    object conversion
    {

      import cats.implicits._
      import arithmetic._

      trait DefaultMeasureConverter extends AnyMeasureConverter with com.quantarray.skylark.measure.conversion.DefaultConversionImplicits
      {
        protected override def convert(from: AnyMeasure, to: AnyMeasure): Option[Double] = ⤇(from, to) match
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

      trait DefaultMeasureConverterWithCanConvert extends DefaultMeasureConverter
      {

        import com.quantarray.skylark.measure.measures._

        implicit def canConvert: CanConvert[AnyMeasure, AnyMeasure]

        protected override def convert(from: AnyMeasure, to: AnyMeasure): Option[Double] = ⤇(from, to) match
        {
          case (x * (same1 / y)) ⤇ same2 if same1 == same2 => x to y
          case ((x ^ -1.0) * (same1 * y)) ⤇ same2 if same1 == same2 => y to x
          case (x / (dm: DimensionlessMeasure)) ⤇ y => x.to(y) |@| Unit.to(dm) map
            {
              _ * _
            }
          case (x * ((dm: DimensionlessMeasure) ^ -1.0)) ⤇ y => x.to(y) |@| Unit.to(dm) map
            {
              _ * _
            }
          case _ => super.convert(from, to)
        }
      }

      trait DefaultConversionImplicits
      {

        implicit val measureCanConvert = new CanConvert[AnyMeasure, AnyMeasure]
        {
          private val cc = this

          override def convert: Converter[AnyMeasure, AnyMeasure] = new DefaultMeasureConverterWithCanConvert
          {
            implicit def canConvert: CanConvert[AnyMeasure, AnyMeasure] = cc
          }
        }

      }

      object default extends DefaultConversionImplicits

    }

    object simplification
    {

      import scala.collection.immutable.SortedMap

      case object DefaultReducer extends Reducer[AnyMeasure, AnyMeasure]
      {

        import com.quantarray.skylark.measure.measures._

        implicit val measureOrdering: Ordering[AnyMeasure] = Ordering.by
        {
          measure: AnyMeasure => measure.name
        }

        type ExponentMap = SortedMap[AnyMeasure, Double]

        val ExponentMap = SortedMap

        override def apply(from: AnyMeasure): AnyMeasure =
        {
          val es = exponentials(deflate(from), 1.0, ExponentMap.empty[AnyMeasure, Double])

          val productOfExponentials = es.groupBy(_._1).values.map(x => x.reduce((x, y) => (x._1, x._2 + y._2))).filter(_._2 != 0).toList

          val reduced = productOfExponentials.size match
          {
            case 0 => Unit
            case 1 => exponential(productOfExponentials.head)
            case 2 => AnyProductMeasure(exponential(productOfExponentials.head), exponential(productOfExponentials(1)))
            case _ =>
              val z = AnyProductMeasure(exponential(productOfExponentials.head), exponential(productOfExponentials(1)))
              productOfExponentials.takeRight(productOfExponentials.size - 2).foldLeft(z)((r, p) => AnyProductMeasure(exponential(p), r))
          }

          reduced
        }

        private def deflate(inflated: AnyMeasure): AnyMeasure =
        {
          def deflateProduct(inflated: AnyProductMeasure): AnyMeasure = deflateNonRecompose.orElse(giveUp)(inflated)

          def deflateNonRecompose: PartialFunction[AnyMeasure, AnyMeasure] =
          {
            case AnyProductMeasure(md, mr) if md == Unit && mr == Unit => Unit
            case AnyProductMeasure(md, mr) if md == Unit => deflate(mr)
            case AnyProductMeasure(md, mr) if mr == Unit => deflate(md)
            case AnyProductMeasure(md, AnyRatioMeasure(nr, dr)) if md == dr => deflate(nr)
            case AnyProductMeasure(AnyRatioMeasure(nr, dr), mr) if dr == mr => deflate(nr)
            case AnyRatioMeasure(nr, Unit) => deflate(nr)
            case AnyExponentialMeasure(Unit, _) => Unit
            case AnyExponentialMeasure(base, exponent) if exponent == 1.0 => deflate(base)
          }

          def deflateRecompose: PartialFunction[AnyMeasure, AnyMeasure] =
          {
            case AnyProductMeasure(md, mr) => deflateProduct(AnyProductMeasure(deflate(md), deflate(mr)))
            case AnyExponentialMeasure(base, exponent) => AnyExponentialMeasure(deflate(base), exponent)
          }

          def giveUp: PartialFunction[AnyMeasure, AnyMeasure] =
          {
            case measure => measure
          }

          deflateNonRecompose.orElse(deflateRecompose).orElse(giveUp)(inflated)
        }

        private def exponentials(measure: AnyMeasure, outerExponent: Double, map: ExponentMap): ExponentMap = measure match
        {
          case AnyProductMeasure(md, mr) => exponentials(md, outerExponent, exponentials(mr, outerExponent, map))
          case AnyRatioMeasure(nr, dr) => exponentials(nr, outerExponent, exponentials(dr, -outerExponent, map))
          case AnyExponentialMeasure(base, exponent) => exponentials(base, exponent * outerExponent, map)
          case _ => map + (measure -> (map.getOrElse(measure, 0.0) + measure.exponent * outerExponent))
        }

        private def exponential(measure: (AnyMeasure, Double)): AnyMeasure = measure match
        {
          case (x, 1.0) => x
          case _ => AnyExponentialMeasure(measure._1, measure._2)
        }
      }

      trait DefaultSimplificationImplicits
      {

        import com.quantarray.skylark.measure.measures._

        case object ProductOfExponentials
        {
          def apply(from: AnyMeasure): AnyProductMeasure = product(from, 1)

          private def product(from: AnyMeasure, outerExponent: Double): AnyProductMeasure = from match
          {
            case AnyProductMeasure(md, mr) => AnyProductMeasure(product(md, outerExponent), product(mr, outerExponent))
            case AnyRatioMeasure(nr, dr) => AnyProductMeasure(product(nr, outerExponent), product(dr, -outerExponent))
            case AnyExponentialMeasure(base, exponent) => AnyProductMeasure(product(base, exponent * outerExponent), Unit)
            case _ if outerExponent == 1.0 => AnyProductMeasure(from, Unit)
            case _ => AnyProductMeasure(AnyExponentialMeasure(from, outerExponent), Unit)
          }
        }

        implicit val defaultCanSimplify = new CanSimplifyMeasure[AnyMeasure, AnyMeasure]
        {
          override def simplify(inflated: AnyMeasure): AnyMeasure =
          {
            val productOfExponentials = ProductOfExponentials(inflated)

            DefaultReducer(productOfExponentials)
          }
        }

        implicit val defaultCanSimplifyQuantity = new CanSimplifyQuantity[Double, AnyMeasure, Quantity, AnyMeasure]
        {
          type QR = Quantity[Double, AnyMeasure]

          override def simplify(inflated: AnyMeasure): AnyMeasure = defaultCanSimplify.simplify(inflated)

          override def simplifyQuantity(inflated: Quantity[Double, AnyMeasure]): QR = Quantity(inflated.value, simplify(inflated.measure))
        }

      }

      object default extends DefaultSimplificationImplicits

    }

    object implicits extends AnyRef
                             with arithmetic.SafeArithmeticImplicits
                             with conversion.DefaultConversionImplicits
                             with simplification.DefaultSimplificationImplicits

  }

}
