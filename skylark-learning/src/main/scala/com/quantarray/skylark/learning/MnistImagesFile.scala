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

import java.io.IOException

/**
 * MNIST images file.
 *
 * @author Araik Grigoryan
 */
class MnistImagesFile(name: String, mode: String) extends MnistDbFile(name, mode)
{
  val numberOfRows: Int = readInt()
  val numberOfColumns: Int = readInt()

  /**
   * Reads the image at the current position.
   */
  @throws(classOf[IOException])
  def readImage: Array[Array[Int]] = Array.fill[Int](numberOfRows, numberOfColumns)(readUnsignedByte())

  override protected def magicNumber: Int = 2051

  override protected def entryLength: Int = numberOfRows * numberOfColumns

  override protected def headerSize: Int = 16
}
