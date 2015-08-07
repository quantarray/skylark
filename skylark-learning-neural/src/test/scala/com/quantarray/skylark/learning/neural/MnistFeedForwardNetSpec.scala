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
      // Load training and test data
      val trainingDataProvider = new MnistDataProvider("data/mnist/train-images-idx3-ubyte", "data/mnist/train-labels-idx1-ubyte")

      val testDataProvider = new MnistDataProvider("data/mnist/t10k-images-idx3-ubyte", "data/mnist/t10k-labels-idx1-ubyte")

      val testSetFit = (testDataProvider.read.set, MnistSupervisedDataSample.fit _)

      // Number of nodes in the hidden layer ≈ √ (784 * 10)
      val net = FeedForwardNet(GaussianWeightAssignment, SigmoidActivation, CrossEntropyCost, 784, 88, 10)

      // Train the network
      val trainer = BackPropagationTrainer(learningRate = 0.005, weightDecay = 0.5)

      val numberOfEpochs = 30
      val miniBatchSize = 10

      // First accuracy is the one of the untrained (random weights) network, second should be ≈ 90%; subsequent accuracies will improve
      val trainedNets = trainer.trainAndTest(net, numberOfEpochs, miniBatchSize, trainingDataProvider.read.set, testSetFit)

      val accuracy = trainer.test(trainedNets.last._1, testSetFit)

      trainingDataProvider.close()
      testDataProvider.close()

      trainedNets.last._2.get should equal(accuracy)
    }
}
