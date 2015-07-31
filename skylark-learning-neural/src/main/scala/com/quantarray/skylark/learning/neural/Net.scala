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

/**
 * Net.
 *
 * @author Araik Grigoryan
 */
trait Net
{
  type C <: Cell

  type L <: Layer

  type T <: Connection

  def activation: Activation

  def cost: Cost

  def connections: Seq[T]

  def biases: Biases

  def weights: Weights

  protected def props[P](groups: Map[L, Map[C, Seq[T]]], select: T => Boolean, prop: T => P): NetPropMap[P] =
  {
    groups.foldLeft(NetPropMap.empty[P])((m, x) =>
    {
      val lss = x._2.foldLeft(LayerPropMap.empty[P])((n, y) =>
      {
        val props = y._2.filter(select).map(prop)
        if (props.isEmpty) n else n + (y._1.index -> props)
      })
      if (lss.isEmpty) m else m + (x._1.index -> lss)
    })
  }
}
