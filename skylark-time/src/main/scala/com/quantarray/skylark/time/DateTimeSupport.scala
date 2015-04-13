/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */

package com.quantarray.skylark.time

import org.joda.time._

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
