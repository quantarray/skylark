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
 * Regime.
 *
 * @author Araik Grigoryan
 */
trait Regime
{
  def activation: Activation

  def cost: Cost
}

/**
 * Cross-entropy regime with Softmax activation is a natural pairing.
 *
 * On The Pairing Of The Softmax Activation And Cross-Entropy Penalty Functions And The Derivation Of The Softmax Activation Function
 * by Dunne Campbell , R. A. Dunne , N. A. Campbell
 * http://citeseerx.ist.psu.edu/viewdoc/versions?doi=10.1.1.49.6403
 */
object SoftmaxCrossEntropyRegime extends Regime
{
  val activation = SoftmaxActivation

  val cost = CrossEntropyCost
}

object SigmoidCrossEntropyRegime extends Regime
{
  val activation = SigmoidActivation

  val cost = CrossEntropyCost
}