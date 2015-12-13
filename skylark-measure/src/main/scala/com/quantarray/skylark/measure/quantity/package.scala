package com.quantarray.skylark.measure

import com.quantarray.skylark.measure

package object quantity
{
  trait Units extends Any
  {
    def toDouble: Double

    /**
     * Unitless.
     */
    def percent = Quantity(toDouble, measure.percent)

    def bp = Quantity(toDouble, measure.bp)

    def rad = Quantity(toDouble, measure.rad)

    def sr = Quantity(toDouble, measure.sr)

    /**
     * Mass.
     */
    def g = Quantity(toDouble, measure.g)

    def kg = Quantity(toDouble, measure.kg)

    def cg = Quantity(toDouble, measure.cg)

    def mg = Quantity(toDouble, measure.mg)

    def t = Quantity(toDouble, measure.t)

    def oz_metric = Quantity(toDouble, measure.oz_metric)

    def oz = Quantity(toDouble, measure.oz)

    def lb = Quantity(toDouble, measure.lb)

    def ton = Quantity(toDouble, measure.ton)

    def gr = Quantity(toDouble, measure.gr)

    def dwt = Quantity(toDouble, measure.dwt)

    def lb_troy = Quantity(toDouble, measure.lb_troy)

    def oz_troy = Quantity(toDouble, measure.oz_troy)

    /**
     * Length.
     */
    def m = Quantity(toDouble, measure.m)

    /**
     * Aream.
     */
    def km2 = Quantity(toDouble, measure.km2)

    def ha = Quantity(toDouble, measure.ha)

    /**
     * Volume.
     */
    def in3 = Quantity(toDouble, measure.in3)

    def gal = Quantity(toDouble, measure.gal)

    def bbl = Quantity(toDouble, measure.bbl)

    /**
     * Energy.
     */
    def MMBtu = Quantity(toDouble, measure.MMBtu)

    /**
     * Currency.
     */
    def USD = Quantity(toDouble, measure.USD)
  }

  implicit final class DoubleQuantity(private val value: Double) extends AnyVal with Units
  {
    override def toDouble: Double = value
  }

}
