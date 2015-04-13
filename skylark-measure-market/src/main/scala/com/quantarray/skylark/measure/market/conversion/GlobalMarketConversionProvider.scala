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

package com.quantarray.skylark.measure.market.conversion

import com.quantarray.skylark.measure.conversion.AssetConversionProvider
import com.quantarray.skylark.measure.market._
import com.quantarray.skylark.measure._
import org.joda.time.DateTime

/**
 * Global market conversion provider.
 *
 * @author Araik Grigoryan
 */
class GlobalMarketConversionProvider(acp: AssetConversionProvider) extends MarketConversionProvider
{
  /**
   * Gets factor in the form numerator/denominator, such that if you multiply the factor by units of denominator,
   * the resulting units are the units of the numerator (i.e. denominator units cancel out).
   */
  override def factor(numerator: Measure, denominator: Measure, market: Option[Market]): Option[Double] =
  {
    (numerator, denominator, market) match
    {
      case (num: AssetMeasureLike[_], den: AssetMeasureLike[_], None) if num.asset == den.asset => acp.factor(num.measure, den.measure)
      case (num: AssetMeasureLike[_], den: AssetMeasureLike[_], None) => None
      case (num: AssetMeasureLike[_], _, None) => acp.factor(num, denominator)
      case (_, den: AssetMeasureLike[_], None) => acp.factor(numerator, den)
      case (num, den, Some(mkt)) if num == den => Some(1.0)
      case (num, den, Some(mkt)) =>
        resolve(num, den, mkt) match
        {
          case (Some(man), Some(space), Some(time)) => man.value(time)
          case (Some(man), _, Some(time)) => man.value(time)
          case _ => acp.factor(num, den)
        }
      case _ => acp.factor(numerator, denominator)
    }
  }

  private def resolve(numerator: Measure, denominator: Measure, market: Market): (Option[MarketManifold[_ <: Measure, DateTime]], Option[Space], Option[DateTime]) =
  {
    def resolvePriceManifold(numerator: Measure, denominator: Measure): Option[MarketManifold[_ <: Measure, DateTime]] =
    {
      val priceManifold = market.manifold(PriceSignal(numerator / denominator))

      (numerator, denominator, priceManifold) match
      {
        case (num, den, Some(curve: TermPriceCurve[_])) => Some(curve)
        case (num@SpacetemporalMeasure(nMeasure, nSpacetime), den@SpacetemporalAssetMeasure(dMeasure, dAsset, dSpaceTime), _) =>
          resolvePriceManifold(nMeasure, den).orElse(resolvePriceManifold(num, den.measure))
        case _ => None
      }
    }

    def resolveSpace(numerator: Measure, denominator: Measure): Option[Space] =
    {
      (numerator, denominator) match
      {
        case (num@SpacetemporalMeasure(nMeasure, Spacetime(None, _)), den@SpacetemporalAssetMeasure(dMeasure, dAsset, Spacetime(Some(dSpace), _))) => Some(dSpace)
        case _ => None
      }
    }

    def resolveTime(numerator: Measure, denominator: Measure): Option[DateTime] =
    {
      val timeInstantManifold = market.manifold(TimeSignal)

      (numerator, denominator, timeInstantManifold) match
      {
        case (num@SpacetemporalMeasure(nMeasure, Spacetime(_, Some(nTime))), den@SpacetemporalAssetMeasure(dMeasure, dAsset, Spacetime(_, None)), _) => Some(nTime)
        case (_, _, Some(TimeInstantCurve(time))) => Some(time)
        case _ => None
      }
    }

    def reciprocal[M <: Measure, K](manifold: MarketManifold[M, K]) =
      ValueTransformativeMarketManifold[M, K](manifold, (key, value) => value.fold[Option[Double]](None)(x => Some(1 / x)))

    (resolvePriceManifold(numerator, denominator)
      .orElse(resolvePriceManifold(denominator, numerator).map(reciprocal(_))),
      resolveSpace(numerator, denominator),
      resolveTime(numerator, denominator))
  }
}
