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

  lazy val layerSourceGroups = connections.groupBy(_.source.layer).map((ls) => (ls._1, ls._2.groupBy(_.source)))

  lazy val layerTargetGroups = connections.groupBy(_.target.layer).map((ls) => (ls._1, ls._2.groupBy(_.target)))

  /**
   * Creates a map of weights, in order or layer and source neuron index.
   */
  def weightsBySource(select: Synapse => Boolean): NeuralNetWeightMap = weightsBy(layerSourceGroups, select)

  /**
   * Creates a map of weights, in order or layer and target neuron index.
   */
  def weightsByTarget(select: Synapse => Boolean): NeuralNetWeightMap = weightsBy(layerTargetGroups, select)

  private def weightsBy(groups: Map[Nucleus, Map[Neuron, Seq[Synapse]]], select: Synapse => Boolean): NeuralNetWeightMap =
  {
    groups.foldLeft(NeuralNetWeightMap.empty)((m, x) =>
    {
      val lss = x._2.foldLeft(NeuralLayerWeightMap.empty)((n, y) =>
      {
        val weights = y._2.filter(select).map(_.weight)
        if (weights.isEmpty) n else n + (y._1.index -> weights)
      })
      if (lss.isEmpty) m else m + (x._1.index -> lss)
    })
  }
}

object FeedForwardNeuralNet
{
  /**
   * Connects layers in feed-forward fashion.
   *
   * In addition to the requested neurons, a bias cell will be created for each layer. By convention,
   * the zeroth layer will not receive a bias cell because it will directly absorb the inputs.
   */
  def apply(activation: NeuralActivation, numberOfNeuronsInLayer0: Int, numberOfNeuronsInLayer1: Int, numberOfNeuronsInLayer2AndUp: Int*): FeedForwardNeuralNet =
  {
    val layer0 = Nucleus(0, activation, numberOfNeuronsInLayer0)

    val layer1 = Nucleus(1, activation, numberOfNeuronsInLayer1)

    val layers2AndUp = numberOfNeuronsInLayer2AndUp.zipWithIndex.map(x => Nucleus(x._2 + 2, activation, x._1))

    val layers = layer0 +: layer1 +: layers2AndUp

    val synapses = layers.zipWithIndex.foldLeft(List.empty[Synapse])((synapsesSoFar, layerIndex) =>
    {
      if (layerIndex._1 == layers.last)
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
        } yield Synapse(sourceNeuron, targetNeuron, 10 * sourceNeuron.index + targetNeuron.index) // TODO: Assign initial weight randomly using Gaussian(0, 1)

        val biasSynapses =

          for
          {
            targetNeuron <- targetLayer.cells
          } yield Synapse(Neuron(0, IdentityActivation, targetLayer), targetNeuron, 0.1 * targetNeuron.index) // TODO: Assign bias randomly using Gaussian(0, 1)

        synapsesSoFar ++ neuronSynapses ++ biasSynapses
      }
    })

    FeedForwardNeuralNet(synapses)
  }
}
