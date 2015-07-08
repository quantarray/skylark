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

import org.scalatest.{FlatSpec, Matchers}

/**
 * Feed-forward Neural net spec.
 *
 * @author Araik Grigoryan
 */
class FeedForwardNeuralNetSpec extends FlatSpec with Matchers
{
  "" should "" in
    {

      val net = FeedForwardNeuralNet(SigmoidActivation, 4, 3, 2)

      net.connections.size should be((4 + 1) * 3 + (3 + 1) * 2) // +1s are to account for the Biases

      val dataSet = new SupervisedDataSet
      {
        override def samples: Seq[SupervisedDataSample] =
          Seq(
            new SupervisedDataSample
            {
              override def input: Seq[Double] = Seq(1, 1, 0, 0)

              override def target: Seq[Double] = Seq(1, 0, 1)
            }
          )
      }

      val trainer = BackPropagationNeuralTrainer(100, 0.5, 0.5)

      trainer.train(net, dataSet)
    }
}
