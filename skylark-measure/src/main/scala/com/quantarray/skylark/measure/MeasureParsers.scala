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

import scala.util.parsing.combinator.JavaTokenParsers

/**
  * Measure parsers.
  *
  * @author Araik Grigoryan
  */
trait MeasureParsers extends JavaTokenParsers
{
  val measureProvider: MeasureProvider

  def measureExpression: Parser[AnyMeasure] = measureTerm

  def measureTerm: Parser[AnyMeasure] = productMeasureFactor | ratioMeasureFactor | exponentialMeasureFactor | measureFactor

  def productMeasureFactor: Parser[AnyMeasure] = measureFactor ~ "*" ~ measureTerm ^^
    {
      case multiplicand ~ _ ~ multiplier => untyped.*(multiplicand, multiplier)
    }

  def ratioMeasureFactor: Parser[AnyMeasure] = measureFactor ~ "/" ~ measureTerm ^^
    {
      case numerator ~ _ ~ denominator => untyped./(numerator, denominator)
    }

  def exponentialMeasureFactor: Parser[AnyMeasure] = measureFactor ~ "^" ~ floatingPointNumber ^^
    {
      case base ~ _ ~ exponent => untyped.^(base, exponent.toDouble)
    }

  def measureFactor: Parser[AnyMeasure] = measureMatter | measureParenthesizedExpression

  def measureParenthesizedExpression: Parser[AnyMeasure] = "(" ~ measureExpression ~ ")" ^^
    {
      case _ ~ measure ~ _ => measure
    }

  def measureMatter: Parser[AnyMeasure] = measureAtom

  def measureAtom: Parser[AnyMeasure] =
    """[^()*/\^\s]+""".r ^^
      {
        case measureName if measureProvider.read(measureName).isDefined => measureProvider.read(measureName).get
      }

  def parseMeasure(measure: String): ParseResult[AnyMeasure] = parseAll(measureExpression, measure)
}
