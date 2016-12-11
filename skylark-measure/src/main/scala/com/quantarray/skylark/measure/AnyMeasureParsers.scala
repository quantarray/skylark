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

import com.quantarray.skylark.measure.any.arithmetic._
import scala.util.parsing.combinator.JavaTokenParsers

/**
  * Any measure parsers.
  *
  * @author Araik Grigoryan
  */
trait AnyMeasureParsers extends JavaTokenParsers
{
  def measureAtoms: Map[String, AnyMeasure]

  def measureExpression: Parser[AnyMeasure] = measureTerm

  def measureTerm: Parser[AnyMeasure] = productMeasureFactor | ratioMeasureFactor | exponentialMeasureFactor | measureFactor

  def productMeasureFactor: Parser[AnyMeasure] = measureFactor ~ "*" ~ measureTerm ^^
    {
      case multiplicand ~ _ ~ multiplier => *(multiplicand, multiplier)
    }

  def ratioMeasureFactor: Parser[AnyMeasure] = measureFactor ~ "/" ~ measureTerm ^^
    {
      case numerator ~ _ ~ denominator => /(numerator ,denominator)
    }

  def exponentialMeasureFactor: Parser[AnyMeasure] = measureFactor ~ "^" ~ floatingPointNumber ^^
    {
      case base ~ _ ~ exponent => ^(base, exponent.toDouble)
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
        case measureName if measureAtoms.contains(measureName) => measureAtoms(measureName)
      }

  def parseMeasure(measure: String): ParseResult[AnyMeasure] = parseAll(measureExpression, measure)

  def parse(measure: String): Option[AnyMeasure] =
  {
    val parseResult = parseMeasure(measure)

    if(parseResult.successful) Some(parseResult.get) else None
  }
}

object AnyMeasureParsers
{
  def apply(measures: Iterable[(String, AnyMeasure)]): AnyMeasureParsers = new AnyMeasureParsers
  {
    val measureAtoms: Map[String, AnyMeasure] = measures.toMap
  }

  def apply(measures: AnyMeasure*): AnyMeasureParsers = apply(measures.map(measure => measure.name -> measure))
}
