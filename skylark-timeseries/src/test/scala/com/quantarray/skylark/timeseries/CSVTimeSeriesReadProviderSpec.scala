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

import java.nio.file.Paths

import com.quantarray.skylark.time.DateTimeQuote._
import com.quantarray.skylark.time.DateTimeSupport.Implicits.RichDateTime
import com.quantarray.skylark.timeseries.CSVTimeSerial.CSVStringReaderTimeSerial
import org.joda.time.DateTime
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Success

class CSVTimeSeriesReadProviderSpec extends FlatSpec with Matchers
{

  implicit object CSVStringReaderIsTimeSerial extends CSVStringReaderTimeSerial
  {
    override def fromSetName(setName: String): TimeSeriesSet = OfficialSet
  }

  "CSVTimeSeriesReadProvider" should "read time series" in
    {
      val provider = new CSVTimeSeriesReadProvider(Paths.get("data", "futures", "CL_2015Z.price-OfficialSet.csv"))

      val seriesFuture = provider.read.history("CL_2015Z.price", d"2015-01-01" until d"2015-10-30", OfficialSet, DateTime.now)

      Await.ready(seriesFuture, Duration.Inf)

      seriesFuture.value.get match
      {
        case Success(series) => series.points.size should be(1)
        case _ => fail("Could not read series.")
      }
    }
}
