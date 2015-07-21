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

import java.io.{IOException, RandomAccessFile}

/**
 * MNIST database file.
 *
 * @author Araik Grigoryan
 */
abstract class MnistDbFile(name: String, mode: String) extends RandomAccessFile(name, mode)
{
  if (magicNumber != readInt)
  {
    throw new RuntimeException("This MNIST DB file " + name + " should start with the number " + magicNumber + ".")
  }

  val count = readInt

  /**
   * MNIST DB files start with unique integer number.
   *
   * @return integer number that should be found in the beginning of the file.
   */
  protected def magicNumber: Int

  /**
   * The current entry index.
   */
  @throws(classOf[IOException])
  def currentIndex: Long = (getFilePointer - headerSize) / entryLength

  /**
   * Set the required current entry index.
   */
  def currentIndex(currentIndex: Long)
  {
    try
    {
      if (currentIndex < 0 || currentIndex > count - 1)
      {
        throw new RuntimeException(s"$currentIndex is not in the range 0 to ${count - 1}")
      }
      seek(headerSize + currentIndex * entryLength)
    }
    catch
      {
        case e: IOException => throw new RuntimeException(e)
      }
  }

  protected def headerSize: Int

  /**
   * Number of bytes for each entry
   */
  protected def entryLength: Int

  /**
   * Move to the next entry.
   */
  @throws(classOf[IOException])
  def next()
  {
    if (currentIndex < count)
    {
      skipBytes(entryLength)
    }
  }

  /**
   * Move to the previous entry.
   */
  @throws(classOf[IOException])
  def prev()
  {
    if (currentIndex >= 0)
    {
      seek(getFilePointer - entryLength)
    }
  }
}
