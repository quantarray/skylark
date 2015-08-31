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

import com.quantarray.skylark.time.IntervalSupport.Implicits.RichInterval
import com.quantarray.skylark.timeseries._
import com.websudos.phantom.Implicits._
import org.joda.time.{DateTime, Interval}

import scala.concurrent.Future

/**
 * Cassandra time serial type class.
 *
 * @author Araik Grigoryan
 */
object CassandraTimeSerial
{

  trait TimeSerialReading[V, S]
  {
    def readPoints(entityKey: String, set: TimeSeriesSet, observedYear: Int, asOfVersionTime: DateTime)
                  (implicit session: S): Future[Seq[TimeSeriesPoint[V]]]

    def series(entityKey: String, observedInterval: Interval, set: TimeSeriesSet, asOfVersionTime: DateTime)
              (implicit session: S): Future[AnyTimeSeries[V]] =
    {
      val futureTimeSeriesPoints = observedInterval.years.foldLeft(List.empty[Future[Seq[TimeSeriesPoint[V]]]])((futureTimeSeriesPoints, observedYear) =>
      {
        val futureYearPoints = readPoints(entityKey, set, observedYear, asOfVersionTime)

        val filteredFutureYearPoints = futureYearPoints.map(_.filter(point => observedInterval.containsInclusive(point.observedTime)))

        filteredFutureYearPoints :: futureTimeSeriesPoints
      })

      Future.sequence(futureTimeSeriesPoints).map(_.flatten).map(TimeSeries(entityKey, set, _))
    }
  }

  trait DoubleTimeSerial extends TimeSerial[Double, Session, Session] with TimeSerialReading[Double, Session] with CassandraTimeSeriesKeySpace
  {

    object DoubleIsTimeSerialReader extends TimeSerialReader with TimeSerialReading[Double, Session]
    {
      def readPoints(entityKey: String, set: TimeSeriesSet, observedYear: Int, asOfVersionTime: DateTime)
                    (implicit session: Session): Future[Seq[TimeSeriesPoint[Double]]] = readDouble(entityKey, set, observedYear, asOfVersionTime)
    }

    val read: TimeSerialReader = DoubleIsTimeSerialReader

    val write: TimeSerialWriter = ???
  }

}
