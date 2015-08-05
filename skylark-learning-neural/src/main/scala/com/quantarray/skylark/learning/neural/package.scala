package com.quantarray.skylark.learning

import breeze.linalg.support.LiteralRow
import breeze.linalg.{DenseMatrix, DenseVector}
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
  val Biases = NetPropMap

  type Weights = NetPropMap[Double]
  val Weights = NetPropMap

  // Fitness function: determines if output Seq[Double] is fit with respect to expected sample
  type Fitness = (Seq[Double], SupervisedDataSample) => Boolean

  /**
   * Breeze types
   */

  type Vector = DenseVector[Double]

  object Vector
  {
    def apply[V: ClassTag](values: Seq[V]) = DenseVector(values: _*)
  }

  type Matrix = DenseMatrix[Double]

  object Matrix
  {
    def apply[@specialized R, @specialized(Double, Int, Float, Long) V](rows: Seq[R])(implicit rl: LiteralRow[R, V], man: ClassTag[V], zero: Zero[V]) = DenseMatrix[R, V](rows: _*)

    def zeros(rows: Int, cols: Int) = DenseMatrix.zeros[Double](rows, cols)

    def eye(n: Int) = DenseMatrix.eye[Double](n)
  }

}
