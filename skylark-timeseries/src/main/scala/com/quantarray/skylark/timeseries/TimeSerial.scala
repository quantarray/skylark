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
 * Time-serial type class.
 *
 * @author Araik Grigoryan
 */
trait TimeSerial[V, RS, WS]
{

  trait TimeSerialReader
  {
    def series(entityKey: String, observedInterval: Interval, set: TimeSeriesSet, asOfVersionTime: DateTime)
              (implicit session: RS): Future[TimeSeries[V]]
  }

  trait TimeSerialWriter
  {

    trait WriteResult
    {
      def versionTime: DateTime
    }

    def series(series: TimeSeries[V])(implicit session: WS): Future[WriteResult]
  }

  def read: TimeSerialReader

  def write: TimeSerialWriter
}

