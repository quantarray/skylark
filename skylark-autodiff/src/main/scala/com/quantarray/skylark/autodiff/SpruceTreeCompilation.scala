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

import scala.collection.mutable

/**
  * Compilation using subtypes of Tree defined in this package.
  *
  * @author Araik Grigoryan
  */
trait SpruceTreeCompilation extends Compilation
{

  /**
    * Compiler.
    *
    * Requires that Vals get compiled ahead of the function to ensure that they appear at the front of the tape.
    */
  class Compiler(vals: Seq[Val])
  {
    val tape = mutable.Buffer[CompiledTree]()

    private val indexes = mutable.Map[SpruceTree[_], Int]()

    vals.foreach(`val` => compile(`val`))

    def compile[T <: SpruceTree[T]](tree: T): Compiler =
    {
      visit(tree)
      this
    }

    private def visit[T <: SpruceTree[_]](term: T): Int =
    {
      def compile(compiledTerm: CompiledTree): Int =
      {
        if (indexes.contains(term))
          indexes(term)
        else
        {
          val index = tape.size
          tape += compiledTerm
          indexes += (term -> index)
          index
        }
      }

      term match
      {
        case Val(symbol) => compile(compiled.Val(symbol))

        case Constant(value) => compile(compiled.Constant(value))

        case Plus(t1: SpruceTree[_], t2: SpruceTree[_]) =>
          val i1 = visit(t1)
          val i2 = visit(t2)
          compile(compiled.Plus(Edge(i1), Edge(i2)))

        case Minus(t1: SpruceTree[_], t2: SpruceTree[_]) =>
          val i1 = visit(t1)
          val i2 = visit(t2)
          compile(compiled.Minus(Edge(i1), Edge(i2)))

        case Times(t1: SpruceTree[_], t2: SpruceTree[_]) =>
          val i1 = visit(t1)
          val i2 = visit(t2)
          compile(compiled.Times(Edge(i1), Edge(i2)))

        case Exp(exponent: SpruceTree[_]) => compile(compiled.Exp(Edge(visit(exponent))))

        case Sin(t: SpruceTree[_]) => compile(compiled.Sin(Edge(visit(t))))
      }
    }
  }

  case class CompiledFunction0[T <: SpruceTree[T]](function: T) extends CompiledFunction[Nothing, CompiledFunction0[T]] with (() => Double)
  {
    private val compiler = new Compiler(Seq.empty).compile(function)

    protected val tape = compiler.tape

    protected val arity: Int = 0

    def derivative(): Double =
    {
      val tape = gradient(Seq.empty)
      tape.head.adjoint
    }

    override def apply(): Double = eval(Seq.empty)
  }

  case class CompiledFunction1[T <: SpruceTree[T]](function: T, `val`: Val) extends CompiledFunction[Double, CompiledFunction1[T]] with (Double => Double)
  {
    private val compiler = new Compiler(Seq(`val`)).compile(function)

    protected val tape = compiler.tape

    protected val arity: Int = 1

    def derivative(point: Double): Double =
    {
      val tape = gradient(Seq(point))
      tape.head.adjoint
    }

    override def apply(point: Double): Double =
      eval(Seq(point))
  }

  case class CompiledFunction2[T <: SpruceTree[T]](function: T, vals: (Val, Val)) extends CompiledFunction[(Double, Double), CompiledFunction2[T]] with (
    (Double, Double) => Double)
  {
    private val compiler = new Compiler(Seq(vals._1, vals._2)).compile(function)

    protected val tape = compiler.tape

    protected val arity: Int = 2

    def gradient(point: (Double, Double)): (Double, Double) =
    {
      val tape = gradient(Seq(point._1, point._2))
      (tape.head.adjoint, tape(1).adjoint)
    }

    def apply(point: (Double, Double)): Double = eval(Seq(point._1, point._2))

    override def apply(v1: Double, v2: Double): Double = apply((v1, v2))
  }


}

object SpruceTreeCompilation extends SpruceTreeCompilation
{

  object Implicits
  {

    implicit final class SpruceTreeToCompiledFunction[T <: SpruceTree[T]](private val function: T) extends AnyVal
    {
      // TODO: Consider having compile{1...N}, with Vals collected from the function itself, instead of redundantly supplied

      def compile(): CompiledFunction0[T] = CompiledFunction0(function)

      def compile(`val`: Val): CompiledFunction1[T] = CompiledFunction1(function, `val`)

      def compile(vals: (Val, Val)): CompiledFunction2[T] = CompiledFunction2(function, vals)

      def apply(vals: (Val, Val))(point: (Double, Double)): Double = compile(vals)(point)
    }

  }

}
