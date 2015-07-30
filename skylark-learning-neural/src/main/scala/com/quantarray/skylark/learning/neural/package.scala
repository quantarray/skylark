package com.quantarray.skylark.learning

import breeze.linalg.DenseMatrix
import breeze.linalg.support.LiteralRow
import breeze.storage.Zero

import scala.collection.immutable.SortedMap
import scala.reflect.ClassTag

/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */
package object neural
{
  type CellIndex = Int

  type LayerIndex = Int

  // Map of Cell's index to a Seq of properties
  type LayerPropMap[T] = Map[CellIndex, Seq[T]]

  object LayerPropMap
  {
    def empty[T] = SortedMap.empty[CellIndex, Seq[T]]
  }

  // Map of Layer's index to a LayerMap
  type NetPropMap[T] = Map[LayerIndex, LayerPropMap[T]]

  object NetPropMap
  {
    def empty[T] = SortedMap.empty[LayerIndex, LayerPropMap[T]]
  }

  type Biases = NetPropMap[Double]

  type Weights = NetPropMap[Double]

  // Fitness function: determines if output Seq[Double] is fit with respect to expected sample
  type Fitness = (Seq[Double], SupervisedDataSample) => Boolean

  /**
   * Breeze types
   */

  type Matrix = DenseMatrix[Double]

  object Matrix
  {
    def apply[@specialized R, @specialized(Double, Int, Float, Long) V](rows: Seq[R])(implicit rl: LiteralRow[R, V], man: ClassTag[V], zero: Zero[V]) =
    {
      DenseMatrix[R, V](rows: _*)
    }

    def zeros(rows: Int, cols: Int) = DenseMatrix.zeros[Double](rows, cols)
  }

}
