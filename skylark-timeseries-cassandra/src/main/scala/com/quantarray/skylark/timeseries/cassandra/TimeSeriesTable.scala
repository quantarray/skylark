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

package com.quantarray.skylark.timeseries.cassandra

import com.quantarray.skylark.timeseries.TimeSeriesPoint
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.Implicits._
import com.websudos.phantom.column.DateTimeColumn
import com.websudos.phantom.keys.{ClusteringOrder, Descending, PartitionKey, PrimaryKey}
import org.joda.time.DateTime

/**
 * Time series table.
 *
 * @author Araik Grigoryan
 */
abstract class TimeSeriesTable[V, P <: TimeSeriesPoint[V], T <: TimeSeriesTable[V, P, T]] extends CassandraTable[T, P]
{

  object entityKey extends StringColumn(this) with PartitionKey[String]
  {
    override lazy val name = "entity_key"
  }

  object setId extends IntColumn(this) with PartitionKey[Int]
  {
    override lazy val name = "set_id"
  }

  object observedYear extends IntColumn(this) with PartitionKey[Int]
  {
    override lazy val name = "observed_year"
  }

  object observedTime extends DateTimeColumn(this) with PrimaryKey[DateTime] with ClusteringOrder[DateTime] with Descending
  {
    override lazy val name = "observed_time"
  }

  object versionTime extends DateTimeColumn(this) with PrimaryKey[DateTime] with ClusteringOrder[DateTime] with Descending
  {
    override lazy val name = "version_time"
  }

}
