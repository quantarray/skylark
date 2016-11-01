package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure.conversion.SameTypeImplicits
import com.quantarray.skylark.measure._

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
        override def convert(from: Measure, to: Measure): Option[Double] = Conversion(from, to) match
        {
          case (dm1: DimensionlessMeasure) ⤇ (dm2: DimensionlessMeasure) => dimensionlessCanConvert.convert(dm1, dm2)
          case (diff1 * (same1 / diff2)) ⤇ same2 if same1 == same2 => diff1.to(diff2)
          case (mm1: MassMeasure) ⤇ (mm2: MassMeasure) => massCanConvert.convert(mm1, mm2)
          case _ => super.convert(from, to)
        }
      }
    }

  }

}
