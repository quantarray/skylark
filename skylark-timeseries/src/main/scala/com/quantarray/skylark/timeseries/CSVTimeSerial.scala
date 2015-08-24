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

import java.io.Reader

import com.github.tototoshi.csv.CSVReader
import com.quantarray.skylark.time.DateTimeSupport.Implicits._
import com.quantarray.skylark.time.IntervalSupport.Implicits._
import com.quantarray.skylark.time.StringDate._
import org.joda.time.{DateTime, Interval}

import scala.concurrent.Future

/**
 * CSV time-serial type class.
 *
 * @author Araik Grigoryan
 */
object CSVTimeSerial
{
  val EntityKeyColumnName = "Entity Key"

  val SetColumnName = "Set"

  val ObservedTimeColumnName = "Observed Time"

  val ObservedValueColumnName = "Observed Value"

  val VersionTimeColumnName = "Version Time"

  val observedTimeFormat: String = "yyyy-MM-dd"

  val versionTimeFormat: String = "yyyy-MM-dd'T'hh:mm:ssZ"

  object CSVStringReaderTimeSerial extends TimeSerial[String, Reader, Nothing]
  {

    object StringIsCSVTimeSerialReader extends TimeSerialReader //with CSVTimeSeriesSetResolution
    {

      import scala.concurrent.ExecutionContext.Implicits.global

      override def series(entityKey: String, observedInterval: Interval, set: TimeSeriesSet, asOfVersionTime: DateTime)
                         (implicit session: Reader): Future[TimeSeries[String]] =
      {
        val csvReader = CSVReader.open(session)

        val future: Future[TimeSeries[String]] =
          try
          {
            val lines = csvReader.allWithHeaders()

            if (lines.isEmpty)
            {
              Future(TimeSeries.empty(entityKey, set))
            }
            else
            {
              val points = lines.foldLeft(List.empty[TimeSeriesPoint[String]])((points, line) =>
              {
                // FIXME: if (line.contains(Set)) fromSetName(line(Set)) else set
                val resolvedSet = OfficialSet
                val observedTime = line(ObservedTimeColumnName).d(observedTimeFormat)
                val observedValue = line(ObservedValueColumnName)
                val versionTime = if (line.contains(VersionTimeColumnName)) line(VersionTimeColumnName).d(versionTimeFormat) else asOfVersionTime

                if (resolvedSet == set && observedInterval.containsInclusive(observedTime) && versionTime.isBeforeOrEquals(asOfVersionTime))
                  TimeSeriesPoint(observedTime, observedValue.toString, versionTime) :: points
                else
                  points
              })

              Future(TimeSeries(entityKey, set, points))
            }
          }
          finally
          {
            csvReader.close()
          }

        future
      }
    }

    override def read: TimeSerialReader = StringIsCSVTimeSerialReader

    override def write: TimeSerialWriter = ???
  }

}
