/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2016 Quantarray, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quantarray.skylark.measure

import org.apache.commons.math3.util.CombinatoricsUtils
import com.quantarray.skylark.measure.any.measures._
import com.quantarray.skylark.measure.any.quantities._
import org.scalatest.{FlatSpec, Matchers}

import scala.language.postfixOps

/**
  * Taylor series spec.
  *
  * @author Araik Grigoryan
  */
class TaylorSeriesSpec extends FlatSpec with Matchers
{
  case class TaylorSeries1(f: Double => Double) extends ((Double) => Double)
  {
    override def apply(x: Double): Double = f(x)

    def estimate(partials: Seq[Double => Double])(x: Double)(dx: Double): Double =
    {
      partials.zipWithIndex.map(pi => pi._1(x) * math.pow(dx, pi._2) / CombinatoricsUtils.factorial(pi._2)).sum
    }
  }

  case class QuantifiedTaylorSeries1(f: AnyQuantity[Double] => AnyQuantity[Double]) extends ((AnyQuantity[Double]) => AnyQuantity[Double])
  {
    override def apply(x: AnyQuantity[Double]): AnyQuantity[Double] = f(x)

    def estimate(partials: Seq[AnyQuantity[Double] => AnyQuantity[Double]])(x: AnyQuantity[Double])(dx: AnyQuantity[Double]): AnyQuantity[Double] =
    {
      import com.quantarray.skylark.measure.any.arithmetic.unsafe._
      import com.quantarray.skylark.measure.any.conversion.default._
      import com.quantarray.skylark.measure.any.simplification.default._

      partials.zipWithIndex.map(pi => pi._1(x) * (dx ^ pi._2) / CombinatoricsUtils.factorial(pi._2)).reduce(_.simplify + _.simplify)
    }
  }

  "TaylorSeries1" should "estimate e^x" in
    {
      val ts = TaylorSeries1(math.exp)

      // Derivative of e^x is e^x
      val partials = (1 to 7).map(_ => ts)
      val estimate = ts.estimate(partials)(2.0)(0.01)
      val actual = ts(2.01)

      math.abs(estimate - actual) should be < 1e-14
    }

  it should "estimate sin(x)" in
    {
      val ts = TaylorSeries1(math.sin)

      val partials = Seq[Double => Double](ts, math.cos, x => -math.sin(x))
      val estimate = ts.estimate(partials)(2.0)(0.01)
      val actual = ts(2.01)

      math.abs(estimate - actual) should be < 1e-7
    }

  "QuantifiedTaylorSeries1" should "estimate e^x" in
    {
      import com.quantarray.skylark.measure.any.implicits._

      val ts = QuantifiedTaylorSeries1(x => AnyQuantity(math.exp(x.value), USD))

      // Derivative of e^x is e^x
      // The function is a special value function, measured in USD
      // The function input is price of oil, measured in USD / bbl
      // Thus the first derivative (i.e. change in function per change in input) is measured in USD / (USD / bbl) = bbl
      // And the second derivative (i.e. change in first derivative per change in input) is measured in bbl / (USD / bbl) = bbl ^ 2 / USD
      val partials = Seq[AnyQuantity[Double] => AnyQuantity[Double]](ts, x => AnyQuantity(math.exp(x.value), bbl),
        x => AnyQuantity(math.exp(x.value), (bbl ^ 2) / USD))
      val estimate = ts.estimate(partials)(2.0 (USD / bbl))(0.01 (USD / bbl))
      val actual = ts(2.01 (USD / bbl))

      estimate.measure should be(USD)
      math.abs((estimate - actual).get.value) should be < 1e-5
    }
}

