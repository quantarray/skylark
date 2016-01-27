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

  def measureExpression: Parser[untyped.Measure] = measureTerm

  def measureTerm: Parser[untyped.Measure] = productMeasureFactor | ratioMeasureFactor | exponentialMeasureFactor | measureFactor

  def productMeasureFactor: Parser[untyped.Measure] = measureFactor ~ "*" ~ measureTerm ^^
    {
      case multiplicand ~ _ ~ multiplier => untyped.ProductMeasure(multiplicand, multiplier)
    }

  def ratioMeasureFactor: Parser[untyped.Measure] = measureFactor ~ "/" ~ measureTerm ^^
    {
      case numerator ~ _ ~ denominator => untyped.RatioMeasure(numerator, denominator)
    }

  def exponentialMeasureFactor: Parser[untyped.Measure] = measureFactor ~ "^" ~ floatingPointNumber ^^
    {
      case base ~ _ ~ exponent => untyped.ExponentialMeasure(base, exponent.toDouble)
    }

  def measureFactor: Parser[untyped.Measure] = measureMatter | measureParenthesizedExpression

  def measureParenthesizedExpression: Parser[untyped.Measure] = "(" ~ measureExpression ~ ")" ^^
    {
      case _ ~ measure ~ _ => measure
    }

  def measureMatter: Parser[untyped.Measure] = measureAtom

  def measureAtom: Parser[untyped.Measure] =
    """[^()*/\^\s]+""".r ^^
      {
        case measureName if measureProvider.read(measureName).isDefined => measureProvider.read(measureName).get
      }

  def parseMeasure(measure: String): ParseResult[untyped.Measure] = parseAll(measureExpression, measure)
}
