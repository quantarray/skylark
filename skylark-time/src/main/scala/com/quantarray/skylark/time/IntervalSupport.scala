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

package com.quantarray.skylark.time

import com.quantarray.skylark.time.DateTimeSupport.Implicits.RichDateTime
import org.joda.time.{ReadablePeriod, Interval, ReadableInstant}

/**
 * Joda Interval support.
 *
 * @author Araik Grigoryan
 */
object IntervalSupport
{

  object Implicits
  {

    implicit final class RichInterval(private val interval: Interval) extends AnyVal
    {
      def years: Seq[Int] = interval.getStart.getYear to interval.getEnd.getYear

      def containsInclusive(instant: ReadableInstant): Boolean = interval.contains(instant) || interval.getEnd.isEqual(instant)

      def by(step: ReadablePeriod) = interval.getStart.by(step).takeWhile(_.isBefore(interval.getEnd))
    }

  }

}
