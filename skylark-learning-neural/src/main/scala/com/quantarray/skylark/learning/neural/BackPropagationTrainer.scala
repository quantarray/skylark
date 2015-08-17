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

import com.quantarray.skylark.learning.{SupervisedDataSample, SupervisedDataSet}

/**
 * Back propagation trainer with stochastic gradient descent.
 *
 * Ideas for the trainer were influenced by Michel Nielsen's Neural Networks and Deep Learning.
 * http://neuralnetworksanddeeplearning.com/
 *
 * @author Araik Grigoryan
 */
case class BackPropagationTrainer(learningRate: Double, weightDecay: Double) extends Trainer with BreezeMatrixOps
{
  override def trainAndTest[N <: Net](net: N, numberOfEpochs: Int, batchSize: Int, trainingSet: SupervisedDataSet, testSetIsFit: Option[(SupervisedDataSet, Fitness)] = None)
                                     (implicit cbf: NetCanBuildFrom[N, net.C, net.T, N]): Seq[(N, Option[Double])] =
  {
    val initialBsWs = matrices(net.biases, net.weights)

    val initialAccuracy: Option[Double] = testSetIsFit match
    {
      case Some(tsf) => Some(evaluate(net.activation, initialBsWs, tsf))
      case _ => None
    }

    val finalBsWsAccuracies = (0 until numberOfEpochs).foldLeft(List((initialBsWs, initialAccuracy)))((x, epochIndex) =>
    {
      val bsws = x.head._1
      val accuracies = x.head._2

      val miniBatches = trainingSet.samples.grouped(batchSize)

      val newBsWs = miniBatches.foldLeft(bsws)((bsws, miniBatch) =>
      {
        train(net.activation, net.cost, weightDecay / trainingSet.samples.size, bsws, miniBatch)
      })

      testSetIsFit match
      {
        case Some(tsif) => (newBsWs, Some(evaluate(net.activation, newBsWs, tsif))) :: x
        case _ => (newBsWs, None) :: x
      }
    })

    finalBsWsAccuracies.map(x => (cbf(net, props(x._1._1, x._1._2)).net, x._2)).reverse
  }


  override def test[N <: Net](net: N, testSetIsFit: (SupervisedDataSet, Fitness)): Double =
  {
    evaluate(net.activation, matrices(net.biases, net.weights), testSetIsFit)
  }

  /**
   * Train on one mini-batch of samples and update networks' biases and weights at the end of training.
   */
  private def train(activation: Activation, cost: Cost, scaledWeightDecay: Double, bsws: (Seq[Matrix], Seq[Matrix]),
                    samples: Seq[SupervisedDataSample]): (Seq[Matrix], Seq[Matrix]) =
  {
    val (nablaBs, nablaWs) = samples.foldLeft(zeros(bsws))((nablaBsNablaWs, sample) =>
    {
      val (deltaNablaBs, deltaNablaWs) = train(activation, cost, bsws, sample)
      (nablaBsNablaWs._1.zip(deltaNablaBs).map(m => m._1 + m._2), nablaBsNablaWs._2.zip(deltaNablaWs).map(m => m._1 + m._2.t))
    })

    val biases = bsws._1
    val weights = bsws._2

    val scaledLearningRate = learningRate / samples.size
    val weightDecay = 1.0 - learningRate * scaledWeightDecay

    val newBs = biases.zip(nablaBs).map(bnb => bnb._1 - bnb._2 * scaledLearningRate)
    val newWs = weights.zip(nablaWs).map(wnw => wnw._1 * weightDecay - wnw._2 * scaledLearningRate)

    (newBs, newWs)
  }

  /**
   * Trains on one sample and calculates gradients for biases and weights.
   */
  private def train(activation: Activation, cost: Cost, bsws: (Seq[Matrix], Seq[Matrix]), sample: SupervisedDataSample): (Seq[Matrix], Seq[Matrix]) =
  {
    val biases = bsws._1
    val weights = bsws._2

    // Forward-propagate the input
    val aszs = biases.zip(weights).foldLeft((List(Matrix(sample.input)), List.empty[Matrix]))((aszs, bw) =>
    {
      val as = aszs._1
      val zs = aszs._2

      val b = bw._1
      val w = bw._2

      // m by 1 vector, where m is the number of inputs to the layer
      val a = as.head

      val z = (w.t * a: Matrix) + b

      val newA = activation(z)

      (newA :: as, z :: zs)
    })

    // Backward-propagate errors
    val as = aszs._1
    val zs = aszs._2

    // Activation output
    val a = as.head
    // Target output
    val y = Matrix(sample.target)
    val z = zs.head
    // Delta of the last layer
    val delta = cost.d(z, a, y)

    val nablaB = delta
    val nablaW: Matrix = delta * as.tail.head.t

    val (nablaBs, nablaWs, _) = (as.tail.tail, zs.tail, as.size - 1 to 2 by -1).zipped.foldLeft((List(nablaB), List(nablaW), delta))((nablaBsNablaWsDelta, azLayerIndexes) =>
    {
      val nablaBs = nablaBsNablaWsDelta._1
      val nablaWs = nablaBsNablaWsDelta._2
      val delta = nablaBsNablaWsDelta._3

      val a = azLayerIndexes._1
      val z = azLayerIndexes._2
      val layerIndex = azLayerIndexes._3

      val w = weights(layerIndex - 1)

      val wd: Matrix = w * delta

      val newDelta = wd :* activation.d(z)

      val nablaB = newDelta
      val dx: Matrix = newDelta * a.t
      val nablaW = dx

      (nablaB :: nablaBs, nablaW :: nablaWs, newDelta)
    })

    (nablaBs, nablaWs)
  }

  private def evaluate(activation: Activation, bsws: (Seq[Matrix], Seq[Matrix]), testSetIsFit: (SupervisedDataSet, (Seq[Double], SupervisedDataSample) => Boolean)): Double =
  {
    val testSet = testSetIsFit._1
    val isFit = testSetIsFit._2

    val accuracy = testSet.samples.map(sample => if (isFit(feedForward(activation, bsws, sample.input), sample)) 1.0 else 0.0).sum / testSet.samples.size

    println(s"Accuracy: $accuracy")

    accuracy
  }

  private def feedForward(activation: Activation, bsws: (Seq[Matrix], Seq[Matrix]), input: Seq[Double]): Seq[Double] =
  {
    val biases = bsws._1
    val weights = bsws._2

    // Forward-propagate the input
    val a = biases.zip(weights).foldLeft(Matrix(input))((a, bw) =>
    {
      val b = bw._1
      val w = bw._2

      val z = (w.t * a: Matrix) + b

      activation(z)
    })

    a.toArray
  }
}
