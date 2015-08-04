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

import com.quantarray.skylark.learning.{MnistDataProvider, MnistSupervisedDataSample}
import org.scalatest.{FlatSpec, Matchers}

/**
 * MNIST feed-forward net spec.
 *
 * @author Araik Grigoryan
 */
class MnistFeedForwardNetSpec extends FlatSpec with Matchers
{
  "BackPropagationTrainer" should "train and test feed-forward net on MNIST data" in
    {
      // Number of nodes in the hidden layer ≈ √ 784 * 10
      val net = FeedForwardNet(GaussianWeightAssignment, SigmoidActivation, QuadraticCost(SigmoidActivation), 784, 88, 10)

      val trainer = BackPropagationTrainer(0.05, 0.5)

      val trainingDataProvider = new MnistDataProvider("data/mnist/train-images-idx3-ubyte", "data/mnist/train-labels-idx1-ubyte")

      val testDataProvider = new MnistDataProvider("data/mnist/t10k-images-idx3-ubyte", "data/mnist/t10k-labels-idx1-ubyte")

      val testSetFit = (testDataProvider.read.set, MnistSupervisedDataSample.fit _)

      val (trainedNet, correctGuesses) = trainer.trainAndTest(net, 30, 10, trainingDataProvider.read.set, testSetFit)

      val testCorrectGuesses = trainer.test(trainedNet, testSetFit)

      trainingDataProvider.close()
      testDataProvider.close()

      correctGuesses.last should equal(testCorrectGuesses)
    }
}
