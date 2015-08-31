package com.quantarray.skylark

/*
 * Skylark
 *
 * © 2012-2015, Quantarray
 * http://skylark.io
 */
package object timeseries
{
  type AnyTimeSeries[V] = TimeSeries[V, TimeSeriesPoint[V]]
}
