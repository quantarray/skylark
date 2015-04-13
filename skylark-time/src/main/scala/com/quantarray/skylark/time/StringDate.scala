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

import org.joda.time.DateTime
import org.joda.time.chrono.ISOChronology
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter, ISODateTimeFormat}

/**
 * String date.
 *
 * @author Araik Grigoryan
 */
object StringDate
{

  implicit final class StringToDateTime(private val string: String) extends AnyVal
  {
    def d: DateTime = d(ISODateTimeFormat.dateTimeParser())

    def d(formatString: String): DateTime = d(DateTimeFormat.forPattern(formatString))

    def d(formatter: DateTimeFormatter): DateTime = formatter
      .withChronology(ISOChronology.getInstance(DefaultTimeZone))
      .withOffsetParsed()
      .parseDateTime(string)

  }

}
