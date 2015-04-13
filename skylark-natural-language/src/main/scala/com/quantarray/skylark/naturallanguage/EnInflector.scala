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

package com.quantarray.skylark.naturallanguage

/**
 * Inflector for en locale.
 *
 * @author Araik Grigoryan
 */
object EnInflector extends Inflector
{
  def plural(word: String) =
    word match
    {
      case "foot" => "feet"
      case "Foot" => "Feet"
      case _ => s"${word}s"
    }

  /**
   * Gets grammatical number.
   *
   * http://en.wikipedia.org/wiki/Grammatical_number
   */
  def number(word: String, count: Double) =
    count match
    {
      case 1.0 => word
      case _ => plural(word)
    }

  object Implicits
  {

    implicit class Inflectible(val word: String) extends AnyVal
    {
      /**
       * Gets grammatical number.
       *
       * http://en.wikipedia.org/wiki/Grammatical_number
       */
      def number(number: Double)(implicit inflector: Inflector = EnInflector): String = inflector.number(word, number)
    }

  }

}
