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

package com.quantarray.skylark.db

import java.io.Closeable

/**
 * DB session.
 *
 * @author Araik Grigoryan
 */
protected[db] abstract class DbSession[S <: Closeable](val session: S)
{
  def close(): Unit = session.close()

  override def toString = s"$session"
}

/**
 * Read session.
 */
protected[db] abstract class RDbSession[S <: Closeable](rSession: S) extends DbSession[S](rSession)

/**
 * Read-only session.
 */
class RODbSession[S <: Closeable](roSession: S) extends RDbSession(roSession)

/**
 * Read-write session.
 */
class RWDbSession[S <: Closeable](rwSession: S) extends RDbSession(rwSession)
