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
  * Compilation mix-in trait.
  *
  * @author Araik Grigoryan
  */
trait Compilation
{
  case class Edge(index: Int, weight: Double = 0.0)

  trait CompiledTree
  {
    def inputs: Seq[Edge]

    def value: Double

    var adjoint: Double = 0 // Var to avoid keeping track of egress Edges

    /**
      * Evaluate self given the current values in the tape.
      */
    def apply(tape: Seq[CompiledTree]): CompiledTree

    /**
      * Evaluate derivative of self with respect to inputs.
      */
    def gradient(tape: Seq[CompiledTree]): CompiledTree
  }

  object compiled
  {

    case class Val(symbol: Symbol, value: Double = 0) extends CompiledTree
    {
      final val inputs: Seq[Edge] = Seq.empty

      /**
        * Evaluate self given the current values in the tape.
        */
      override def apply(tape: Seq[CompiledTree]): Val = this

      /**
        * Evaluate derivative of self with respect to inputs.
        */
      override def gradient(tape: Seq[CompiledTree]): Val = this
    }

    case class Constant(value: Double, inputs: Seq[Edge] = Seq.empty) extends CompiledTree
    {
      /**
        * Evaluate self given the current values in the tape.
        */
      override def apply(tape: Seq[CompiledTree]): Constant = this

      /**
        * Evaluate derivative of self with respect to inputs.
        */
      override def gradient(tape: Seq[CompiledTree]): Constant = this
    }

    case class Plus(left: Edge, right: Edge, value: Double = 0) extends CompiledTree
    {
      val inputs = Seq(left, right)

      /**
        * Evaluate self given the current values in the tape.
        */
      override def apply(tape: Seq[CompiledTree]): Plus = copy(value = tape(left.index).value + tape(right.index).value)

      /**
        * Evaluate derivative of self with respect to inputs.
        */
      override def gradient(tape: Seq[CompiledTree]): Plus = apply(tape).copy(left = left.copy(weight = 1), right = right.copy(weight = 1))
    }

    case class Times(left: Edge, right: Edge, value: Double = 0) extends CompiledTree
    {
      val inputs = Seq(left, right)

      /**
        * Evaluate self given the current values in the tape.
        */
      override def apply(tape: Seq[CompiledTree]): Times = copy(value = tape(left.index).value * tape(right.index).value)

      /**
        * Evaluate derivative of self with respect to inputs.
        */
      override def gradient(tape: Seq[CompiledTree]): CompiledTree =
      {
        val lv = tape(left.index).value
        val rv = tape(right.index).value

        apply(tape).copy(left = left.copy(weight = rv), right = right.copy(weight = lv))
      }
    }

    case class Exp(input: Edge, value: Double = 0) extends CompiledTree
    {
      val inputs = Seq(input)

      /**
        * Evaluate self given the current values in the tape.
        */
      override def apply(tape: Seq[CompiledTree]): Exp = copy(value = math.exp(tape(input.index).value))

      /**
        * Evaluate derivative of self with respect to inputs.
        */
      override def gradient(tape: Seq[CompiledTree]): Exp =
      {
        val value = apply(tape)
        value.copy(input.copy(weight = value.value))
      }
    }

  }

  trait CompiledFunction[P, C <: CompiledFunction[P, C]] extends ((P) => Double)
  {
    self: C =>

    protected def tape: Seq[CompiledTree]

    /**
      * Computes value.
      */
    def eval(point: Seq[Double]): Double =
    {
      val evalTape = tape.zipWithIndex.foldLeft(List.empty[CompiledTree])((evalTape, cti) =>
      {
        val ct = cti match
        {
          case (v@compiled.Val(_, _), i) if i < point.size => v.copy(value = point(i))
          case (x, _) => x(evalTape.reverse)
        }

        ct :: evalTape
      })

      evalTape.head.value
    }

    /**
      * Computes gradient.
      */
    def gradient(point: Seq[Double]): Seq[CompiledTree] =
    {
      def forwardSweep(tape: Seq[CompiledTree]): Seq[CompiledTree] =
      {
        // Traverse in the order of Vals up to top-most
        val forwardSweepTape = tape.zipWithIndex.foldLeft(List.empty[CompiledTree])((forwardSweepTape, cti) =>
        {
          val ct = cti match
          {
            case (v@compiled.Val(_, _), i) => v.copy(value = point(i))
            case (x, _) => x.gradient(forwardSweepTape.reverse)
          }

          ct :: forwardSweepTape
        })

        forwardSweepTape.reverse
      }

      def reverseSweep(forwardSweepTape: Seq[CompiledTree]): Seq[CompiledTree] =
      {
        val reverseForwardSweepTape = forwardSweepTape.reverse

        // Traverse in the order of top-most down to Vals
        val reverseSweepTape = reverseForwardSweepTape.zipWithIndex.foldLeft(List.empty[CompiledTree])((reverseSweepTape, cti) =>
        {
          val ct = cti match
          {
            case (v@compiled.Val(_, _), _) => v
            case (x, i) =>

              if (i == 0)
                x.adjoint = 1

              for (input <- x.inputs)
              {
                forwardSweepTape(input.index).adjoint += x.adjoint * input.weight
              }

              x
          }

          ct :: reverseSweepTape
        })

        reverseSweepTape
      }

      reverseSweep(forwardSweep(tape))
    }
  }
}
