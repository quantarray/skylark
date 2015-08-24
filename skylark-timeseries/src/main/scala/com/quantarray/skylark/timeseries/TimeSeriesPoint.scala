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

import org.joda.time.DateTime

/**
 * Time series point.
 *
 * @author Araik Grigoryan
 */
trait TimeSeriesPoint[V]
{
  def observedTime: DateTime

  def versionTime: DateTime

  def observedValue: V

  override def toString = s"($observedTime, $observedValue) @ $versionTime"
}

object TimeSeriesPoint
{
  def apply[V](observedTime: DateTime, observedValue: V, versionTime: DateTime = DateTime.now()): TimeSeriesPoint[V] =
  {
    val otovvt = (observedTime, observedValue, versionTime)

    new TimeSeriesPoint[V]
    {
      val observedTime: DateTime = otovvt._1

      val observedValue: V = otovvt._2

      val versionTime: DateTime = otovvt._3
    }
  }
}
