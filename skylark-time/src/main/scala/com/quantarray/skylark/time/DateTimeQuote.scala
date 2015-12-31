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

import scala.language.experimental.macros
import scala.reflect.macros.blackbox


/**
  * DateTime quote.
  *
  * @author Araik Grigoryan
  */
object DateTimeQuote
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

  def dImpl(c: blackbox.Context)(args: c.Expr[Any]*): c.Expr[DateTime] =
  {
    import c.universe._

    val quoteRegex =
      """^(?:[1-9]\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\d(?:0[48]|[2468][048]|[13579][26])
        |(?:[2468][048]|[13579][26])00)-02-29)$""".r

    c.prefix.tree match
    {
      case Apply(_, List(Apply(_, partTrees))) =>

        val parts: List[String] = partTrees map
          { case Literal(Constant(const: String)) => const }

        val quote = parts.head

        quoteRegex.pattern.matcher(quote).matches() match
        {
          case true => c.Expr[DateTime](q"com.quantarray.skylark.time.DateTimeQuote.StringToDateTime($quote).d")
          case _ => c.abort(c.enclosingPosition, s"Cannot parse $quote to convert it into ${classOf[DateTime]}.")
        }


      case _ => c.abort(c.enclosingPosition, "Invalid date.")
    }
  }

  def dtImpl(c: blackbox.Context)(args: c.Expr[Any]*): c.Expr[DateTime] =
  {
    import c.universe._

    val quoteRegex =
      """^(?:[1-9]\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\d(?:0[48]|[2468][048]|[13579][26])
        |(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\d|2[0-3]):[0-5]\d:[0-5]\d(?:Z|[+-][01]\d:[0-5]\d)$""".r

    c.prefix.tree match
    {
      case Apply(_, List(Apply(_, partTrees))) =>

        val parts: List[String] = partTrees map
          { case Literal(Constant(const: String)) => const }

        val quote = parts.head

        quoteRegex.pattern.matcher(quote).matches() match
        {
          case true => c.Expr[DateTime](q"com.quantarray.skylark.time.DateTimeQuote.StringToDateTime($quote).d")
          case _ => c.abort(c.enclosingPosition, s"Cannot parse $quote to convert it into ${classOf[DateTime]}.")
        }


      case _ => c.abort(c.enclosingPosition, "Invalid date.")
    }
  }

  implicit final class DateTimeQuoteContext(private val sc: StringContext) extends AnyVal
  {
    def d(args: Any*): DateTime = macro dImpl

    def dt(args: Any*): DateTime = macro dtImpl
  }

}
