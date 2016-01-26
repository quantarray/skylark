package com.quantarray.skylark.measure

package object reduction
{
  type EnergyPriceTimesCurrencyPriceMeasure = ProductMeasure[EnergyPrice, CurrencyPriceMeasure]

  implicit object EnergyPriceTimesCurrencyPriceCanReduce
    extends CanReduce[EnergyPriceTimesCurrencyPriceMeasure, Either[EnergyPriceTimesCurrencyPriceMeasure, EnergyPrice]]
  {
    override def reduce(inflated: EnergyPriceTimesCurrencyPriceMeasure): Either[EnergyPriceTimesCurrencyPriceMeasure, EnergyPrice] =
    {
      if (inflated.multiplicand.numerator == inflated.multiplier.denominator)
      {
        Right(RatioMeasure(inflated.multiplier.numerator, inflated.multiplicand.denominator))
      }
      else
      {
        Left(inflated)
      }
    }
  }
}
