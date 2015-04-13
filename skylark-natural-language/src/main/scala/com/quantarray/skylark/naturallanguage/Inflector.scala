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
 * Inflector.
 *
 * @author Araik Grigoryan
 */
trait Inflector
{
  /**
   * Gets grammatical number.
   *
   * http://en.wikipedia.org/wiki/Grammatical_number
   */
  def number(word: String, number: Double): String
}

object NoneInflector extends Inflector
{
  def number(word: String) = word

  def number(word: String, number: Double) = word
}

object InflectorImplicits
{

  implicit class Inflectible(val word: String) extends AnyVal
  {
    /**
     * Gets grammatical number.
     *
     * http://en.wikipedia.org/wiki/Grammatical_number
     */
    def number(number: Double)(implicit inflector: Inflector = NoneInflector): String = inflector.number(word, number)
  }

}
