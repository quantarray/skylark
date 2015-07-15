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
  type LayerMap[T] = Map[CellIndex, Seq[T]]

  object LayerMap
  {
    def empty[T] = SortedMap.empty[CellIndex, Seq[T]]
  }

  // Map of Layer's index to a LayerMap
  type NetMap[T] = Map[LayerIndex, LayerMap[T]]

  object NetMap
  {
    def empty[T] = SortedMap.empty[LayerIndex, LayerMap[T]]
  }

}
