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

import java.io.{BufferedWriter, FileWriter}

/**
 * MNIST data provider.
 *
 * @author Araik Grigoryan
 */
class MnistDataProvider(imagesFile: String, labelsFile: String)
{
  lazy val read = new MnistDataReader(imagesFile, labelsFile)

  lazy val write = new MnistDataWriter()

  def close(): Unit =
  {
  }

  class MnistDataReader(imagesFile: String, labelsFile: String)
  {
    private val images = new MnistImagesFile(imagesFile, "r")

    private val labels = new MnistLabelsFile(labelsFile, "r")

    val count = images.count

    def imageLabel(index: Long): (Array[Array[Int]], Int) =
    {
      images.currentIndex(index)
      labels.currentIndex(index)

      (images.readImage, labels.readLabel)
    }

    def currentIndex: (Long, Long) = (images.currentIndex, labels.currentIndex)

    def set: MnistSupervisedDataSet = set(count)

    def set(count: Int): MnistSupervisedDataSet = set(0, count)

    def set(start: Int, count: Int): MnistSupervisedDataSet = MnistSupervisedDataSet((start until math.min(start + count, this.count)).map(imageLabel(_)).map(MnistSupervisedDataSample))
  }

  class MnistDataWriter()
  {
    def image(image: Array[Array[Int]], ppmFileName: String): Unit =
    {
      val writer = new BufferedWriter(new FileWriter(ppmFileName))
      val numberOfRows = image.length
      val numberOfColumns = image(0).length
      writer.write("P3\n")
      writer.write("" + numberOfRows + " " + numberOfColumns + " 255\n")
      for
      {
        i <- 0 until numberOfRows
        j <- 0 until numberOfColumns
      } yield writer.write(s"${image(i)(j)} ${image(i)(j)} ${image(i)(j)}  ")
      writer.close()
    }
  }

}
