/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2016 Quantarray, LLC
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

package com.quantarray.skylark.measure

import com.quantarray.skylark.measure.duration.default._
import com.quantarray.skylark.measure.measures._
import com.quantarray.skylark.measure.quantities._
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration.Duration
import scala.language.postfixOps

/**
  * Duration spec.
  *
  * @author Araik Grigoryan
  */
class DurationSpec extends FlatSpec with Matchers
{
  def seconds(duration: Duration): Long = duration.toSeconds

  def nanos(duration: Duration): Long = duration.toNanos

  def dimension(quantity: Quantity[Double, TimeMeasure]): quantity.measure.D = quantity.measure.dimension

  "Quantity of TimeMeasure" should "be convertible to Duration" in
    {
      val secondsInDay = 24 * 60 * 60

      seconds(1 seconds) should equal(1)
      seconds(1 minutes) should equal(60)
      seconds(1 hours) should equal(60 * 60)
      seconds(1 day) should equal(secondsInDay)
      seconds(1 year) should equal(365 * secondsInDay)
      seconds(1 year360) should equal(360 * secondsInDay)
      seconds(1 fortnight) should equal(14 * secondsInDay)

      intercept[MatchError]
        {
          seconds(Quantity(1.0, TimeMeasure("smacks", US)))
        }

      nanos(1.4 seconds) should equal(1400000000)
      nanos(1.4 milliseconds) should equal(1400000)
      nanos(1.4 microseconds) should equal(1400)
      nanos(1.4 nanoseconds) should equal(1)
    }

  "Duration" should "be convertible to Quantity of TimeMeasure" in
    {
      import scala.concurrent.duration._

      dimension(Duration(1, NANOSECONDS)) should be(Time)
      dimension(Duration(1, MICROSECONDS)) should be(Time)
      dimension(Duration(1, MILLISECONDS)) should be(Time)
      dimension(Duration(1, SECONDS)) should be(Time)
      dimension(Duration(1, MINUTES)) should be(Time)
      dimension(Duration(1, HOURS)) should be(Time)
      dimension(Duration(1, DAYS)) should be(Time)
    }
}

