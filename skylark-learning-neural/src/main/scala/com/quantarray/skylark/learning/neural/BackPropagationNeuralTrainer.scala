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
    val xs = weights.keys.foldLeft(List(DenseMatrix(dataSample.input: _*)))((xs, layerIndex) =>
    {
      // m by 1 vector, where m is the number of inputs for to layer identified by the layerIndex
      val x = xs.head

      // m by n matrix, where m is number of inputs to the layer identified by the layerIndex
      // n is the number inputs to the layer identified by layerIndex + 1
      val w = DenseMatrix(weights(layerIndex).values.toSeq: _*)

      // n by 1 vector, where m is the number of inputs for to layer identified by the layerIndex + 1
      val b = DenseMatrix(biases(layerIndex + 1).values.toSeq: _*)

      val z = (w.t * x: DenseMatrix[Double]) + b

      val newX = z.map(activation)

      newX :: xs
    })

    println(xs)
  }
}
