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

import com.quantarray.skylark.autodiff.AutoDiff._
import org.scalatest.{FlatSpec, Matchers}

class MacroTreeAutoDiffSpec extends FlatSpec with Matchers
{
  "Constant" should "evaluate derivative" in
    {
      derivative((x: Double) => 4, -3) should equal(0)
    }

  "4 * x" should "evaluate derivative" in
    {
      derivative((x: Double) => 4 * x, -3) should equal(4)
    }

  "x^2" should "evaluate derivative" in
    {
      derivative((x: Double) => x * x, -3) should equal(-6)
    }

  "x * sin(x)" should "evaluate derivative" in
    {
      val point = -3.0
      derivative((x: Double) => x * math.sin(x), -3.0) should equal(point * math.cos(point) + math.sin(point))
    }

  "(x + y) * exp(x - y)" should "evaluate derivative" in
    {
      gradient2((x: Double, y: Double) => (x + y) * math.exp(x - y), 1.0, -2.0) should equal((0.0,40.171073846375336))
    }
}
