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

import com.quantarray.skylark.measure.{Measure, Quantity}

/**
 * Market manifold.
 *
 * @author Araik Grigoryan
 */
trait MarketManifold[M <: Measure, K]
{
  type S <: Signal[M]

  def measure: M

  def signal: S

  /**
   * Gets value portion of quantity at key.
   */
  def value(key: K): Option[Double]

  /**
   * Gets quantity at key.
   */
  def get(key: K): Option[Quantity[M]] = value(key).fold[Option[Quantity[M]]](None)(value => Some(Quantity[M](value, measure)))
}

case class ValueTransformativeMarketManifold[M <: Measure, K](manifold: MarketManifold[M, K], f: (K, Option[Double]) => Option[Double]) extends MarketManifold[M, K]
{
  type S = manifold.S

  val measure: M = manifold.measure

  val signal: S = manifold.signal

  override def value(key: K): Option[Double] = f(key, manifold.value(key))
}