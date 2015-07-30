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

package com.quantarray.skylark.learning.neural

/**
 * Activation function.
 *
 * @author Araik Grigoryan
 */
trait Activation extends (Double => Double)
{
  /**
   * Derivative of this function.
   */
  def d(x: Double): Double
}

object IdentityActivation extends Activation
{
  override def apply(x: Double): Double = x

  /**
   * Derivative of this function.
   */
  override def d(x: Double): Double = 0

  override def toString(): String = "identity"
}

object SigmoidActivation extends Activation
{
  override def apply(x: Double): Double = 1.0 / (1.0 + math.exp(-x))

  /**
   * Derivative of this function.
   */
  override def d(x: Double): Double = SigmoidActivation(x) * (1.0 - SigmoidActivation(x))

  override def toString(): String = "sigmoid"
}

case class LogisticActivation(x0: Double, l: Double, k: Double) extends Activation
{
  override def apply(x: Double): Double = l / (1.0 + math.exp(-k * (x - x0)))

  /**
   * Derivative of this function.
   */
  override def d(x: Double): Double = ??? // FIXME: Implement derivative of Logistic activation function

  override def toString(): String = "logistic"
}

object HyperbolicTangentActivation extends Activation
{
  override def apply(x: Double): Double = (math.exp(2 * x) - 1) / (math.exp(2 * x) + 1)

  /**
   * Derivative of this function.
   */
  override def d(x: Double): Double = 1.0 - HyperbolicTangentActivation(x) * HyperbolicTangentActivation(x)

  override def toString(): String = s"tanh"
}