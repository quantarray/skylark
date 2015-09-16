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

import com.quantarray.skylark.timeseries.{AnyTimeSeries, TimeSerial, TimeSeriesProvider, TimeSeriesSet}
import com.websudos.phantom.Implicits._
import org.joda.time.{DateTime, Interval}

import scala.concurrent.Future

/**
 * Cassandra time-series provider.
 *
 * @author Araik Grigoryan
 */
trait CassandraTimeSeriesProvider extends TimeSeriesProvider with CassandraDbConnectivity with CassandraCluster with CassandraKeySpace
{
  override type RS = Session

  override type WS = Session

  case class CassandraTimeSeriesReader() extends TimeSeriesReader
  {
    private val readOnlySession = db.readOnlyWithDelayedClose()

    private implicit val session = readOnlySession.session

    /**
     * Reads history of time series.
     */
    override def history[V](entityKey: String, observedInterval: Interval, set: TimeSeriesSet, asOfVersionTime: DateTime)
                           (implicit timeSerial: TimeSerial[V, Session, _]): Future[AnyTimeSeries[V]] =
    {
      timeSerial.read.series(entityKey, observedInterval, set, asOfVersionTime)
    }

    override def close(): Unit =
    {
      readOnlySession.close()
    }
  }

  val read: TimeSeriesReader = CassandraTimeSeriesReader()

  val write: TimeSeriesWriter = ???
}
