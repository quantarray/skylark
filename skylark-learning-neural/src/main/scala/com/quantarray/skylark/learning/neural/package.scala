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
  type NeuralLayerWeightMap = Map[Int, Seq[Double]]

  object NeuralLayerWeightMap
  {
    def empty = SortedMap.empty[Int, Seq[Double]]
  }

  type NeuralNetWeightMap = Map[Int, NeuralLayerWeightMap]

  object NeuralNetWeightMap
  {
    def empty = SortedMap.empty[Int, NeuralLayerWeightMap]
  }
}
