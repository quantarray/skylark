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
import breeze.linalg.support.LiteralRow
import breeze.storage.Zero

import scala.reflect.ClassTag

/**
 * Breeze matrix ops.
 *
 * @author Araik Grigoryan
 */
trait BreezeMatrixOps
{
  type Matrix = DenseMatrix[Double]

  object Matrix
  {
    def apply[@specialized R, @specialized(Double, Int, Float, Long) V](rows: Seq[R])(implicit rl: LiteralRow[R, V], man: ClassTag[V], zero: Zero[V]) =
    {
      DenseMatrix[R, V](rows: _*)
    }
  }

  /**
   * Constructs matrix representation of biases and weights.
   */
  def matrices(bsws: (Biases, Weights)): (Seq[Matrix], Seq[Matrix]) =
  {
    val biases = bsws._1
    val weights = bsws._2

    val bs = weights.keys.map(layerIndex =>
    {
      // n by 1 vector, where m is the number of inputs for to layer identified by the layerIndex + 1
      Matrix(biases(layerIndex + 1).values.toSeq)
    })

    val ws = weights.keys.map(layerIndex =>
    {
      // m by n matrix, where m is number of inputs to the layer identified by the layerIndex
      // n is the number inputs to the layer identified by layerIndex + 1
      Matrix(weights(layerIndex).values.toSeq)
    })

    (bs.toSeq, ws.toSeq)
  }

  // TODO: Refactor to use DenseMatrix.zeros
  def zeros(bsws: (Biases, Weights)): (Seq[Matrix], Seq[Matrix]) =
  {
    val biases = bsws._1
    val weights = bsws._2

    val bs = weights.keys.map(layerIndex =>
    {
      // n by 1 vector, where m is the number of inputs for to layer identified by the layerIndex + 1
      Matrix(biases(layerIndex + 1).values.map(_.map(_ => 0.0)).toSeq)
    })

    val ws = weights.keys.map(layerIndex =>
    {
      // m by n matrix, where m is number of inputs to the layer identified by the layerIndex
      // n is the number inputs to the layer identified by layerIndex + 1
      Matrix(weights(layerIndex).values.map(_.map(_ => 0.0)).toSeq)
    })

    (bs.toSeq, ws.toSeq)
  }
}
