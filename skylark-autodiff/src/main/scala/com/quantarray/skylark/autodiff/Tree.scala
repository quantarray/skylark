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
  * Tree.
  *
  * @author Araik Grigoryan
  */
trait Tree[Repr <: Tree[Repr]]
{
  self: Repr =>

  def +[T <: Tree[T]](tree: T): Plus[Repr, T] = Plus(this, tree)

  def *[T <: Tree[T]](tree: T): Times[Repr, T] = Times(this, tree)

  def -[T <: Tree[T]](tree: T): Plus[Repr, Times[T, Constant]] = Plus(this, tree * Constant(-1))
}

case class Val(symbol: Symbol = '@) extends Tree[Val]

case class Constant(value: Double) extends Tree[Constant]

case class Plus[T1 <: Tree[T1], T2 <: Tree[T2]](t1: T1, t2: T2) extends Tree[Plus[T1, T2]]

case class Times[T1 <: Tree[T1], T2 <: Tree[T2]](t1: T1, t2: T2) extends Tree[Times[T1, T2]]

case class Exp[T <: Tree[T]](exponent: T) extends Tree[Exp[T]]
