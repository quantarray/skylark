package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure.conversion.SameTypeImplicits
import com.quantarray.skylark.measure.{CanConvert, Converter, MassMeasure}

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object conversion
{

  object default extends SameTypeImplicits
  {

    implicit object MeasureCanConvert extends CanConvert[Measure, Measure]
    {

      override def convert: Converter[Measure, Measure] = new MeasureConverter
      {
        override def convert(from: Measure, to: Measure): Option[Double] = (from, to) match
        {
          case (ProductMeasure(diff1, RatioMeasure(same1, diff2)), same2) if same1 == same2 => diff1.to(diff2)
          case (mm1: MassMeasure, mm2: MassMeasure) => massCanConvert.convert(mm1, mm2)
          case _ => super.convert(from, to)
        }
      }
    }

  }

}
