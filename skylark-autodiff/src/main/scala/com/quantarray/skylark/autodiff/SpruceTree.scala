/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2016 Quantarray, LLC
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

package com.quantarray.skylark.autodiff

/**
  * Spruce tree, used for manually constructing the function whose derivative/gradient is desired.
  *
  * @author Araik Grigoryan
  */
trait SpruceTree[Repr <: SpruceTree[Repr]]
{
  self: Repr =>

  def +[T <: SpruceTree[T]](tree: T): Plus[Repr, T] = Plus(this, tree)

  def *[T <: SpruceTree[T]](tree: T): Times[Repr, T] = Times(this, tree)

  def -[T <: SpruceTree[T]](tree: T): Plus[Repr, Times[T, Constant]] = Plus(this, tree * Constant(-1))
}

case class Val(symbol: Symbol = '@) extends SpruceTree[Val]

case class Constant(value: Double) extends SpruceTree[Constant]

case class Plus[T1 <: SpruceTree[T1], T2 <: SpruceTree[T2]](t1: T1, t2: T2) extends SpruceTree[Plus[T1, T2]]

case class Times[T1 <: SpruceTree[T1], T2 <: SpruceTree[T2]](t1: T1, t2: T2) extends SpruceTree[Times[T1, T2]]

case class Exp[T <: SpruceTree[T]](exponent: T) extends SpruceTree[Exp[T]]
