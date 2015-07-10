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
  type NeuralCellIndex = Int

  type NeuralLayerIndex = Int

  // Map of NeuralCell's index to a Seq of properties
  type NeuralLayerMap[T] = Map[NeuralCellIndex, Seq[T]]

  object NeuralLayerMap
  {
    def empty[T] = SortedMap.empty[NeuralCellIndex, Seq[T]]
  }

  // Map of NeuralLayer's index to a NeuralLayerMap
  type NeuralNetMap[T] = Map[NeuralLayerIndex, NeuralLayerMap[T]]

  object NeuralNetMap
  {
    def empty[T] = SortedMap.empty[NeuralLayerIndex, NeuralLayerMap[T]]
  }
}
