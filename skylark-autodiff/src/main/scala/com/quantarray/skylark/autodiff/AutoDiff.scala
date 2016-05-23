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

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

/**
  * Automatic differentiator.
  *
  * @author Araik Grigoryan
  */
object AutoDiff extends MacroTreeCompilation
{
  // TODO: How to { val f = (x: Double) => x * x; derivative(f) }?

  def gradient1Impl(c: blackbox.Context)(function: c.Expr[Double => Double], point: c.Expr[Double]): c.Expr[Double] =
  {
    import c.universe._

    val compiler = new Compiler[c.Tree](c)

    val tape = compiler.compile(function.tree).tape

    val gradient = CompiledFunction1(tape).derivative(c.eval(point))

    c.Expr(q"$gradient")
  }

  def derivative(function: Double => Double, point: Double): Double = macro gradient1Impl

  def gradient2Impl(c: blackbox.Context)(function: c.Expr[(Double, Double) => Double], point1: c.Expr[Double],
                                         point2: c.Expr[Double]): c.Expr[(Double, Double)] =
  {
    import c.universe._

    val gradient = CompiledFunction2[c.Tree](c, function.tree).gradient(c.eval(point1), c.eval(point2))

    c.Expr(q"$gradient")
  }

  def gradient2(function: (Double, Double) => Double, point1: Double, point2: Double): (Double, Double) = macro gradient2Impl

}
