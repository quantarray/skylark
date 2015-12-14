package com.quantarray.skylark.measure

package object reduction
{
  type EnergyPriceTimesCurrencyPriceMeasure = ProductMeasure[EnergyPriceMeasure, CurrencyPriceMeasure]

  implicit object EnergyPriceTimesCurrencyPriceCanReduce
    extends CanReduce[EnergyPriceTimesCurrencyPriceMeasure, Either[EnergyPriceTimesCurrencyPriceMeasure, EnergyPriceMeasure]]
  {
    override def reduce(inflated: EnergyPriceTimesCurrencyPriceMeasure): Either[EnergyPriceTimesCurrencyPriceMeasure, EnergyPriceMeasure] =
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
