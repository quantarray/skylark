package com.quantarray.skylark.measure

import com.quantarray.skylark.measure

package object quantity
{

  trait Units[N] extends Any
  {
    implicit def qn: QuasiNumeric[N]

    def value: N

    /**
      * Dimensionless.
      */
    def unit = Quantity(value, measure.Unit)

    def units = unit

    def percent = Quantity(value, measure.percent)

    def bp = Quantity(value, measure.bp)

    def rad = Quantity(value, measure.rad)

    def sr = Quantity(value, measure.sr)

    /**
      * Time.
      */
    def day = Quantity(value, measure.day)

    def days = day

    /**
      * Mass.
      */
    def g = Quantity(value, measure.g)

    def kg = Quantity(value, measure.kg)

    def cg = Quantity(value, measure.cg)

    def mg = Quantity(value, measure.mg)

    def t = Quantity(value, measure.t)

    def oz_metric = Quantity(value, measure.oz_metric)

    def oz = Quantity(value, measure.oz)

    def lb = Quantity(value, measure.lb)

    def mt = Quantity(value, measure.mt)

    def ton = Quantity(value, measure.ton)

    def gr = Quantity(value, measure.gr)

    def dwt = Quantity(value, measure.dwt)

    def lb_troy = Quantity(value, measure.lb_troy)

    def oz_troy = Quantity(value, measure.oz_troy)

    /**
      * Length.
      */
    def m = Quantity(value, measure.m)

    def in = Quantity(value, measure.in)

    def ft = Quantity(value, measure.ft)

    def yd = Quantity(value, measure.yd)

    /**
      * Area.
      */
    def km2 = Quantity(value, measure.km2)

    def ha = Quantity(value, measure.ha)

    /**
      * Volume.
      */
    def in3 = Quantity(value, measure.in3)

    def gal = Quantity(value, measure.gal)

    def bbl = Quantity(value, measure.bbl)

    /**
      * Energy.
      */
    def MMBtu = Quantity(value, measure.MMBtu)

    /**
      * Currency.
      */
    def USD = Quantity(value, measure.USD)

    /**
      * Composes a quantity of supplied measure.
      */
    def apply[M <: Measure[M]](measure: M): Quantity[N, M] = Quantity(value, measure)

    /**
      * Composes a quantity of supplied measure.
      */
    def *[M <: Measure[M]](measure: M): Quantity[N, M] = apply(measure)
  }

  implicit final class DoubleQuantity(val value: Double) extends AnyVal with Units[Double]
  {
    implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.DoubleQuasiNumeric)
  }

  implicit final class IntQuantity(private val intValue: Int) extends AnyVal with Units[Double]
  {
    implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.DoubleQuasiNumeric)

    override def value: Double = intValue
  }

}
