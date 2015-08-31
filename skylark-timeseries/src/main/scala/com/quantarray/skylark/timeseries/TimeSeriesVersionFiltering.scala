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

import com.quantarray.skylark.time.DateTimeSupport.Implicits._
import org.joda.time.DateTime

/**
 *
 *
 * @author Araik Grigoryan
 */
trait TimeSeriesVersionFiltering
{
  def versioned[V](entityKey: String, set: TimeSeriesSet, points: Seq[TimeSeriesPoint[V]], asOfVersionTime: DateTime): Seq[TimeSeriesPoint[V]] =
  {
    val groupedPoints = points.groupBy(point => (entityKey, set, point.observedTime))

    val groupedSortedPoints = groupedPoints.map(groupedPoint =>
      (groupedPoint._1, groupedPoint._2.sortWith((one, two) => one.versionTime < two.versionTime).reverse))

    val foundVersionedPoints = groupedSortedPoints.map(groupedSortedPoint =>
      (groupedSortedPoint._1, groupedSortedPoint._2.find(_.versionTime <= asOfVersionTime)))

    val foundPointsForVersion = foundVersionedPoints.foldLeft(List.empty[TimeSeriesPoint[V]])((foundPointsForVersionSoFar, foundVersionedPoint) =>
      foundVersionedPoint._2 match
      {
        case Some(foundPointForVersion) => foundPointForVersion :: foundPointsForVersionSoFar
        case _ => foundPointsForVersionSoFar
      })

    foundPointsForVersion
  }
}
