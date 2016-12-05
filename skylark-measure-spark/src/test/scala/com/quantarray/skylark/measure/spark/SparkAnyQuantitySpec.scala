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

package com.quantarray.skylark.measure.spark

import com.quantarray.skylark.measure.AnyMeasureParsers.ops._
import com.quantarray.skylark.measure.any.arithmetic.safe._
import com.quantarray.skylark.measure.any.measures._
import com.quantarray.skylark.measure.{AnyMeasureParsers, AnyQuantity, AnyQuantityRef}
import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, fixture}

class SparkAnyQuantitySpec extends fixture.FreeSpec with fixture.TestDataFixture with Matchers
{
  "Spark local" -
    {
      "should create AnyQuantity Dataset" in
        {
          fixture =>

            implicit val session = SparkSession.builder
              .master("local[*]")
              .appName(fixture.name)
              .getOrCreate()

            import session.implicits._

            val data = session.createDataset(1 to 3)

            val quantitiesRefs = data.map(value => AnyQuantityRef(value.toDouble, USD |/| bbl))

            val quantityRefsColl = quantitiesRefs.collect()

            quantityRefsColl.length should be(3)

            val parser = AnyMeasureParsers(USD, bbl)

            val quantities = quantityRefsColl.map(quantityRef => AnyQuantity(quantityRef.value, parser.parse(quantityRef.measureRef).get))

            quantities.foreach
            {
              quantity =>

                quantity.measure should be(USD / bbl)
            }

            session.stop()
        }
    }
}
