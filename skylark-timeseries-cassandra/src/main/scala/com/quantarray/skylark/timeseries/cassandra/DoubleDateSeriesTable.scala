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

package com.quantarray.skylark.timeseries.cassandra

import com.datastax.driver.core.{ResultSet, Row, Session}
import com.quantarray.skylark.timeseries.{OfficialSet, TimeSeriesPoint, TimeSeriesSet}
import com.websudos.phantom.Implicits._

import scala.concurrent.Future

/**
 * Double time-series table.
 *
 * @author Araik Grigoryan
 */
abstract class DoubleDateSeriesTable[P <: TimeSeriesPoint[Double]] extends TimeSeriesTable[Double, P, DoubleDateSeriesTable[P]]
{
  override def tableName = "\"DoubleDateSeries\""

  object observedValue extends DoubleColumn(this)
  {
    override lazy val name = "observed_value"
  }

}

object DoubleDateSeriesTable extends DoubleDateSeriesTable[TimeSeriesRow[Double]]
{
  def set(setId: Int): TimeSeriesSet = OfficialSet

  override def fromRow(row: Row): TimeSeriesRow[Double] =
  {
    TimeSeriesRow(entityKey(row), set(setId(row)), observedTime(row).toDateTime(dateSeriesDefaultTimeZone), observedValue(row), versionTime(row))
  }

  def read(entityKey: String, set: TimeSeriesSet, observedYear: Int)(implicit session: Session): Future[Seq[TimeSeriesPoint[Double]]] =
  {
    select.where(_.entityKey eqs entityKey)
      .and(_.setId eqs set.id)
      .and(_.observedYear eqs observedYear)
      .fetch()
  }

  def write(entityKey: String, set: TimeSeriesSet, points: Seq[TimeSeriesPoint[Double]])(implicit session: Session): Seq[Future[ResultSet]] =
  {
    points.map(point =>
      insert.value(_.entityKey, entityKey)
        .value(_.setId, set.id)
        .value(_.observedYear, point.observedTime.getYear)
        .value(_.observedTime, point.observedTime)
        .value(_.versionTime, point.versionTime)
        .value(_.observedValue, point.observedValue)
        .future()
    )
  }
}
