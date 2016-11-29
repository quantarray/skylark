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
  val * = ProductMeasure
  val / = RatioMeasure
  val ^ = ExponentialMeasure

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

      @QuantifyAnyMeasure[DefaultMeasures, AnyQuantity[Double]](measuresScope)
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
    type AnyDimension = measure.AnyDimension

    type AnyMeasure = measure.AnyMeasure

    type AnyProductMeasure = measure.AnyProductMeasure

    val * = AnyProductMeasure

    type AnyRatioMeasure = measure.AnyRatioMeasure

    val / = AnyRatioMeasure

    type AnyExponentialMeasure = measure.AnyExponentialMeasure

    val ^ = AnyExponentialMeasure

    type AnyMeasureConverter = measure.AnyMeasureConverter

    type AnyQuantity[N] = measure.AnyQuantity[N]

    val AnyQuantity = measure.AnyQuantity

    type AreaMeasure = measure.AreaMeasure

    type BinaryMultiple = measure.BinaryMultiple

    type CanAdd[A1, A2] = measure.CanAdd[A1, A2]

    type CanAddAnyQuantity[N, A1, A2] = measure.CanAddAnyQuantity[N, A1, A2]

    type CanAddQuantity[N, M1 <: Measure[M1], A1 <: Quantity[N, M1], M2 <: Measure[M2], A2 <: Quantity[N, M2], RM <: Measure[RM]] = measure.CanAddQuantity[N, M1, A1, M2, A2, RM]

    type CanConvert[From, To] = measure.CanConvert[From, To]

    type CanDivide[N, D, R] = measure.CanDivide[N, D, R]

    type CanExponentiate[B, R] = measure.CanExponentiate[B, R]

    type CanMultiply[M1, M2, R] = measure.CanMultiply[M1, M2, R]

    type CanSimplify[I, D] = measure.CanSimplify[I, D]

    type Conversion[From, To] = measure.Conversion[From, To]

    val Conversion = measure.Conversion

    type Converter[From, To] = measure.Converter[From, To]

    type ConvertException = measure.ConvertException

    type Currency = measure.Currency

    type DecadicMultiple = measure.DecadicMultiple

    type DefaultDimensions = measure.DefaultDimensions

    type DefaultMeasures = measure.DefaultMeasures

    type Dimension[Self <: Dimension[Self]] = measure.Dimension[Self]

    type ProductDimension[D1 <: Dimension[D1], D2 <: Dimension[D2]] = measure.ProductDimension[D1, D2]

    type RatioDimension[D1 <: Dimension[D1], D2 <: Dimension[D2]] = measure.RatioDimension[D1, D2]

    type ExponentialDimension[B <: Dimension[B]] = measure.ExponentialDimension[B]

    val DimensionlessConverter = measure.DimensionlessConverter

    type DimensionlessMeasure = measure.DimensionlessMeasure

    type ⤇[From, To] = measure.⤇[From, To]

    val ⤇ : Conversion.type = measure.⤇

    object arithmetic
    {

      trait SafeArithmeticImplicits
      {
        implicit def exponentialCanExponentiate = new CanExponentiate[AnyMeasure, AnyMeasure]
        {
          override def pow(base: AnyMeasure, exponent: Double): AnyMeasure = AnyExponentialMeasure(base, exponent)
        }

        implicit def productCanMultiply = new CanMultiply[AnyMeasure, AnyMeasure, AnyMeasure]
        {
          override def times(multiplicand: AnyMeasure, multiplier: AnyMeasure): AnyMeasure = AnyProductMeasure(multiplicand, multiplier)
        }

        implicit def ratioCanDivide = new CanDivide[AnyMeasure, AnyMeasure, AnyMeasure]
        {
          override def divide(numerator: AnyMeasure, denominator: AnyMeasure): AnyMeasure = AnyRatioMeasure(numerator, denominator)
        }

        implicit def lhsCanAddQuantity = new CanAddAnyQuantity[Double, AnyQuantity[Double], AnyQuantity[Double]]
        {
          type R = AnyMeasure

          type QR = Option[AnyQuantity[Double]]

          override def plus(addend1: AnyMeasure, addend2: AnyMeasure): AnyMeasure = addend1

          override def plus(addend1: AnyQuantity[Double], addend2: AnyQuantity[Double])(implicit cc: CanConvert[AnyMeasure, AnyMeasure]): QR =
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
        implicit def lhsCanAddQuantityUnsafe = new CanAddAnyQuantity[Double, AnyQuantity[Double], AnyQuantity[Double]]
        {
          type R = AnyMeasure

          type QR = AnyQuantity[Double]

          override def plus(addend1: AnyMeasure, addend2: AnyMeasure): AnyMeasure = addend1

          override def plus(addend1: AnyQuantity[Double], addend2: AnyQuantity[Double])(implicit cc: CanConvert[AnyMeasure, AnyMeasure]): QR =
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
        override def convert(from: AnyMeasure, to: AnyMeasure): Option[Double] = ⤇(from, to) match
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

        implicit val measureCanConvert = new CanConvert[AnyMeasure, AnyMeasure]
        {

          implicit val canConvert: CanConvert[AnyMeasure, AnyMeasure] = implicitly(this)

          override def convert: Converter[AnyMeasure, AnyMeasure] = new DefaultMeasureConverter
          {
            override def convert(from: AnyMeasure, to: AnyMeasure): Option[Double] = ⤇(from, to) match
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

      case object DefaultReducer extends Reducer[AnyMeasure, AnyMeasure]
      {

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
            case 0 => measures.Unit
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
            case AnyProductMeasure(md, mr) if md == measures.Unit && mr == measures.Unit => measures.Unit
            case AnyProductMeasure(md, mr) if md == measures.Unit => deflate(mr)
            case AnyProductMeasure(md, mr) if mr == measures.Unit => deflate(md)
            case AnyProductMeasure(md, mr@AnyRatioMeasure(nr, dr)) if md == dr => deflate(nr)
            case AnyProductMeasure(md@AnyRatioMeasure(nr, dr), mr) if dr == mr => deflate(nr)
            case AnyRatioMeasure(nr, measures.Unit) => deflate(nr)
            case AnyExponentialMeasure(measures.Unit, _) => measures.Unit
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

        case object ProductOfExponentials
        {
          def apply(from: AnyMeasure): AnyProductMeasure = product(from, 1)

          private def product(from: AnyMeasure, outerExponent: Double): AnyProductMeasure = from match
          {
            case AnyProductMeasure(md, mr) => AnyProductMeasure(product(md, outerExponent), product(mr, outerExponent))
            case AnyRatioMeasure(nr, dr) => AnyProductMeasure(product(nr, outerExponent), product(dr, -outerExponent))
            case AnyExponentialMeasure(base, exponent) => AnyProductMeasure(product(base, exponent * outerExponent), measures.Unit)
            case _ if outerExponent == 1.0 => AnyProductMeasure(from, measures.Unit)
            case _ => AnyProductMeasure(AnyExponentialMeasure(from, outerExponent), measures.Unit)
          }
        }

        implicit val defaultCanSimplify = new CanSimplify[AnyMeasure, AnyMeasure]
        {
          override def simplify(inflated: AnyMeasure): AnyMeasure =
          {
            val productOfExponentials = ProductOfExponentials(inflated)

            DefaultReducer(productOfExponentials)
          }
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
