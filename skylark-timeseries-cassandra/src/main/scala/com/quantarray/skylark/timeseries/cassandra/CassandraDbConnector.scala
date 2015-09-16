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

import com.datastax.driver.core.{Cluster, Session}
import com.quantarray.skylark.db.{RODbSession, RWDbSession}

import scala.concurrent._

/**
 * Cassandra database connector.
 *
 * @author Araik Grigoryan
 */
case class CassandraDbConnector(cluster: Cluster, keySpaceName: String)
{
  def readOnly[T](block: RODbSession[Session] => T): T =
  {
    lazy val session = blocking
    {
      cluster.connect(keySpaceName)
    }

    try
    {
      block(new RODbSession(session))
    }
    finally session.close()
  }

  def readOnlyWithDelayedClose(): RODbSession[Session] =
  {
    lazy val session = blocking
    {
      cluster.connect(keySpaceName)
    }

    new RODbSession(session)
  }

  def readWrite[T](block: RWDbSession[Session] => T): T =
  {
    lazy val session = blocking
    {
      cluster.connect(keySpaceName)
    }

    try
    {
      block(new RWDbSession(session))
    }
    finally session.close()
  }

  def readWriteWithDelayedClose(): RWDbSession[Session] =
  {
    lazy val session = blocking
    {
      cluster.connect(keySpaceName)
    }

    new RWDbSession(session)
  }
}
