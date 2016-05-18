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
  * Compilation using subtypes of Tree defined in this package.
  *
  * @author Araik Grigoryan
  */
trait TreeCompilation extends Compilation
{
  class Compiler(val tape: Seq[CompiledTerm], val indexes: Map[Tree[_], Int])
  {
    def this(vals: Seq[Val]) = this(vals.map(`val` => compiled.Val(`val`.symbol)), vals.foldLeft(Map.empty[Tree[_], Int])((is, `val`) => is + (`val` -> is.size)))

    def compile[T <: Tree[T]](function: T): Compiler =
    {
      val (_, tape, indexes) = visit(function, List.empty, Map.empty)
      new Compiler(tape.reverse, indexes)
    }

    private def visit[T <: Tree[_]](term: T, tape: List[CompiledTerm], indexes: Map[Tree[_], Int]): (Int, List[CompiledTerm], Map[Tree[_], Int]) =
    {
      def compile(compiledTerm: CompiledTerm, tape: List[CompiledTerm], indexes: Map[Tree[_], Int]): (Int, List[CompiledTerm], Map[Tree[_], Int]) =
      {
        if (indexes.contains(term))
          (indexes(term), tape, indexes)
        else
        {
          val index = tape.size
          val newTape = compiledTerm :: tape
          val newIndexes = indexes + (term -> index)
          (index, newTape, newIndexes)
        }
      }

      term match
      {
        case Val(symbol) => compile(compiled.Val(symbol), tape, indexes)

        case Constant(value) => compile(compiled.Constant(value), tape, indexes)

        case Plus(t1: Tree[_], t2: Tree[_]) =>
          val (i1, tape1, is1) = visit(t1, tape, indexes)
          val (i2, tape2, is2) = visit(t2, tape1, is1)
          compile(compiled.Sum(Edge(i1), Edge(i2)), tape2, is2)

        case Times(t1: Tree[_], t2: Tree[_]) =>
          val (i1, tape1, is1) = visit(t1, tape, indexes)
          val (i2, tape2, is2) = visit(t2, tape1, is1)
          compile(compiled.Product(Edge(i1), Edge(i2)), tape2, is2)

        case Exp(exponent: Tree[_]) =>
          val (i, t, is) = visit(exponent, tape, indexes)
          compile(compiled.Exp(Edge(i)), t, is)
      }
    }
  }

  case class CompiledFunction1[T <: Tree[T]](function: T, `val`: Val) extends CompiledFunction[Double, CompiledFunction1[T]]
  {
    private val compiler = new Compiler(Seq(`val`)).compile(function)

    protected val tape = compiler.tape

    def derivative(point: Double): Double =
    {
      val tape = gradient(Seq(point))
      tape.head.adjoint
    }

    override def apply(point: Double): Double = eval(Seq(point))
  }

  case class CompiledFunction2[T <: Tree[T]](function: T, vals: (Val, Val)) extends CompiledFunction[(Double, Double), CompiledFunction2[T]] with ((Double, Double) => Double)
  {
    private val compiler = new Compiler(Seq(vals._1, vals._2)).compile(function)

    protected val tape = compiler.tape

    def gradient(point: (Double, Double)): (Double, Double) =
    {
      val tape = gradient(Seq(point._1, point._2))
      (tape.head.adjoint, tape(1).adjoint)
    }

    override def apply(point: (Double, Double)): Double = eval(Seq(point._1, point._2))

    override def apply(v1: Double, v2: Double): Double = apply((v1, v2))
  }


}

object TreeCompilation extends TreeCompilation
{

  object Implicits
  {

    implicit final class TermToCompiledFunction[T <: Tree[T]](private val function: T) extends AnyVal
    {
      def compile(`val`: Val): CompiledFunction1[T] = CompiledFunction1(function, `val`)

      def compile(vals: (Val, Val)): CompiledFunction2[T] = CompiledFunction2(function, vals)

      def apply(vals: (Val, Val))(point: (Double, Double)): Double = compile(vals)(point)
    }

  }

}
