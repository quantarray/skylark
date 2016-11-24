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

@compileTimeOnly("AscribeAnyMeasure annotation can only be used with classes")
private[measure] class AscribeAnyMeasure[T](measuresScope: Any) extends StaticAnnotation
{
  def macroTransform(annottees: Any*): Any = macro AscribeAnyMeasure.macroTransformImpl
}

private[measure] object AscribeAnyMeasure
{
  def macroTransformImpl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] =
  {
    import c.universe._

    val targetTraitTree: Tree = c.prefix.tree match
    {
      case q"new $name[$targetTraitTree](...$paramss)" => targetTraitTree
    }

    val targetTraitTpe: Type = c.typecheck(q"0.asInstanceOf[$targetTraitTree]").tpe

    val measuresScope: List[Tree] = c.prefix.tree match
    {
      case q"new $name[$targetTraitTpe](...$paramss)" if paramss.nonEmpty => paramss.head
      case _ => c.abort(c.enclosingPosition, "AscribeAnyMeasure requires a single constructor parameter pointing to the scope where measures are defined.")
    }

    val measureValTermSymbols: List[TermSymbol] = targetTraitTpe.members.collect
    { case d if d.isTerm => d.asTerm }.filter(t => t.isStable && t.isVal && t.isFinal).toList.reverse

    val measureDefs = measureValTermSymbols.map(
      {
        measureValTermSymbol =>

          val measureIdentifier = TermName(measureValTermSymbol.name.toString.trim)
          val measureTermName = TermName(measureValTermSymbol.name.toString.trim)

          q"""val $measureIdentifier: AnyMeasure = ${measuresScope.head}.$measureTermName"""
      })

    val objectName = annottees.map(_.tree) match
    {
      case List(q"$mods object $tpname extends { ..$earlyDefs } with ..$parents { $self => ..$stats }") => tpname
      case _ => c.abort(c.enclosingPosition, s"AscribeAnyMeasure annotation can only be used with classes.")
    }

    c.Expr(
      q"""
      object $objectName
      {
        ..$measureDefs
      }
        """
    )
  }
}
