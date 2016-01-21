package com.quantarray.skylark.measure

import scala.annotation.implicitNotFound

/**
  * Can convert type class.
  *
  * @author Araik Grigoryan
  */
@implicitNotFound("Cannot find CanConvert implementation that converts from ${From} to ${To}.")
trait CanConvert[From, To]
{
  def convert: Converter[From, To]
}

object CanConvert
{
  def empty[From, To]: CanConvert[From, To] = new CanConvert[From, To]
  {
    override def convert: Converter[From, To] = new Converter[From, To]
    {
    }
  }

  def apply[From, To](convert: Converter[From, To]): CanConvert[From, To] =
  {
    val params = convert

    new CanConvert[From, To]
    {
      val convert: Converter[From, To] = params
    }
  }
}