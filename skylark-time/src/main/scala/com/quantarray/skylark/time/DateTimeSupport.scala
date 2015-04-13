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

import org.joda.time._

/**
 * Joda DateTime support..
 *
 * @author Araik Grigoryan
 */
object DateTimeSupport
{

  object Implicits
  {
    // Required for TreeMap to know how to sort DateTime instances
    implicit val dateTimeOrdering = Ordering.by
    {
      dateTime: DateTime => dateTime.getMillis
    }

    implicit class RichDateTime(val dateTime: DateTime) extends AnyVal
    {
      def isBeforeOrEquals(instant: ReadableInstant): Boolean = dateTime.isBefore(instant) || dateTime.isEqual(instant)

      def until(end: DateTime): Interval = new Interval(dateTime, end)

      def by(step: ReadablePeriod): Seq[DateTime] = Iterator.iterate(dateTime)(_.plus(step)).toStream
    }

  }

}
