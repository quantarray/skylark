package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure.{CanConvert, Converter}

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object conversion
{

  object default
  {

    implicit object MeasureCanConvert extends CanConvert[Measure, Measure]
    {

      override def convert: Converter[Measure, Measure] = new MeasureConverter
      {
        override def apply(from: Measure, to: Measure): Option[Double] = (from, to) match
        {
          case (ProductMeasure(diff1, RatioMeasure(same1, diff2)), same2) if same1 == same2 => diff1.to(diff2)
          case _ => super.apply(from, to)
        }
      }
    }

  }

}
