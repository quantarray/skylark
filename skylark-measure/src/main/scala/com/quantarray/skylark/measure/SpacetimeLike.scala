/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2015 Quantarray, LLC
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

import org.joda.time.DateTime

/**
 * Spacetime.
 *
 * @author Araik Grigoryan
 */
trait SpacetimeLike
{
  def space: Option[Space]

  def time: Option[DateTime]

  def hasSpace: Boolean = space.isDefined

  def hasTime: Boolean = time.isDefined
}

object HereAndNow extends SpacetimeLike
{
  val space: Option[Space] = Some(Here)

  override def time: Option[DateTime] = Some(DateTime.now)
}

case class Spacetime(space: Option[Space], time: Option[DateTime]) extends SpacetimeLike
{
  override def toString: String = (space, time) match
  {
    case (Some(spc), Some(tme)) => s"$spc at $tme"
    case (Some(spc), _) => s"$spc"
    case (_, Some(tme)) => s"$tme"
    case _ => "Omnipresent"
  }
}

object Spacetime
{
  def apply(space: Space, time: DateTime) = new Spacetime(Some(space), Some(time))

  def apply(time: DateTime) = new Spacetime(None, Some(time))

  object Implicits
  {

    implicit final class SpaceToSpacetime(private val space: Space) extends AnyVal
    {
      def at(time: DateTime) = Spacetime(space, time)

      def on = at _

      def anytime = Spacetime(Some(space), None)
    }

  }

}

