package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object simplification
{
  type EnergyPriceTimesFXMeasure = ProductMeasure[EnergyPrice, FX]

  trait DefaultSimplificationImplicits
  {

    implicit val energyPriceTimesCurrencyPriceCanSimplify = new CanSimplify[EnergyPriceTimesFXMeasure, Option[EnergyPrice]]
    {
      override def simplify(inflated: EnergyPriceTimesFXMeasure): Option[EnergyPrice] =
      {
        if (inflated.multiplicand.numerator == inflated.multiplier.denominator)
        {
          Some(RatioMeasure(inflated.multiplier.numerator, inflated.multiplicand.denominator))
        }
        else
        {
          None
        }
      }
    }

  }

  object default extends AnyRef with DefaultSimplificationImplicits
}
