package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2017, Quantarray
 * http://skylark.io
 */
package object simplification
{
  type EnergyPriceTimesFXMeasure = ProductMeasure[EnergyPrice, FX]

  trait DefaultSimplificationImplicits
  {

    implicit val canSimplifyEnergyPriceTimesCurrencyPrice = new CanSimplifyMeasure[EnergyPriceTimesFXMeasure, Option[EnergyPrice]]
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

    implicit val canSimplifyEnergyPriceTimesCurrencyPriceQuantity = new CanSimplifyQuantity[Double, EnergyPriceTimesFXMeasure, Quantity, Option[EnergyPrice]]
    {
      type QR = Option[Quantity[Double, EnergyPrice]]

      override def simplify(inflated: EnergyPriceTimesFXMeasure): Option[EnergyPrice] = canSimplifyEnergyPriceTimesCurrencyPrice.simplify(inflated)

      override def simplifyQuantity(inflated: Quantity[Double, EnergyPriceTimesFXMeasure]): QR = simplify(inflated.measure).map(Quantity(inflated.value, _))
    }

  }

  object default extends AnyRef with DefaultSimplificationImplicits
}
