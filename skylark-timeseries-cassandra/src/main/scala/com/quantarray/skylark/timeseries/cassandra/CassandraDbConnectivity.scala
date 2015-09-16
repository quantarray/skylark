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

import com.datastax.driver.core.Cluster

/**
 * Cassandra database connectivity.
 *
 * @author Araik Grigoryan
 */
trait CassandraDbConnectivity
{
  self: CassandraCluster with CassandraKeySpace =>

  lazy val cluster = Cluster.builder()
    .addContactPoint(contactPointAddress)
    .withPort(contactPointAddressPort)
    //.withCredentials()
    .withoutJMXReporting()
    .withoutMetrics()
    .build()

  lazy val db = CassandraDbConnector(cluster, keySpaceName)
}
