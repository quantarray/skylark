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

package com.quantarray.skylark.measure

/**
  * Quasi-numeric type class.
  *
  * @author Araik Grigoryan
  */
trait QuasiNumeric[@specialized(Double, Int) T]
{
  def negate(x: T): T

  def plus(x: T, y: T): T

  def minus(x: T, y: T): T

  def times(x: T, y: T): T

  def divide(x: T, y: T): T

  def timesConstant(x: T, c: Double): T

  def divideByConstant(x: T, c: Double): T

  def pow(x: T, c: Double): T
}

object QuasiNumeric
{

  implicit object DoubleQuasiNumeric extends QuasiNumeric[Double] with Serializable
  {
    override def negate(x: Double): Double = -x

    override def plus(x: Double, y: Double): Double = x + y

    override def minus(x: Double, y: Double): Double = x - y

    override def times(x: Double, y: Double): Double = x * y

    override def divide(x: Double, y: Double): Double = x / y

    override def timesConstant(x: Double, c: Double): Double = x * c

    override def divideByConstant(x: Double, c: Double): Double = x / c

    override def pow(x: Double, c: Double): Double = math.pow(x, c)
  }

}
