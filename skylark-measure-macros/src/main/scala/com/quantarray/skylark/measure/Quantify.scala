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

package com.quantarray.skylark.measure

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

@compileTimeOnly("Quantify annotation can only be used with classes")
class Quantify[T, Q](measuresScope: Any) extends StaticAnnotation
{
  def macroTransform(annottees: Any*): Any = macro Quantify.macroTransformImpl

}

object Quantify
{
  def macroTransformImpl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] =
  {
    import c.universe._

    val targetTrait: Tree = c.prefix.tree match
    {
      case q"new $name[$targetTraitTpe, $quantityTpe](...$paramss)" => targetTraitTpe
    }

    val tpe: Type = c.typecheck(q"0.asInstanceOf[$targetTrait]").tpe

    val measuresScope: List[Tree] = c.prefix.tree match
    {
      case q"new $name[$targetTraitTpe, $quantityTpe](...$paramss)" if paramss.nonEmpty => paramss.head
      case _ => c.abort(c.enclosingPosition, "Quantify requires a single constructor parameter pointing to the scope where measures are defined.")
    }

    val measureValTermSymbols: List[TermSymbol] = tpe.members.collect
    { case d if d.isTerm => d.asTerm }.filter(t => t.isStable && t.isVal && t.isFinal).toList.reverse

    val quantityDefs = measureValTermSymbols.map(
      {
        measureValTermSymbol =>

          val quantityIdentifier = TermName(measureValTermSymbol.name.toString.trim)
          val measureTermName = TermName(measureValTermSymbol.name.toString.trim)

          q"""def $quantityIdentifier = Quantity[Double, ${measureValTermSymbol.typeSignature}](value, ${measuresScope.head}.$measureTermName)"""
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

      case _ => c.abort(c.enclosingPosition, s"Quantify annotation can only be used with classes.")
    }

    c.Expr(
      q"""
      implicit class $className(val value: Double) extends AnyVal
      {
        implicit def qn: QuasiNumeric[Double] = implicitly(QuasiNumeric.doubleQuasiNumeric)

        def *[M <: Measure[M]](measure: M): Quantity[Double, M] = Quantity(value, measure)

        ..$quantityDefs
      }
        """
    )
  }
}