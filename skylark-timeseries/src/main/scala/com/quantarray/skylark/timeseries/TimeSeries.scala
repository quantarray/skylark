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

package com.quantarray.skylark.timeseries

/**
 * Time series.
 *
 * @author Araik Grigoryan
 */
trait TimeSeries[V]
{
  type P <: TimeSeriesPoint[V]

  def entityKey: String

  def set: TimeSeriesSet

  def points: Seq[P]

  def isEmpty: Boolean = points.isEmpty

  def nonEmpty: Boolean = points.nonEmpty

  def isSingular: Boolean = points.size == 1

  def nonSingular: Boolean = !isSingular

  override def toString = s"($entityKey, $set) -> $points"
}

object TimeSeries
{
  def empty[V](entityKey: String, set: TimeSeriesSet) =
  {
    val eks = (entityKey, set)

    new TimeSeries[V]
    {
      type P = TimeSeriesPoint[V]

      val entityKey: String = eks._1

      val set: TimeSeriesSet = eks._2

      val points: Seq[P] = Seq.empty
    }
  }
}

