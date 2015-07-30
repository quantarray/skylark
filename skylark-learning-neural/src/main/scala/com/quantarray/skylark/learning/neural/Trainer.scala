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
  def trainAndTest[N <: Net](net: N, numberOfEpochs: Int, batchSize: Int, trainingSet: SupervisedDataSet, testSetFit: Option[(SupervisedDataSet, Fitness)])
                            (implicit cbf: NetCanBuildFrom[N, net.C, net.T, N]): N

  def trainAndTest[N <: Net](net: N, numberOfEpochs: Int, batchSize: Int, trainingSet: SupervisedDataSet, testSetFit: (SupervisedDataSet, Fitness))
                            (implicit cbf: NetCanBuildFrom[N, net.C, net.T, N]): N = trainAndTest(net, numberOfEpochs, batchSize, trainingSet, Some(testSetFit))
}
