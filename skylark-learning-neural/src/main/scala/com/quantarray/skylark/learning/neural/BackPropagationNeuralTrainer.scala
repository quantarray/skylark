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
 * Back propagation neural trainer.
 *
 * @author Araik Grigoryan
 */
case class BackPropagationNeuralTrainer(numberOfEpochs: Int, learningRate: Double, momentum: Double)
  extends NeuralTrainer[FeedForwardNeuralNet] with BreezeNeuralTraining[FeedForwardNeuralNet]
{
  override def train(net: FeedForwardNeuralNet, dataSet: SupervisedDataSet): FeedForwardNeuralNet =
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

  def train(activation: NeuralActivation, weights: NeuralNetMap[Double], biases: NeuralNetMap[Double], dataSample: SupervisedDataSample) =
  {
    // Forward-propagate the input
    val xszs = weights.keys.foldLeft((List(DenseMatrix(dataSample.input: _*)), List.empty[DenseMatrix[Double]]))((xszs, layerIndex) =>
    {
      val xs = xszs._1
      val zs = xszs._2

      // m by 1 vector, where m is the number of inputs to the layer identified by the layerIndex
      val x = xs.head

      // m by n matrix, where m is number of inputs to the layer identified by the layerIndex
      // n is the number inputs to the layer identified by layerIndex + 1
      val w = DenseMatrix(weights(layerIndex).values.toSeq: _*)

      // n by 1 vector, where m is the number of inputs for to layer identified by the layerIndex + 1
      val b = DenseMatrix(biases(layerIndex + 1).values.toSeq: _*)

      val z = (w.t * x: DenseMatrix[Double]) + b

      val newX = z.map(activation)

      (newX :: xs, z :: zs)
    })

    // FIXME: Make explicit the assumption that there are at least 2 layers in the network

    // Backward-propagate errors
    val xs = xszs._1
    val zs = xszs._2

    // Activation output
    val x = xs.head
    // Target output
    val y = DenseMatrix(dataSample.target: _*)
    val z = zs.head
    val delta = QuadraticObjective.d(z, x, y) :* z.map(activation.d)

    val nablaB = delta
    val nablaW: DenseMatrix[Double] = delta * xs.tail.head.t

    (xs.tail.tail, zs.tail, 2 until xs.size).zipped.foldLeft((List(nablaB), List(nablaW), delta))((nablaBWs, xzLayerIndexes) =>
    {
      val nablaBs = nablaBWs._1
      val nablaWs = nablaBWs._2
      val delta = nablaBWs._3

      val x = xzLayerIndexes._1
      val z = xzLayerIndexes._2
      val layerIndex = xzLayerIndexes._3

      val w = DenseMatrix(weights(layerIndex - 1).values.toSeq: _*)

      val wd: DenseMatrix[Double] = w * delta

      val newDelta = wd :* z.map(activation.d)

      val nablaB = newDelta
      val dx: DenseMatrix[Double] = newDelta * x.t
      val nablaW = dx

      (nablaB :: nablaBs, nablaW :: nablaWs, newDelta)
    })

    println()
  }
}
