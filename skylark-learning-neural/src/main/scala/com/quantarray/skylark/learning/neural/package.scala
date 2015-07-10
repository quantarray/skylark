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
  type NeuralLayerMap[T] = Map[Int, Seq[T]]

  object NeuralLayerMap
  {
    def empty[T] = SortedMap.empty[Int, Seq[T]]
  }

  type NeuralNetMap[T] = Map[Int, NeuralLayerMap[T]]

  object NeuralNetMap
  {
    def empty[T] = SortedMap.empty[Int, NeuralLayerMap[T]]
  }
}
