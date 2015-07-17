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

import breeze.linalg.DenseMatrix

/**
 * Back propagation trainer.
 *
 * @author Araik Grigoryan
 */
case class BackPropagationTrainer(numberOfEpochs: Int, learningRate: Double, momentum: Double) extends Trainer[FeedForwardNet]
{
  type Matrix = DenseMatrix[Double]

  override def train(net: FeedForwardNet, dataSet: SupervisedDataSet): FeedForwardNet =
  {
    val weights = net.weightsBySource(_.source.nonBias)

    val biases = net.weightsByTarget(_.source.isBias)

    train(net.activation, weights, biases, dataSet.samples.head)

    dataSet.samples.map(sample =>
    {
      val activation = sample.input

      sample
    })

    net
  }

  def train(activation: Activation, weights: NetMap[Double], biases: NetMap[Double], dataSample: SupervisedDataSample): (Seq[Matrix], Seq[Matrix]) =
  {
    // Forward-propagate the input
    val aszs = weights.keys.foldLeft((List(DenseMatrix(dataSample.input: _*)), List.empty[Matrix]))((aszs, layerIndex) =>
    {
      val as = aszs._1
      val zs = aszs._2

      // m by 1 vector, where m is the number of inputs to the layer identified by the layerIndex
      val a = as.head

      // m by n matrix, where m is number of inputs to the layer identified by the layerIndex
      // n is the number inputs to the layer identified by layerIndex + 1
      val w = DenseMatrix(weights(layerIndex).values.toSeq: _*)

      // n by 1 vector, where m is the number of inputs for to layer identified by the layerIndex + 1
      val b = DenseMatrix(biases(layerIndex + 1).values.toSeq: _*)

      val z = (w.t * a: Matrix) + b

      val newA = z.map(activation)

      (newA :: as, z :: zs)
    })

    // FIXME: Make explicit the assumption that there are at least 2 layers in the network

    // Backward-propagate errors
    val as = aszs._1
    val zs = aszs._2

    // Activation output
    val a = as.head
    // Target output
    val y = DenseMatrix(dataSample.target: _*)
    val z = zs.head
    val delta = QuadraticObjective.d(z, a, y) :* z.map(activation.d)

    val nablaB = delta
    val nablaW: Matrix = delta * as.tail.head.t

    val (nablaBs, nablaWs, _) = (as.tail.tail, zs.tail, 2 until as.size).zipped.foldLeft((List(nablaB), List(nablaW), delta))((nablaBsNablaWsDelta, azLayerIndexes) =>
    {
      val nablaBs = nablaBsNablaWsDelta._1
      val nablaWs = nablaBsNablaWsDelta._2
      val delta = nablaBsNablaWsDelta._3

      val a = azLayerIndexes._1
      val z = azLayerIndexes._2
      val layerIndex = azLayerIndexes._3

      val w = DenseMatrix(weights(layerIndex - 1).values.toSeq: _*)

      val wd: Matrix = w * delta

      val newDelta = wd :* z.map(activation.d)

      val nablaB = newDelta
      val dx: Matrix = newDelta * a.t
      val nablaW = dx

      (nablaB :: nablaBs, nablaW :: nablaWs, newDelta)
    })

    (nablaBs, nablaWs)
  }
}
