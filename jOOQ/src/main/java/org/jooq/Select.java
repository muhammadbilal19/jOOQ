/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq;

// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import java.util.List;

import org.jooq.exception.DataAccessException;

/**
 * A {@link Query} that can provide a {@link Result} after execution
 *
 * @param <R> The record type being returned by this query
 * @author Lukas Eder
 */
public interface Select<R extends Record> extends ResultQuery<R>, TableLike<R>, FieldLike {

    /**
     * Combine with other selects
     */
    @Support
    Select<R> union(Select<? extends R> select);

    /**
     * Combine with other selects
     */
    @Support
    Select<R> unionAll(Select<? extends R> select);

    /**
     * Combine with other selects
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    Select<R> except(Select<? extends R> select);

    /**
     * Combine with other selects
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    Select<R> intersect(Select<? extends R> select);

    /**
     * All fields selected in this query
     */
    List<Field<?>> getSelect();

    /**
     * Execute this query in the context of its attached executor and return a
     * <code>COUNT(*)</code> value.
     * <p>
     * This wraps a pre-existing <code>SELECT</code> query in another one to
     * calculate the <code>COUNT(*)</code> value, without modifying the original
     * <code>SELECT</code>. An example: <code><pre>
     * -- Original query:
     * SELECT id, title FROM book WHERE title LIKE '%a%'
     *
     * -- Wrapped query:
     * SELECT count(*) FROM (
     *   SELECT id, title FROM book WHERE title LIKE '%a%'
     * )
     * </pre></code> This is particularly useful for those databases that do not
     * support the <code>COUNT(*) OVER()</code> window function to calculate
     * total results in paged queries.
     *
     * @return The <code>COUNT(*)</code> result
     * @throws DataAccessException if something went wrong executing the query
     * @deprecated - 3.5.0 - [#3356] - This method is being removed as it is
     *             confusingly different from all the other types of
     *             {@link #fetch()} methods, in that it modifies the original
     *             {@link Select} statement by wrapping it. In particular, this
     *             method can be easily confused with {@link #fetch(Field)}, or
     *             more concretely <code>fetch(count())</code>, which has an
     *             entirely different semantics. Use
     *             {@link DSLContext#fetchCount(Select)} instead.
     */
    @Deprecated
    int fetchCount() throws DataAccessException;
}
