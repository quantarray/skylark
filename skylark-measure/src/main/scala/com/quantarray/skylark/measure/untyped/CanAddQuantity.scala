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

package com.quantarray.skylark.measure.untyped

import scala.annotation.implicitNotFound

/**
  * Can add quantity type class.
  *
  * @author Araik Grigoryan
  */
@implicitNotFound("Cannot find CanAddQuantity implementation that adds ${A1} and ${A2}.")
trait CanAddQuantity[N, A1 <: Quantity[N], A2 <: Quantity[N]] extends CanAdd[Measure, Measure]
{
  type QR

  def plus(addend1: A1, addend2: A2)(implicit cc: CanConvert[Measure, Measure]): QR
}

object CanAddQuantity
{
  type Aux[N, A1 <: Quantity[N], A2 <: Quantity[N], QR0] = CanAddQuantity[N, A1, A2]
    {
      type QR = QR0
    }
}