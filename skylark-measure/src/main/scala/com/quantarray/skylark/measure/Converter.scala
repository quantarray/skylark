package com.quantarray.skylark.measure

/**
 * Converter.
 *
 * @author Araik Grigoryan
 */
trait Converter[From, To]
{
  def apply(from: From, to: To): Option[Double] = (from, to) match
  {
    case (_, _) if from == to => Some(1.0)
    case _ => None
  }
}
