/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */

package com.quantarray.skylark.time

import org.joda.time.DateTime
import org.joda.time.chrono.ISOChronology
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter, ISODateTimeFormat}

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
