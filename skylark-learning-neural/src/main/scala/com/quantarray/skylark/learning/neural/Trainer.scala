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

import com.quantarray.skylark.learning.SupervisedDataSet

/**
 * Trainer.
 *
 * @author Araik Grigoryan
 */
trait Trainer
{
  /**
   * Trains and optionally tests the supplied network.
   *
   * @return A new network with adjusted biases and weights and a collection of representing the number of correct guesses at end of each epoch.
   */
  def trainAndTest[N <: Net](net: N, numberOfEpochs: Int, batchSize: Int, trainingSet: SupervisedDataSet, testSetFit: Option[(SupervisedDataSet, Fitness)])
                            (implicit cbf: NetCanBuildFrom[N, net.C, net.T, N]): (N, Seq[Double])

  /**
   * Trains and tests the supplied network.
   *
   * @return A new network with adjusted biases and weights and a collection of representing the number of correct guesses at end of each epoch.
   */
  def trainAndTest[N <: Net](net: N, numberOfEpochs: Int, batchSize: Int, trainingSet: SupervisedDataSet, testSetFit: (SupervisedDataSet, Fitness))
                            (implicit cbf: NetCanBuildFrom[N, net.C, net.T, N]): (N, Seq[Double]) =
    trainAndTest(net, numberOfEpochs, batchSize, trainingSet, Some(testSetFit))

  /**
   * Tests the supplied network.
   * @return Number of correct guesses.
   */
  def test[N <: Net](net: N, testSetFit: (SupervisedDataSet, Fitness)): Double
}
