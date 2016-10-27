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

/**
  * Can add quantity type class.
  *
  * @author Araik Grigoryan
  */
trait CanAddQuantity[N, M1 <: Measure[M1], A1 <: Quantity[N, M1], M2 <: Measure[M2], A2 <: Quantity[N, M2], RM <: Measure[RM]] extends CanAdd[M1, M2]
{
  type QR

  def plus(addend1: A1, addend2: A2)(implicit cc1: CanConvert[M1, RM], cc2: CanConvert[M2, RM]): QR
}

object CanAddQuantity
{
  type Aux[N, M1 <: Measure[M1], A1 <: Quantity[N, M1], M2 <: Measure[M2], A2 <: Quantity[N, M2], RM <: Measure[RM], QR0] = CanAddQuantity[N, M1, A1, M2, A2, RM]
    {
      type QR = QR0
    }
}
