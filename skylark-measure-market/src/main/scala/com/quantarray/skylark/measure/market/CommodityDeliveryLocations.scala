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

package com.quantarray.skylark.measure.market

import com.quantarray.skylark.measure.Space

/**
 * Commodity delivery locations.
 *
 * @author Araik Grigoryan
 */
trait CommodityDeliveryLocations
{

  object US
  {

    object Oklahoma
    {

      object Cushing extends Space
      {
        override def toString: String = "Cushing, OK"
      }

    }

  }

  object Scotland
  {

    object SullomVoe extends Space
    {
      override def toString: String = s"Sullom Voe, Scotland"
    }

  }

}
