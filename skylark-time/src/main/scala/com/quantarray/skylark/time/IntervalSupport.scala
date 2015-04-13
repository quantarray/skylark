/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */

package com.quantarray.skylark.time

import com.quantarray.skylark.time.DateTimeSupport.Implicits.RichDateTime
import org.joda.time.{ReadablePeriod, Interval, ReadableInstant}

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
