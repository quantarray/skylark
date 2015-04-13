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

package com.quantarray.skylark.measure.market

import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.substance.StandardSubstance
import com.quantarray.skylark.time.StringDate._
import org.joda.time.DateTime
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.OptionValues._

/**
 * Global market spec.
 *
 * @author Araik Grigoryan
 */
class GlobalMarketSpec extends FlatSpec with Matchers
{
  "Curves" should "be addable and inspectable" in
    {
      implicit val valueDate = "2012-12-29".d

      val honey = new StandardSubstance("Honey", 200 * (g / m3))
      val mapleSyrup = new StandardSubstance("Maple Syrup", 220 * (g / m3))

      implicit val market = new GlobalMarket(
        Seq(
          DiscreteForwardCurve(USD / (gal of honey), Seq(("2015-03-07".d, 3.0))),
          DiscreteForwardCurve(USD / (gal of mapleSyrup), Seq(("2015-03-07".d, 4.0))
          )
        )
      )

      val honeyPriceSignal = PriceSignal(USD / (gal of honey))

      market.manifold(honeyPriceSignal) match
      {
        case Some(curve: TermPriceCurve[_]) =>

          curve.value("2015-03-07".d).value should equal(3)

        case _ =>
      }

      val mapleSyrupPriceSignal = PriceSignal(USD / (gal of mapleSyrup))

      market.manifold(mapleSyrupPriceSignal) match
      {
        case Some(curve: TermPriceCurve[_]) =>

          curve.value("2015-03-07".d).value should equal(4)

        case _ =>
      }
    }
}
