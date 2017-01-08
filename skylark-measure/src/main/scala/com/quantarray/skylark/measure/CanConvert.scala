/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2017 Quantarray, LLC
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

import scala.annotation.implicitNotFound

/**
  * Can convert type class.
  *
  * @author Araik Grigoryan
  */
@implicitNotFound("Cannot find CanConvert implementation that converts from ${From} to ${To}.")
trait CanConvert[From, To]
{
  def convert: Converter[From, To]
}

object CanConvert
{
  def empty[From, To]: CanConvert[From, To] = new CanConvert[From, To]
  {
    override def convert: Converter[From, To] = new Converter[From, To]
    {
    }
  }

  def apply[From, To](convert: Converter[From, To]): CanConvert[From, To] =
  {
    val params = convert

    new CanConvert[From, To]
    {
      val convert: Converter[From, To] = params
    }
  }
}