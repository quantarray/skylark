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

/**
 * Scale, representing an abstract notion of choice of measure of a particular dimension, e.g. temperature scale.
 *
 * @author Araik Grigoryan
 */
abstract class Scale[D <: Dimension[D]](val dimension: D)

case object Kelvin extends Scale(Temperature)

case object Centigrade extends Scale(Temperature)

case object Fahrenheit extends Scale(Temperature)
