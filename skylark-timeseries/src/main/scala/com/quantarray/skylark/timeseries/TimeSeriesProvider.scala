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

import org.joda.time.{DateTime, Interval}

import scala.concurrent.Future

/**
 * Time series provider.
 *
 * @author Araik Grigoryan
 */
trait TimeSeriesProvider
{
  // Read session type: any time that is "closeable" for a given TimeSeriesReader; could be Any or Nothing.
  type RS

  // Write session type: any time that "closeable" for a given TimeSeriesWriter; could be Any or Nothing.
  type WS

  trait TimeSeriesReader
  {
    /**
     * Reads history of time series.
     */
    def history[V](entityKey: String, observedInterval: Interval, set: TimeSeriesSet, asOfVersionTime: DateTime)
                  (implicit timeSerial: TimeSerial[V, RS, _]): Future[AnyTimeSeries[V]]

    /**
     * Reads history of time series.
     */
    def history[V](entityKeys: Seq[String], observedInterval: Interval, set: TimeSeriesSet, asOfVersionTime: DateTime)
                  (implicit timeSerial: TimeSerial[V, RS, _]): Future[Seq[AnyTimeSeries[V]]] =
    {
      import scala.concurrent.ExecutionContext.Implicits.global

      Future.sequence(entityKeys.map(history(_, observedInterval, set, asOfVersionTime)))
    }

    def close(): Unit
  }

  trait TimeSeriesWriter
  {

    trait WriteResult
    {
      def versionTime: DateTime
    }

    type WR <: WriteResult

    /**
     * Writes an immutable, non-destructible versioned copy of the series.
     */
    def latest[V](series: AnyTimeSeries[V])(implicit timeSerial: TimeSerial[V, _, WS]): Future[WR]

    def close(): Unit
  }

  def read: TimeSeriesReader

  def write: TimeSeriesWriter

  def close(): Unit =
  {
    read.close()
    write.close()
  }

}
