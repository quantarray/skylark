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

package com.quantarray.skylark.measure.market

import com.quantarray.skylark.measure._

/**
 * Market.
 *
 * @author Araik Grigoryan
 */
trait Market //[Repr <: Market] TODO
{
  type M = MarketManifold[_ <: Measure, _]

  lazy val signals = manifolds.foldLeft(Map.empty[Signal[_ <: Measure], M])((signalMapSoFar, manifold) => signalMapSoFar + (manifold.signal -> manifold))

  /**
   * Gets the parent.
   */
  def parent: Option[Market]

  /**
   * Gets all manifolds in this market.
   */
  def manifolds: Seq[M]

  /**
   * Builds a new market of this type.
   */
  def build(manifolds: Seq[M]): Market

  /**
   * Gets manifold for the signal.
   */
  def manifold(signal: Signal[_ <: Measure]) = signals.get(signal)

  /**
   * Creates a child of this instance, populated with curves.
   */
  def child(signals: List[Signal[_ <: Measure]]): Market = build(manifolds)
}
