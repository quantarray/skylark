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

/**
  * Same type converter.
  *
  * @author Araik Grigoryan
  */
trait SameTypeConverter[T] extends Converter[T, T]
{
  override final def apply(from: T, to: T): Option[Double] = convert(from, to).orElse(convert(to, from).map(1 / _))

  protected def convert(from: T, to: T): Option[Double] = if (from == to) Some(1.0) else None
}

object SameTypeConverter
{
  def apply[T]: SameTypeConverter[T] = new SameTypeConverter[T] {}
}
