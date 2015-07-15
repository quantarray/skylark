/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2015 Quantarray, LLC
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

package com.quantarray.skylark.learning.neural

import breeze.linalg.DenseMatrix

/**
 * Objective function, a.k.a. cost function.
 *
 * @author Araik Grigoryan
 */
trait Objective extends ((DenseMatrix[Double], DenseMatrix[Double]) => Double)
{
  /**
   * Partial derivative of the objective function with respect to output activation x.
   */
  def d(z: DenseMatrix[Double], x: DenseMatrix[Double], y: DenseMatrix[Double]): DenseMatrix[Double]
}

case object QuadraticObjective extends Objective
{
  override def apply(x: DenseMatrix[Double], y: DenseMatrix[Double]): Double =
  {
    //    val n = norm(x - y) // FIXME: How to compute norm of a DenseMatrix?
    //    0.5 * n * n
    ???
  }

  override def d(z: DenseMatrix[Double], x: DenseMatrix[Double], y: DenseMatrix[Double]): DenseMatrix[Double] = x - y
}


