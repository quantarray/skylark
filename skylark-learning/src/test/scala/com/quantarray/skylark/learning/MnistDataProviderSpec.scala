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

package com.quantarray.skylark.learning

import java.io.File

import org.scalatest.{FlatSpec, Matchers}

class MnistDataProviderSpec extends FlatSpec with Matchers
{
  "MnistDataProviderSpec" should "read and write" in
    {
      val dataProvider = new MnistDataProvider("data/mnist/t10k-images-idx3-ubyte", "data/mnist/t10k-labels-idx1-ubyte")

      (0 to 10).foreach(imageIndex =>
      {
        val imageLabel = dataProvider.read.imageLabel(imageIndex)

        imageLabel._1.length should be(28)
        imageLabel._1(0).length should be(28)

        val ppmFileName = s"$imageIndex-${imageLabel._2}.ppm"

        dataProvider.write.image(imageLabel._1, ppmFileName)

        val isDeleted = new File(ppmFileName).delete()

        isDeleted should be(true)
      })

      dataProvider.close()
    }
}
