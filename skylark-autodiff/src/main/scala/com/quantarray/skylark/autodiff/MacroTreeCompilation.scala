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
import scala.reflect.macros.blackbox

/**
  * Compilation using blackbox Trees defined by Scala macros.
  *
  * @author Araik Grigoryan
  */
trait MacroTreeCompilation extends Compilation
{

  class Compiler[T](c: blackbox.Context)
  {

    import c.universe._

    val tape = mutable.Buffer[CompiledTree]()

    private val indexes = mutable.Map[c.Tree, Int]()

    private val vals = mutable.Map[String, c.Tree]()

    def compile(function: T): Compiler[T] =
    {
      function match
      {
        case Function(vparams, body) =>

          // Compile all Vals first to ensure that they are first on tape, in pre-order ordering
          visitVals(body)
          visit(body)

          this

        case _ => this
      }
    }

    private def compile(tree: c.Tree, compiledTerm: CompiledTree): Int =
    {
      if (indexes.contains(tree))
        indexes(tree)
      else
      {
        val index = tape.size
        tape += compiledTerm
        indexes += (tree -> index)
        index
      }
    }

    private def visitVals(tree: c.Tree): Unit =
    {
      tree match
      {
        case Apply(fun, args) =>

          // TODO: Abort compilation if more than 1 arg

          fun match
          {
            case Select(qual, name) =>

              visitVals(qual)
              args.foreach(arg => visitVals(arg))
          }
        case Ident(TermName(name)) =>

          if (!vals.contains(name))
          {
            compile(tree, compiled.Val(name, 0.0))
            vals += (name -> tree)
          }

        case _ =>
      }
    }

    private def visit(tree: c.Tree): Unit =
    {
      import c.universe._

      tree match
      {
        case Apply(fun, args) =>

          fun match
          {
            case Select(qual, name) =>

              visit(qual)
              args.foreach(arg => visit(arg))

              val qualIndex =
                if (vals.contains(qual.toString))
                  indexes(vals(qual.toString))
                else if (indexes.contains(qual))
                  indexes(qual)
                else if (qual.toString == "scala.math.`package`")
                  -1 // TODO: Move qualIndex processing into cases that expect 2 arguments
                else
                  c.abort(c.enclosingPosition, s"Cannot compile qualifier $qual.")

              val argIndex =
                if (vals.contains(args.head.toString))
                  indexes(vals(args.head.toString))
                else if (indexes.contains(args.head))
                  indexes(args.head)
                else
                  c.abort(c.enclosingPosition, s"Cannot compile argument $args.")

              name match
              {
                case TermName("$plus") => compile(tree, compiled.Plus(Edge(qualIndex), Edge(argIndex)))
                case TermName("$minus") => compile(tree, compiled.Minus(Edge(qualIndex), Edge(argIndex)))
                case TermName("$times") => compile(tree, compiled.Times(Edge(qualIndex), Edge(argIndex)))
                case TermName("exp") => compile(tree, compiled.Exp(Edge(argIndex)))
                case TermName("sin") => compile(tree, compiled.Sin(Edge(argIndex)))
                case _ => c.abort(c.enclosingPosition, s"Derivative/gradient function for '$name' is unknown.")
              }
            case _ =>
          }

        case Ident(TermName(name)) =>

          if (!vals.contains(name))
          {
            compile(tree, compiled.Val(name, 0.0))
            vals += (name -> tree)
          }

        case Literal(Constant(value: Double)) => compile(tree, compiled.Constant(value))

        case Literal(Constant(value: Int)) => compile(tree, compiled.Constant(value))

        case _ =>
      }
    }
  }

  case class CompiledFunction1(tape: Seq[CompiledTree]) extends CompiledFunction[Double, CompiledFunction1] with (Double => Double)
  {
    protected val arity: Int = 1

    def derivative(point: Double): Double =
    {
      val tape = gradient(Seq(point))
      tape.head.adjoint
    }

    override def apply(point: Double): Double = eval(Seq(point))
  }

  case class CompiledFunction2[T](c: blackbox.Context, function: T) extends CompiledFunction[Double, CompiledFunction2[T]] with ((Double, Double) => Double)
  {
    val compiler = new Compiler[T](c)

    val tape = compiler.compile(function).tape

    protected val arity: Int = 2

    def gradient(point1: Double, point2: Double): (Double, Double) =
    {
      val tape = gradient(Seq(point1, point2))
      (tape.head.adjoint, tape(1).adjoint)
    }

    def apply(point: (Double, Double)): Double = eval(Seq(point._1, point._2))

    override def apply(v1: Double, v2: Double): Double = apply((v1, v2))
  }

}
