package com.quantarray.skylark.learning

import scala.collection.immutable.SortedMap

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

}
