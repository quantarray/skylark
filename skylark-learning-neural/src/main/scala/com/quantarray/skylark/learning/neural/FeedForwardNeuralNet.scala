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
case class FeedForwardNeuralNet(connections: Seq[Synapse]) extends NeuralNet[Neuron, Nucleus, Synapse]
{
}

object FeedForwardNeuralNet
{
  /**
   * Connects layers in feed-forward fashion.
   */
  def apply(activation: Activation, numberOfNeuronsInLayer1: Int, numberOfNeuronsInLayer2: Int, numberOfNeuronsInLayer3toN: Int*): FeedForwardNeuralNet =
  {
    // TODO: Create layers, neurons, and synapses
    FeedForwardNeuralNet(Seq.empty[Synapse])
  }
}
