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

import scala.collection.immutable.SortedMap

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
    val layerTargetGroups = net.connections.groupBy(_.target.layer).map((ls) => (ls._1, ls._2.groupBy(_.target)))

    println(layerTargetGroups)

    // TODO: Generalize collection of weight and biases into one function
    val weights = layerTargetGroups.foldLeft(SortedMap.empty[Int, SortedMap[Int, Seq[Double]]])((m, x) =>
    {
      m + (x._1.index -> x._2.foldLeft(SortedMap.empty[Int, Seq[Double]])((n, y) =>
      {
        n + (y._1.index -> y._2.filter(_.source.nonBias).map(_.weight))
      }))
    })

    println(weights)

    val biases = layerTargetGroups.foldLeft(SortedMap.empty[Int, SortedMap[Int, Seq[Double]]])((m, x) =>
    {
      m + (x._1.index -> x._2.foldLeft(SortedMap.empty[Int, Seq[Double]])((n, y) =>
      {
        n + (y._1.index -> y._2.filter(_.source.isBias).map(_.weight))
      }))
    })

    println(biases)

    dataSet.samples.map(sample =>
    {
      val activation = sample.input

      sample
    })

    net
  }

  def backprop(targetNeuronToSynapses: Map[Neuron, Seq[Synapse]], dataSample: SupervisedDataSample) =
  {

  }
}
