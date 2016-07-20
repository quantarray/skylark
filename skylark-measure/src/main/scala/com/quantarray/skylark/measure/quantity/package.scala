package com.quantarray.skylark.measure

import com.quantarray.skylark.measure

package object quantity
{

  trait Units[N] extends Any
  {
    implicit def qn: QuasiNumeric[N]

    def toValue: N

    /**
      * Dimensionless.
      */
    def unit = Quantity(toValue, measure.Unit)

    def units = unit

    def percent = Quantity(toValue, measure.percent)

    def bp = Quantity(toValue, measure.bp)

    def rad = Quantity(toValue, measure.rad)

    def sr = Quantity(toValue, measure.sr)

    /**
      * Time.
      */
    def day = Quantity(toValue, measure.day)

    def days = day

    /**
      * Mass.
      */
    def g = Quantity(toValue, measure.g)

    def kg = Quantity(toValue, measure.kg)

    def cg = Quantity(toValue, measure.cg)

    def mg = Quantity(toValue, measure.mg)

    def t = Quantity(toValue, measure.t)

    def oz_metric = Quantity(toValue, measure.oz_metric)

    def oz = Quantity(toValue, measure.oz)

    def lb = Quantity(toValue, measure.lb)

    def mt = Quantity(toValue, measure.mt)

    def ton = Quantity(toValue, measure.ton)

    def gr = Quantity(toValue, measure.gr)

    def dwt = Quantity(toValue, measure.dwt)

    def lb_troy = Quantity(toValue, measure.lb_troy)

    def oz_troy = Quantity(toValue, measure.oz_troy)

    /**
      * Length.
      */
    def m = Quantity(toValue, measure.m)

    def in = Quantity(toValue, measure.in)

    def ft = Quantity(toValue, measure.ft)

    def yd = Quantity(toValue, measure.yd)

    /**
      * Area.
      */
    def km2 = Quantity(toValue, measure.km2)

    def ha = Quantity(toValue, measure.ha)

    /**
      * Volume.
      */
    def in3 = Quantity(toValue, measure.in3)

    def gal = Quantity(toValue, measure.gal)

    def bbl = Quantity(toValue, measure.bbl)

    /**
      * Energy.
      */
    def MMBtu = Quantity(toValue, measure.MMBtu)

    /**
      * Currency.
      */
    def USD = Quantity(toValue, measure.USD)
  }


  implicit final class DoubleQuantity(private val value: Double) extends AnyVal with Units[Double]
  {
    implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.DoubleQuasiNumeric)

    override def toValue: Double = value

    def *[M <: Measure[M]](measure: M): Quantity[Double, M] = Quantity(toValue, measure)

    def apply[M <: Measure[M]](measure: M): Quantity[Double, M] = Quantity(toValue, measure)
  }

  implicit final class IntQuantity(private val value: Int) extends AnyVal with Units[Double]
  {
    implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.DoubleQuasiNumeric)

    override def toValue: Double = value

    def *[M <: Measure[M]](measure: M): Quantity[Double, M] = Quantity(toValue, measure)

    def apply[M <: Measure[M]](measure: M): Quantity[Double, M] = Quantity(toValue, measure)
  }

}
