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

import com.datastax.driver.core.Session
import com.quantarray.skylark.timeseries.{TimeSeriesPoint, TimeSeriesSet, TimeSeriesVersionFiltering}
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 *
 *
 * @author Araik Grigoryan
 */
trait CassandraTimeSeriesKeySpace extends CassandraKeySpace with TimeSeriesVersionFiltering
{
  def readDouble(entityKey: String, set: TimeSeriesSet, observedYear: Int, asOfVersionTime: DateTime)
                (implicit session: Session): Future[Seq[TimeSeriesPoint[Double]]] =
  {
    DoubleDateSeriesTable.read(entityKey, set, observedYear).map(versioned(entityKey, set, _, asOfVersionTime))
  }

  //  def readString(entityKey: String, set: TimeSeriesSet, observedYear: Int, asOfVersionTime: DateTime)
  //                (implicit session: Session): Future[Seq[TimeSeriesPoint[String]]] =
  //  {
  //    StringDateSeriesTable.read(entityKey, set, observedYear).map(versioned(_, asOfVersionTime))
  //  }
  //
  //  def readTimestamp(entityKey: String, set: TimeSeriesSet, observedYear: Int, asOfVersionTime: DateTime)
  //                   (implicit session: Session): Future[Seq[TimeSeriesPoint[DateTime]]] =
  //  {
  //    TimestampDateSeriesTable.read(entityKey, set, observedYear).map(versioned(_, asOfVersionTime))
  //  }
  //
  //  def writeDouble(points: Seq[TimeSeriesPoint[Double]])(implicit session: Session): Future[Seq[ResultSet]] =
  //  {
  //    Future.sequence(DoubleDateSeriesTable.write(points))
  //  }
  //
  //  def writeString(points: Seq[TimeSeriesPoint[String]])(implicit session: Session): Future[Seq[ResultSet]] =
  //  {
  //    Future.sequence(StringDateSeriesTable.write(points))
  //  }
  //
  //  def writeTimestamp(points: Seq[TimeSeriesPoint[DateTime]])(implicit session: Session): Future[Seq[ResultSet]] =
  //  {
  //    Future.sequence(TimestampDateSeriesTable.write(points))
  //  }
}
