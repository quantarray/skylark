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

import java.io.{InputStreamReader, FileInputStream, File, Reader}
import java.nio.file.Path

import com.github.tototoshi.csv.CSVReader
import org.joda.time.{DateTime, Interval}

import scala.concurrent.Future

/**
 * CSV time series read provider.
 *
 * @author Araik Grigoryan
 */
class CSVTimeSeriesReadProvider(filePath: Path) extends TimeSeriesProvider
{
  type RS = Reader

  type WS = Nothing

  private lazy val file = new File(filePath.toString)

  private lazy val inputStream = new FileInputStream(file)

  private implicit lazy val reader = new InputStreamReader(inputStream, CSVReader.DEFAULT_ENCODING)

  case class CSVTimeSeriesReader() extends TimeSeriesReader
  {
    /**
     * Reads history of time series.
     */
    override def history[V](entityKey: String, observedInterval: Interval, set: TimeSeriesSet, asOfVersionTime: DateTime)
                           (implicit timeSerial: TimeSerial[V, Reader, _]): Future[TimeSeries[V]] =
    {
      timeSerial.read.series(entityKey, observedInterval, set, asOfVersionTime)
    }

    override def close(): Unit =
    {
      reader.close()
      inputStream.close()
    }
  }

  lazy val read: TimeSeriesReader = CSVTimeSeriesReader()

  lazy val write: TimeSeriesWriter = ???

  override def close(): Unit = ???
}
