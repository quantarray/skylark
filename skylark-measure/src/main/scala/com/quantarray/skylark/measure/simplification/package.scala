package com.quantarray.skylark.measure

package object simplification
{
  type EnergyPriceTimesCurrencyPriceMeasure = ProductMeasure[EnergyPrice, CurrencyPriceMeasure]

  object Implicits
  {
    implicit object EnergyPriceTimesCurrencyPriceCanSimplify extends CanSimplify[EnergyPriceTimesCurrencyPriceMeasure, EnergyPrice]
    {
      override def simplify(inflated: EnergyPriceTimesCurrencyPriceMeasure): Option[EnergyPrice] =
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
}
