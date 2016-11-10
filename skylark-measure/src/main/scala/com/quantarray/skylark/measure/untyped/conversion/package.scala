package com.quantarray.skylark.measure.untyped

import com.quantarray.skylark.measure.conversion.DefaultConversionImplicits
import com.quantarray.skylark.measure._

/*
 * Skylark
 *
 * © 2012-2016, Quantarray
 * http://skylark.io
 */
package object conversion
{

  object default extends DefaultConversionImplicits
  {

    implicit object MeasureCanConvert extends CanConvert[Measure, Measure]
    {

      override def convert: Converter[Measure, Measure] = new MeasureConverter
      {
        override def convert(from: Measure, to: Measure): Option[Double] = ⤇(from, to) match
        {
          case (dm1: DimensionlessMeasure) ⤇ (dm2: DimensionlessMeasure) => dimensionlessCanConvert.convert(dm1, dm2)
          case (tm1: TimeMeasure) ⤇ (tm2: TimeMeasure) => timeCanConvert.convert(tm1, tm2)
          case (mm1: MassMeasure) ⤇ (mm2: MassMeasure) => massCanConvert.convert(mm1, mm2)
          case (lm1: LengthMeasure) ⤇ (lm2: LengthMeasure) => lengthCanConvert.convert(lm1, lm2)
          case (em1: EnergyMeasure) ⤇ (em2: EnergyMeasure) => energyCanConvert.convert(em1, em2)
          case (ccy1: Currency) ⤇ (ccy2: Currency) => currencyCanConvert.convert(ccy1, ccy2)
          case (diff1 * (same1 / diff2)) ⤇ same2 if same1 == same2 => diff1.to(diff2)
          case _ => super.convert(from, to)
        }
      }
    }

  }

}
