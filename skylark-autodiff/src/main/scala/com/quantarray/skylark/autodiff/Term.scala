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
  * Term.
  *
  * @author Araik Grigoryan
  */
trait Term[Repr <: Term[Repr]]
{
  self: Repr =>

  def +[T <: Term[T]](term: T): Sum[Repr, T] = Sum(this, term)

  def *[T <: Term[T]](term: T): Product[Repr, T] = Product(this, term)

  def -[T <: Term[T]](term: T): Sum[Repr, Product[T, Constant]] = Sum(this, term * Constant(-1))
}

case class Val(symbol: Symbol = '@) extends Term[Val]

case class Constant(value: Double) extends Term[Constant]

case class Sum[T1 <: Term[T1], T2 <: Term[T2]](t1: T1, t2: T2) extends Term[Sum[T1, T2]]

case class Product[T1 <: Term[T1], T2 <: Term[T2]](t1: T1, t2: T2) extends Term[Product[T1, T2]]

case class Exp[T <: Term[T]](exponent: T) extends Term[Exp[T]]
