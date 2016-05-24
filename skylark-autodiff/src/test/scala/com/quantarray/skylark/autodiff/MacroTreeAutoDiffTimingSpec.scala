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

class MacroTreeAutoDiffTimingSpec extends FlatSpec with Matchers
{
  private def time[R](block: => R): (R, Long) =
  {
    val t0 = System.nanoTime()
    val result = block
    val t1 = System.nanoTime()
    val elapsedTime = t1 - t0
    println(s"Elapsed time: $elapsedTime ns")
    (result, elapsedTime)
  }

  @inline
  private def f(x: Double): Double = x * math.sin(x)

  val point = -3.0

  val delta = 0.00001

  @inline
  private def autoDiffTest(index: Int): Unit =
  {
    val d = derivative((x: Double) => x * math.sin(x), -3.0)
    if (index == 1)
      println(s"AD: $d")
  }

  @inline
  private def finiteDifferenceTest(index: Int): Unit =
  {
    val d = (f(point + 0.00001) - f(point)) / delta
    if (index == 1)
      println(s"FD: $d")
  }

  "Timing" should "display results and relative times of AD and FD methods" in
    {
      val numberOfIterations = 10000000

      val (_, adTime) = time
      {
        1 to numberOfIterations foreach autoDiffTest
      }

      val (_, fdTime) = time
      {
        1 to numberOfIterations foreach finiteDifferenceTest
      }

      val ratio = fdTime / adTime
      println(s"FD / AD ratio: $ratio")
    }
}
