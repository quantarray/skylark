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
trait Activation extends (Matrix => Matrix)
{
  /**
   * Derivative of this function.
   */
  def d(x: Matrix): Matrix
}

trait ElementaryActivation extends Activation
{
  /**
   * Value of this function.
   */
  def f(x: Double): Double

  /**
   * Derivative of this function.
   */
  def df(x: Double): Double

  override def apply(x: Matrix): Matrix = x.map(f)

  override def d(x: Matrix): Matrix = x.map(df)
}

object SigmoidActivation extends ElementaryActivation
{
  override def f(x: Double): Double = 1.0 / (1.0 + math.exp(-x))

  /**
   * Derivative of this function.
   */
  override def df(x: Double): Double = SigmoidActivation.f(x) * (1.0 - SigmoidActivation.f(x))

  override def toString(): String = "sigmoid"
}

case class LogisticActivation(x0: Double, l: Double, k: Double) extends ElementaryActivation
{
  override def f(x: Double): Double = l / (1.0 + math.exp(-k * (x - x0)))

  /**
   * Derivative of this function.
   */
  override def df(x: Double): Double = ??? // FIXME: Implement derivative of Logistic activation function

  override def toString(): String = "logistic"
}

object HyperbolicTangentActivation extends ElementaryActivation
{
  override def f(x: Double): Double = (math.exp(2 * x) - 1) / (math.exp(2 * x) + 1)

  /**
   * Derivative of this function.
   */
  override def df(x: Double): Double = 1.0 - HyperbolicTangentActivation.f(x) * HyperbolicTangentActivation.f(x)

  override def toString(): String = s"tanh"
}

object RectifiedLinearActivation extends ElementaryActivation
{
  override def f(x: Double): Double = math.max(0, x)

  /**
   * Derivative of this function.
   */
  override def df(x: Double): Double = 0

  override def toString(): String = s"rectified linear"
}

/**
 * Softmax activation. Activations will be calculated independently for each column vector of the matrix.
 */
object SoftmaxActivation extends Activation
{
  override def apply(z: Matrix): Matrix =
  {
    val colSums = (0 until z.cols).map(col => (0 until z.rows).map(row => math.exp(z(row, col))).sum)
    z.mapPairs((rowCol, value) => math.exp(value) / colSums(rowCol._2))
  }

  /**
   * Derivative of this function.
   *
   * ∂f_i / ∂z_j = f_i * (1 - f_j) ; if i = j
   * ∂f_i / ∂z_j = -f_i * f_j      ; if i ≠ j
   */
  override def d(z: Matrix): Matrix =
  {
    val f = SoftmaxActivation(z)
    val fis = (0 until f.rows).map(row => f(row, 0))
    val dis = fis.zipWithIndex.zip(fis.zipWithIndex).map(fi => if (fi._1._2 == fi._2._2) fi._1._1 * (1.0 - fi._2._1) else -fi._1._1 * fi._2._1)
    Matrix(dis)
  }
}