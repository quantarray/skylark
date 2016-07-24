package com.quantarray.skylark.measure

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object commodity
{
  object us
  {

    import composition._

    object commercial
    {
      object grains
      {
        object corn
        {
          object shelled
          {
            val bushel = "bushel" := 56 * lb
          }
        }

      }
    }
  }
}
