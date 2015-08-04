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

import scala.language.reflectiveCalls

/**
 * Feed-forward net.
 *
 * The head of the layers is considered the input layer.
 *
 * @author Araik Grigoryan
 */
case class FeedForwardNet(activation: Activation, cost: Cost, connections: Seq[Synapse]) extends Net
{
  type C = Neuron

  type L = Nucleus

  type T = Synapse

  lazy val sourceLayerGroups = connections.groupBy(_.source.layer).map((lc) => (lc._1, lc._2.groupBy(_.source)))

  lazy val targetLayerGroups = connections.groupBy(_.target.layer).map((lc) => (lc._1, lc._2.groupBy(_.target)))

  /**
   * Creates a map of weights, in order of layer and target neuron index.
   */
  lazy val biases: Biases = props(targetLayerGroups, _.source.isBias, _.weight)

  /**
   * Creates a map of weights, in order of layer and source neuron index.
   */
  lazy val weights: Weights = props(sourceLayerGroups, _.source.nonBias, _.weight)
}

object FeedForwardNet
{

  case class FromScratchBuilder(weight: WeightAssignment, activation: Activation, cost: Cost,
                                neuronsInLayer0: Int, neuronsInLayer1: Int, neuronsInLayer2AndUp: Int*) extends NetBuilder[Neuron, Synapse, FeedForwardNet]
  {
    val layer0 = Nucleus(0, neuronsInLayer0)

    val layer1 = Nucleus(1, neuronsInLayer1)

    val layers2AndUp = neuronsInLayer2AndUp.zipWithIndex.map(x => Nucleus(x._2 + 2, x._1))

    val layers = layer0 +: layer1 +: layers2AndUp

    val synapses = layers.zipWithIndex.foldLeft(List.empty[Synapse])((synapses, layerIndex) =>
    {
      if (layerIndex._1 == layers.last)
      {
        synapses
      }
      else
      {
        val sourceLayer = layerIndex._1
        val targetLayer = layers(layerIndex._2 + 1)

        val biasSynapses =
          targetLayer.cells.map(target => (Neuron(0, targetLayer, isBias = true), target)).map(st => Synapse(st._1, st._2, weight(st._1, st._2)))

        val weightSynapses = for
        {
          source <- sourceLayer.cells
          target <- targetLayer.cells
        } yield Synapse(source, target, weight(source, target))

        synapses ++ biasSynapses ++ weightSynapses
      }
    })

    override def net: FeedForwardNet = FeedForwardNet(activation, cost, synapses)
  }

  case class FromBiasesAndWeightsBuilder(activation: Activation, cost: Cost, biases: Biases, weights: Weights) extends NetBuilder[Neuron, Synapse, FeedForwardNet]
  {
    val layers = weights.map(kv => Nucleus(kv._1, kv._2.size)).toSeq :+ Nucleus(weights.size, biases.last._2.size)

    val synapses = layers.zipWithIndex.foldLeft(List.empty[Synapse])((synapses, layerIndex) =>
    {
      if (layerIndex._1 == layers.last)
      {
        synapses
      }
      else
      {
        val sourceLayer = layerIndex._1
        val targetLayer = layers(layerIndex._2 + 1)

        val biasSynapses =
          targetLayer.cells.map(target => (Neuron(0, targetLayer, isBias = true), target)).map(st =>
            Synapse(st._1, st._2, biases(targetLayer.index)(st._2.index).head))

        val weightSynapses = for
        {
          source <- sourceLayer.cells
          target <- targetLayer.cells
        } yield Synapse(source, target, weights(sourceLayer.index)(source.index)(target.index - 1))

        synapses ++ biasSynapses ++ weightSynapses
      }
    })

    override def net: FeedForwardNet = FeedForwardNet(activation, cost, synapses)
  }

  implicit val canBuildFrom = new NetCanBuildFrom[FeedForwardNet, Neuron, Synapse, FeedForwardNet]
  {
    /**
     * Creates a new builder on request of a net.
     */
    override def apply(from: FeedForwardNet, biasesWeights: (Biases, Weights)) =
    {
      FromBiasesAndWeightsBuilder(from.activation, from.cost, biasesWeights._1, biasesWeights._2)
    }

    /**
     * Creates a new builder from scratch.
     */
    override def apply(weightAssignment: WeightAssignment, activation: Activation, cost: Cost,
                       neuronsInLayer0: Int, neuronsInLayer1: Int, neuronsInLayer2AndUp: Int*) =
    {
      FromScratchBuilder(weightAssignment, activation, cost, neuronsInLayer0, neuronsInLayer1, neuronsInLayer2AndUp: _*)
    }
  }

  /**
   * Connects layers in feed-forward fashion.
   *
   * In addition to the requested neurons, a bias cell will be created for each layer. By convention,
   * the zeroth layer will not receive a bias cell because it will directly absorb the inputs.
   */
  def apply(weightAssignment: WeightAssignment, activation: Activation, cost: Cost, neuronsInLayer0: Int, neuronsInLayer1: Int, neuronsInLayer2AndUp: Int*): FeedForwardNet =
  {
    canBuildFrom(weightAssignment, activation, cost, neuronsInLayer0, neuronsInLayer1, neuronsInLayer2AndUp: _*).net
  }
}
