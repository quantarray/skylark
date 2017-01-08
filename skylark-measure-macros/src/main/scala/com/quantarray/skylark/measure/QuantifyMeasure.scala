/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2017 Quantarray, LLC
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

package com.quantarray.skylark.measure

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

@compileTimeOnly("QuantifyMeasure annotation can only be used with classes")
private[measure] class QuantifyMeasure[T, Q](measuresScope: Any) extends StaticAnnotation
{
  def macroTransform(annottees: Any*): Any = macro QuantifyMeasure.macroTransformImpl

}

private[measure] object QuantifyMeasure
{
  def macroTransformImpl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] =
  {
    import c.universe._

    val targetTraitTree: Tree = c.prefix.tree match
    {
      case q"new $name[$targetTraitTree, $quantityTree](...$paramss)" => targetTraitTree
    }

    val targetTraitTpe: Type = c.typecheck(q"0.asInstanceOf[$targetTraitTree]").tpe

    val quantityTree: Tree = c.prefix.tree match
    {
      case q"new $name[$targetTraitTree, $quantityTree](...$paramss)" => quantityTree
    }

    val isAnyMeasure = quantityTree match
    {
      case tq"Quantity[$numericType, AnyMeasure]" => true
      case _ => false
    }

    val measuresScope: List[Tree] = c.prefix.tree match
    {
      case q"new $name[$targetTraitTpe, $quantityTpe](...$paramss)" if paramss.nonEmpty => paramss.head
      case _ => c.abort(c.enclosingPosition, "QuantifyMeasure requires a single constructor parameter pointing to the scope where measures are defined.")
    }

    val measureValTermSymbols: List[TermSymbol] = targetTraitTpe.members.collect
    { case d if d.isTerm => d.asTerm }.filter(t => t.isStable && t.isVal && t.isFinal).toList.reverse

    val quantityDefs = measureValTermSymbols.map(
      {
        measureValTermSymbol =>

          val quantityIdentifier = TermName(measureValTermSymbol.name.toString.trim)
          val measureTermName = TermName(measureValTermSymbol.name.toString.trim)

          val quantityTpeIdentifier =
            if (isAnyMeasure)
              q"""Quantity[Double, AnyMeasure]"""
            else
              q"""Quantity[Double, ${measureValTermSymbol.typeSignature}]"""

          q"""def $quantityIdentifier = $quantityTpeIdentifier(value, ${measuresScope.head}.$measureTermName)"""
      })

    val className = annottees.map(_.tree) match
    {
      case List(q"$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlyDefs } with ..$parents { $self => ..$stats }") =>

        paramss match
        {
          case List(List(q"$paramAccessor val value: $valueType")) =>
          case _ => c.abort(c.enclosingPosition, s"$tpname must have a single parameter named 'value'.")
        }

        tpname

      case _ => c.abort(c.enclosingPosition, s"QuantifyMeasure annotation can only be used with classes.")
    }

    if (isAnyMeasure)
    {
      c.Expr(
        q"""
      implicit class $className(val value: Double) extends AnyVal
      {
        implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)

        def apply(measure: AnyMeasure): Quantity[Double, AnyMeasure] = Quantity(value, measure)

        def *(measure: AnyMeasure): Quantity[Double, AnyMeasure] = apply(measure)

        ..$quantityDefs
      }
        """
      )
    }
    else
    {
      c.Expr(
        q"""
      implicit class $className(val value: Double) extends AnyVal
      {
        implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)

        def apply[M <: Measure[M]](measure: M): Quantity[Double, M] = Quantity(value, measure)

        def *[M <: Measure[M]](measure: M): Quantity[Double, M] = apply(measure)

        ..$quantityDefs
      }
        """
      )
    }
  }
}