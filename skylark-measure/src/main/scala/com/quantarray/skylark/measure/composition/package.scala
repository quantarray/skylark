package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object composition
{

  implicit final class IntQuantity(private val value: Int) extends AnyVal
  {
    def *[M <: Measure[M]](measure: M): (Double, M) = (value, measure)
  }

  implicit final class DoubleQuantity(private val value: Double) extends AnyVal
  {
    def *[M <: Measure[M]](measure: M): (Double, M) = (value, measure)
  }

  implicit final class StringMeasureComposition(private val name: String) extends AnyVal
  {
    def :=[M <: Measure[M]](quantity: (Double, M)): M = quantity._2.composes(name, quantity._1)

    def :=[M <: Measure[M]](measure: M): M = measure.composes(name, 1.0)
  }
}
