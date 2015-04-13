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

package com.quantarray.skylark.measure.conversion

import com.quantarray.skylark.measure._

/**
 * Asset constant conversion provider.
 *
 * @author Araik Grigoryan
 */
class PhysicalConstantConversionProvider() extends ConstantConversionProvider with CommonFactors
{
  private[this] def OneOpt = Some(1.0)

  private[this] def crossSystemFactor(numerator: Measure, denominator: Measure, asset: Option[Asset]): Option[Double] =
  {
    val defaultCrossFactor = crossSystemFactor(numerator, denominator)

    asset match
    {
      case Some(a) => a.factor(numerator, denominator).orElse(defaultCrossFactor)
      case _ => defaultCrossFactor
    }
  }

  /**
   * Gets a factor for measures in the same dimension.
   *
   * Gets factor in the form numerator/denominator, such that if you multiply the factor by units of denominator,
   * the resulting units are the units of the numerator (i.e. denominator units cancel out).
   *
   * Substance is optional because most conversion factors are not substance-specific. For the case when
   * conversion factors *are* substance specific (e.g. 1 barrel of oil = 42 gallons of oil), one should
   * supply the substance.
   */
  private[this] def factorInSameDimension(numerator: Measure, denominator: Measure, asset: Option[Asset]): Option[Double] =
  {
    def factorAny(): Option[Double] =
    {
      def crossSystemFactor = this.crossSystemFactor(numerator, denominator, asset)

      (numerator.multBase, denominator.multBase) match
      {
        case (None, None) => crossSystemFactor
        case (Some(nb), None) =>
          if (numerator.system == denominator.system)
          {
            Some(1.0 / nb._1)
          }
          else
          {
            val crossFactor = factorInSameDimension(nb._2, denominator, asset)

            crossFactor match
            {
              case Some(cf) => Some(cf / nb._1)
              case _ => crossSystemFactor
            }
          }
        case (None, Some(db)) =>
          if (numerator.system == denominator.system)
          {
            Some(db._1)
          }
          else
          {
            val crossFactor = factorInSameDimension(numerator, db._2, asset)

            crossFactor match
            {
              case Some(cf) => Some(cf * db._1)
              case _ => crossSystemFactor
            }
          }
        case (Some(nb), Some(db)) =>
          if (numerator.system == denominator.system)
          {
            Some(db._1 / nb._1)
          }
          else
          {
            val crossFactor = factorInSameDimension(nb._2, db._2, asset)

            crossFactor match
            {
              case Some(cf) => Some(cf * db._1 / nb._1)
              case _ => None
            }
          }
        case _ => None
      }
    }

    (numerator, denominator) match
    {
      case (num, den) if num == den => OneOpt

      case (RatioMeasure(nn, nd), RatioMeasure(dn, dd)) =>
        (factorInSameDimension(nn, dn, asset), factorInSameDimension(nd, dd, asset)) match
        {
          case (Some(nf), Some(df)) => Some(nf / df)
          case _ => None
        }
      case (num: ProductMeasure, den: ProductMeasure) =>
        (factorInSameDimension(num.multiplicand, den.multiplicand, asset), factorInSameDimension(num.multiplier, den.multiplier, asset)) match
        {
          case (Some(nf), Some(df)) => Some(nf * df)
          case _ => None
        }
      case (num: ExponentialMeasure, den: ExponentialMeasure) if num.exp == den.exp =>

        factorInSameDimension(num.base, den.base, asset) match
        {
          case Some(f) => Some(math.pow(f, num.exp))
          case _ => None
        }
      case _ => factorAny()
    }
  }

  /**
   * Gets a factor for measures defined in two same or different dimensions.
   *
   * Gets factor in the form numerator/denominator, such that if you multiply the factor by units of denominator,
   * the resulting units are the units of the numerator (i.e. denominator units cancel out).
   *
   * Substance is optional because most conversion factors are not substance-specific. For the case when
   * conversion factors *are* substance specific (e.g. 1 barrel of oil = 42 gallons of oil), one should
   * supply the substance.
   */
  override def factor(numerator: Measure, denominator: Measure): Option[Double] =
  {
    (numerator, denominator) match
    {
      case (num: AssetMeasureLike[_], den) => factorInSameDimension(num.measure, den, Some(num.asset))
      case (num, den: AssetMeasureLike[ _]) => factorInSameDimension(num, den.measure, Some(den.asset))
      case _ => factorInSameDimension(numerator, denominator, None)
    }
  }

  /**
   * Gets a function that converts from source to target measure.
   */
  override def fun(source: Measure, target: Measure)(d: Double): Option[Double] = ???
}