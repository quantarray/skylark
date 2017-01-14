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

import scala.annotation.implicitNotFound
import scala.language.higherKinds

/**
  * Can add quantity type class.
  *
  * @author Araik Grigoryan
  */
@implicitNotFound("Cannot find CanAddQuantity implementation that adds ${Q1} and ${Q2}.")
trait CanAddQuantity[N, M1, Q1[_, _], M2, Q2, RM]
{
  type QR

  def plus(addend1: Q1[N, M1], addend2: Q2)(implicit cc1: CanConvert[M1, RM], cc2: CanConvert[M2, RM]): QR
}

