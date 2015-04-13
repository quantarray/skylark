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

package com.quantarray.skylark.measure

import scala.util.parsing.combinator.JavaTokenParsers

/**
 * Measure parsers.
 *
 * @author Araik Grigoryan
 */
trait MeasureParsers extends JavaTokenParsers
{
  implicit val measureProvider: MeasureProvider

  implicit val assetProvider: AssetProvider

  def measureExpression: Parser[Measure] = measureTerm

  def measureTerm: Parser[Measure] = productMeasureFactor | ratioMeasureFactor | exponentialMeasureFactor | measureFactor

  def productMeasureFactor: Parser[Measure] = measureFactor ~ "*" ~ measureTerm ^^
    {
      case multiplicand ~ _ ~ multiplier => ProductMeasure(multiplicand, multiplier)
    }

  def ratioMeasureFactor: Parser[Measure] = measureFactor ~ "/" ~ measureTerm ^^
    {
      case numerator ~ _ ~ denominator => RatioMeasure(numerator, denominator)
    }

  def exponentialMeasureFactor: Parser[Measure] = measureFactor ~ "^" ~ floatingPointNumber ^^
    {
      case base ~ _ ~ exponent => ExponentialMeasure(base, exponent.toDouble)
    }

  def measureFactor: Parser[Measure] = measureMatter | measureParenthesizedExpression

  def measureParenthesizedExpression: Parser[Measure] = "(" ~ measureExpression ~ ")" ^^
    {
      case _ ~ measure ~ _ => measure
    }

  def measureMatter: Parser[Measure] = measureAssetAtom | measureAtom

  def measureAssetAtom: Parser[AssetMeasure[_ <: Asset]] = measureAtom ~ "of" ~ assetAtom ^^
    {
      case measure ~ _ ~ asset => AssetMeasure(measure, asset)
    }

  def assetAtom: Parser[Asset] = """.+""".r ^^
    {
      case assetName if assetProvider.asset(assetName).isDefined => assetProvider.asset(assetName).get
    }

  def measureAtom: Parser[Measure] = """[^()*/\^\s]+""".r ^^
    {
      case measureName if measureProvider.measure(measureName).isDefined => measureProvider.measure(measureName).get
    }
}
