package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure
import com.quantarray.skylark.measure.QuasiNumeric

/*
 * Skylark
 *
 * © 2012-2016, Quantarray, LLC
 * http://skylark.io
 */
package object quantity
{
  trait Units[@specialized(Double, Int) N] extends Any
  {
    implicit def qn: QuasiNumeric[N]

    def value: N

    /**
      * Composes a quantity of supplied measure.
      */
    def apply(measure: Measure) = Quantity(value, measure)

    /**
      * Composes a quantity of supplied measure.
      */
    def *(measure: Measure): Quantity[N] = apply(measure)

    /**
      * Dimensionless.
      */
    def unit = apply(measure.Unit)

    def units = unit

    def percent = apply(measure.percent)

    def bp = apply(measure.bp)

    def rad = apply(measure.rad)

    def sr = apply(measure.sr)

    /**
      * Time.
      */
    def day = apply(measure.day)

    def days = day

    /**
      * Mass.
      */
    def g = apply(measure.g)

    def kg = apply(measure.kg)

    def cg = apply(measure.cg)

    def mg = apply(measure.mg)

    def t = apply(measure.t)

    def oz_metric = apply(measure.oz_metric)

    def oz = apply(measure.oz)

    def lb = apply(measure.lb)

    def mt = apply(measure.mt)

    def ton = apply(measure.ton)

    def gr = apply(measure.gr)

    def dwt = apply(measure.dwt)

    def lb_troy = apply(measure.lb_troy)

    def oz_troy = apply(measure.oz_troy)

    /**
      * Length.
      */
    def m = apply(measure.m)

    def in = apply(measure.in)

    def ft = apply(measure.ft)

    def yd = apply(measure.yd)

    /**
      * Area.
      */
    def km2 = apply(measure.km2)

    def ha = apply(measure.ha)

    /**
      * Volume.
      */
    def in3 = apply(measure.in3)

    def gal = apply(measure.gal)

    def bbl = apply(measure.bbl)

    /**
      * Energy.
      */
    def MMBtu = apply(measure.MMBtu)

    /**
      * Currency.
      */
    def USD = apply(measure.USD)
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
