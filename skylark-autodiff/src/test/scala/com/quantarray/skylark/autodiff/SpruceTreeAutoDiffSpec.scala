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

package com.quantarray.skylark.autodiff

import org.scalatest.{FlatSpec, Matchers}

class SpruceTreeAutoDiffSpec extends FlatSpec with Matchers with SpruceTreeCompilation
{

  import SpruceTreeCompilation.Implicits._

  val tolerance = 0.0000000000001

  "Constant" should "evaluate to itself and compute gradient of zero" in
  {
    val f = Constant(4)

    val cf = f.compile()

    cf() should equal(4)
    cf.derivative() should equal(0.0)
  }

  "4 * x" should "evaluate function and gradient" in
    {
      val x = Val('x)

      val f = Constant(4.0) * x

      val point = -3.0

      val cf = f.compile(x)

      cf(point) should equal(-12.0 +- tolerance)
      cf.derivative(point) should equal(4)
    }

  "x^2" should "evaluate function and gradient" in
    {
      val x = Val('x)

      val f = x * x

      val point = -3.0

      val cf = f.compile(x)

      cf(point) should equal(9.0 +- tolerance)
      cf.derivative(point) should equal(-6.0)
    }

  "(x + y) * exp(x - y)" should "evaluate function and gradient" in
    {
      val x = Val('x)
      val y = Val('y)

      val f = (x + y) * Exp(x - y)

      val vals = (x, y)
      val point = (1.0, -2.0)

      val cf = f.compile(vals)

      cf(point) should equal(-20.085536923187668 +- tolerance)
      cf.gradient(point) should equal((0, 40.171073846375336))

      f(vals)(point) should equal(-20.085536923187668 +- tolerance)
    }


}