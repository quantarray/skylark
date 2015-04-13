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

import com.quantarray.skylark.measure.Spacetime.Implicits.SpaceToSpacetime
import com.quantarray.skylark.measure.conversion.{PhysicalConstantConversionProvider, PhysicalSubstanceConversionProvider}
import com.quantarray.skylark.measure.substance.commodities.PhysicalSubstances
import org.joda.time.DateTime
import org.scalatest.OptionValues._
import org.scalatest.{FlatSpec, Matchers}

/**
 * Measure spec.
 *
 * @author Araik Grigoryan
 */
class MeasureSpec extends FlatSpec with Matchers with PhysicalSubstances
{
  implicit val constantConversion = new PhysicalConstantConversionProvider()

  implicit val substanceConversion = new PhysicalSubstanceConversionProvider(constantConversion)

  "kg" should "have expected properties" in
    {
      kg.name should be("kg")
      kg.dimension should be(Mass)
      kg.system should be(SI)
      kg.declMultBase.value should be((1000, g))
      kg.multBase.value should be((1000, g))
      kg.isStructuralAtom should be(right = true)
      kg.expBase should be(kg)
      kg.exp should be(1.0)
      kg.inverse should be(ExponentialMeasure(kg, -1.0))
      kg / lb should be(RatioMeasure(kg, lb))
      kg * 17.0 should be(MassMeasure(s"17.0 $kg", (17000.0, g)))
      kg ^ 2 should be(ExponentialMeasure(kg, 2.0))
      kg ^(2, Some((1.0, g ^ 2))) should be(ExponentialMeasure(kg, 2.0, Some((1.0, ExponentialMeasure(g, 2.0, None)))))
    }

  it should "equal itself" in
    {
      kg should be(kg)
    }

  it should "be convertible to lb" in
    {
      constantConversion.factor(kg, lb).value should be(0.45359188070533535)
      constantConversion.factor(lb, kg).value should be(2.204625)

      (kg to lb).value should be(2.204625)
      (lb to kg).value should be(0.45359188070533535)
    }

  it should "be convertible to lb in presence of an attached substance" in
    {
      constantConversion.factor(kg of cotton, lb of cotton).value should be(0.45359188070533535)

      (kg of cotton to lb).value should be(2.204625)
      (lb of cotton to (kg of cotton)).value should be(0.45359188070533535)

      (kg of cotton to kg).value should be(1)
    }

  it should "be composable with supported DSL" in
    {
      (kg of cotton) at HereAndNow
      (kg of cotton) at Here.on(DateTime.now)
      (kg of cotton) on DateTime.now
      (kg of cotton) on Earth
    }

  "bbl" should "be convertible to gal depending on the substance" in
    {
      (bbl to gal).value should equal(31.5)
      (bbl of wti to gal).value should equal(42)
    }

}
