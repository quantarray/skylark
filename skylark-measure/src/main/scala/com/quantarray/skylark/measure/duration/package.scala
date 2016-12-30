package com.quantarray.skylark.measure

import com.quantarray.skylark.measure.measures._

import scala.concurrent.duration._
import scala.language.implicitConversions

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object duration
{
  trait DefaultDurationImplicits
  {
    implicit def timeQuantityToDuration(quantity: Quantity[Double, TimeMeasure]): Duration = quantity.measure match
    {
      case `ns` | `nsec` | `nanosec` | `nanosecond` | `nsecs` | `nanosecs` | `nanoseconds` => Duration(quantity.value, NANOSECONDS)
      case `μs` | `μsec` | `μsecond` | `microsec` | `microsecond` | `μsecs` | `μseconds` | `microsecs` | `microseconds` =>
        Duration(quantity.value, MICROSECONDS)
      case `ms` | `msec` | `millisec` | `millisecond` | `msecs` | `millisecs` | `milliseconds` => Duration(quantity.value, MILLISECONDS)
      case `s` | `sec` | `second` | `secs` | `seconds` => Duration(quantity.value, SECONDS)
      case `min` | `minute` | `mins` | `minutes` => Duration(quantity.value, MINUTES)
      case `h` | `hour` | `hours` => Duration(quantity.value, HOURS)
      case `day` | `days` => Duration(quantity.value, DAYS)
      case `year365` | `years365` | `year` | `years` => Duration(365 * quantity.value, DAYS)
      case `year360` | `years360` => Duration(360 * quantity.value, DAYS)
      case `fortnight` => Duration(14 * quantity.value, DAYS)
    }

    implicit def durationToTimeQuantity(duration: FiniteDuration): Quantity[Double, TimeMeasure] = duration.unit match
    {
      case NANOSECONDS => Quantity(duration.length, ns)
      case MICROSECONDS => Quantity(duration.length, μs)
      case MILLISECONDS => Quantity(duration.length, ms)
      case SECONDS => Quantity(duration.length, s)
      case MINUTES => Quantity(duration.length, min)
      case HOURS => Quantity(duration.length, hours)
      case DAYS => Quantity(duration.length, days)
    }
  }

  object default extends AnyRef with DefaultDurationImplicits
}
