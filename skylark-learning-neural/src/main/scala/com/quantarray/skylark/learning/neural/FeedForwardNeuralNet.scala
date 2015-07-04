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
 * Feed-forward neural net.
 *
 * The head of the layers is considered the input layer.
 *
 * @author Araik Grigoryan
 */
case class FeedForwardNeuralNet(connections: Seq[Synapse]) extends NeuralNet
{
  type C = Neuron

  type L = Nucleus

  type T = Synapse
}

object FeedForwardNeuralNet
{
  /**
   * Connects layers in feed-forward fashion.
   */
  def apply(activation: NeuralActivation, numberOfNeuronsInLayer0: Int, numberOfNeuronsInLayer1: Int, numberOfNeuronsInLayer2AndUp: Int*): FeedForwardNeuralNet =
  {
    val layer0 = Nucleus(0, activation, numberOfNeuronsInLayer0)

    val layer1 = Nucleus(1, activation, numberOfNeuronsInLayer1)

    val layers2AndUp = numberOfNeuronsInLayer2AndUp.zipWithIndex.map(x => Nucleus(x._2 + 2, activation, x._1))

    val layers = layer0 +: layer1 +: layers2AndUp

    val synapses = layers.zipWithIndex.foldLeft(List.empty[Synapse])((synapsesSoFar, layerIndex) =>
    {
      if (layerIndex._2 == layers.size - 1)
      {
        synapsesSoFar
      }
      else
      {
        val sourceLayer = layerIndex._1
        val targetLayer = layers(layerIndex._2 + 1)

        val neuronSynapses = for
        {
          sourceNeuron <- sourceLayer.cells
          targetNeuron <- targetLayer.cells
        } yield Synapse(sourceNeuron, targetNeuron, 0.33) // TODO: Assign initial weight randomly using Gaussian(0, 1)

        val biasSynapses = for
        {
          targetNeuron <- targetLayer.cells
        } yield Synapse(Bias(0.14, targetLayer), targetNeuron, 1) // TODO: Assign bias randomly using Gaussian(0, 1)

        synapsesSoFar ++ neuronSynapses ++ biasSynapses
      }
    })

    FeedForwardNeuralNet(synapses)
  }
}
