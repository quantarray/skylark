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

/**
 * MNIST supervised data sample.
 *
 * @author Araik Grigoryan
 */
case class MnistSupervisedDataSample(imageLabel: (Array[Array[Int]], Int)) extends SupervisedDataSample
{
  private lazy val zeros = Seq.fill(10)(0.0)

  lazy val input: Seq[Double] = imageLabel._1.flatten.map(_.toDouble)

  lazy val target: Seq[Double] = zeros.patch(imageLabel._2, Seq(1.0), 1)

  override def toString: String = s"MNIST label ${imageLabel._2}"
}
