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

  def measureExpression: Parser[UntypedMeasure] = measureTerm

  def measureTerm: Parser[UntypedMeasure] = productMeasureFactor | ratioMeasureFactor | exponentialMeasureFactor | measureFactor

  def productMeasureFactor: Parser[UntypedMeasure] = measureFactor ~ "*" ~ measureTerm ^^
    {
      case multiplicand ~ _ ~ multiplier => UntypedProductMeasure(multiplicand, multiplier)
    }

  def ratioMeasureFactor: Parser[UntypedMeasure] = measureFactor ~ "/" ~ measureTerm ^^
    {
      case numerator ~ _ ~ denominator => UntypedRatioMeasure(numerator, denominator)
    }

  def exponentialMeasureFactor: Parser[UntypedMeasure] = measureFactor ~ "^" ~ floatingPointNumber ^^
    {
      case base ~ _ ~ exponent => UntypedExponentialMeasure(base, exponent.toDouble)
    }

  def measureFactor: Parser[UntypedMeasure] = measureMatter | measureParenthesizedExpression

  def measureParenthesizedExpression: Parser[UntypedMeasure] = "(" ~ measureExpression ~ ")" ^^
    {
      case _ ~ measure ~ _ => measure
    }

  def measureMatter: Parser[UntypedMeasure] = measureAtom

  def measureAtom: Parser[UntypedMeasure] =
    """[^()*/\^\s]+""".r ^^
      {
        case measureName if measureProvider.read(measureName).isDefined => measureProvider.read(measureName).get
      }

  def parseMeasure(measure: String): ParseResult[UntypedMeasure] = parseAll(measureExpression, measure)
}
