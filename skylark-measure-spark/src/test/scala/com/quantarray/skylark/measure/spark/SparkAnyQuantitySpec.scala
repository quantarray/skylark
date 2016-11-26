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

import com.quantarray.skylark.measure.AnyQuantity
import com.quantarray.skylark.measure.measures._
import com.quantarray.skylark.measure.quantities.any._
import com.quantarray.skylark.measure.any.implicits._
import org.scalatest.{Matchers, fixture}
import org.apache.spark.sql.SparkSession

class SparkAnyQuantitySpec extends fixture.FreeSpec with fixture.TestDataFixture with Matchers
{
  "Spark local" -
    {
      "should perform AnyQuantity filter" in
        {
          fixture =>

            implicit val session = SparkSession.builder
              .master("local[*]")
              .appName(fixture.name)
              .getOrCreate()

            val data = 1 to 1000 map
              { value => AnyQuantity(value.toDouble, kg) }

            val distData = session.sparkContext.parallelize(data)

            val filter = distData.filter(_.value < 10).collect()

            filter should be(Array(1.0.kg, 2.0.kg, 3.0.kg, 4.0.kg, 5.0.kg, 6.0.kg, 7.0.kg, 8.0.kg, 9.0.kg))

            val conversion = distData.filter(_.value < 10).map(_.to(lb)).collect()

            conversion should be(Array(Some(2.204625.lb), Some(4.40925.lb), Some(6.613875.lb), Some(8.8185.lb), Some(11.023125.lb),
              Some(13.22775.lb), Some(15.432375.lb), Some(17.637.lb), Some(19.841625.lb)))

            session.stop()
        }
    }
}
