package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure._

import scala.collection.immutable.SortedMap

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object reduction
{

  case object DefaultReducer extends Reducer[Measure, Measure]
  {

    implicit val measureOrdering = Ordering.by
    {
      measure: Measure => measure.name
    }

    type ExponentMap = SortedMap[Measure, Double]

    val ExponentMap = SortedMap

    override def apply(from: Measure): Measure =
    {
      val es = exponentials(deflate(from), 1.0, ExponentMap.empty[Measure, Double])

      val productOfExponentials = es.groupBy(_._1).values.map(x => x.reduce((x, y) => (x._1, x._2 + y._2))).filter(_._2 != 0).toList

      val reduced = productOfExponentials.size match
      {
        case 1 => exponential(productOfExponentials.head)
        case 2 => ProductMeasure(exponential(productOfExponentials.head), exponential(productOfExponentials(1)))
        case _ =>
          val z = ProductMeasure(exponential(productOfExponentials.head), exponential(productOfExponentials(1)))
          productOfExponentials.takeRight(productOfExponentials.size - 2).foldLeft(z)((r, p) => ProductMeasure(exponential(p), r))
      }

      reduced
    }

    private def deflate(inflated: Measure): Measure =
    {
      def deflateProduct(inflated: ProductMeasure): Measure = deflateNonRecompose.orElse(giveUp)(inflated)

      def deflateNonRecompose: PartialFunction[Measure, Measure] =
      {
        case ProductMeasure(md, mr) if md == Unit && mr == Unit => Unit
        case ProductMeasure(md, mr) if md == Unit => deflate(mr)
        case ProductMeasure(md, mr) if mr == Unit => deflate(md)
        case ProductMeasure(md, mr@RatioMeasure(nr, dr)) if md == dr => deflate(nr)
        case ProductMeasure(md@RatioMeasure(nr, dr), mr) if dr == mr => deflate(nr)
        case RatioMeasure(nr, Unit) => deflate(nr)
        case ExponentialMeasure(Unit, _) => Unit
        case ExponentialMeasure(base, exponent) if exponent == 1.0 => deflate(base)
      }

      def deflateRecompose: PartialFunction[Measure, Measure] =
      {
        case ProductMeasure(md, mr) => deflateProduct(ProductMeasure(deflate(md), deflate(mr)))
        case ExponentialMeasure(base, exponent) => ExponentialMeasure(deflate(base), exponent)
      }

      def giveUp: PartialFunction[Measure, Measure] =
      {
        case measure => measure
      }

      deflateNonRecompose.orElse(deflateRecompose).orElse(giveUp)(inflated)
    }

    private def exponentials(measure: Measure, outerExponent: Double, map: ExponentMap): ExponentMap = measure match
    {
      case ProductMeasure(md, mr) => exponentials(md, outerExponent, exponentials(mr, outerExponent, map))
      case RatioMeasure(nr, dr) => exponentials(nr, outerExponent, exponentials(dr, -outerExponent, map))
      case ExponentialMeasure(base, exponent) => exponentials(base, exponent * outerExponent, map)
      case _ => map + (measure -> (map.getOrElse(measure, 0.0) + measure.exponent * outerExponent))
    }

    private def exponential(measure: (Measure, Double)): Measure = measure match
    {
      case (x, 1.0) => x
      case _ => ExponentialMeasure(measure._1, measure._2)
    }
  }

}
