/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sql.parser.integrate.asserts.statement.dml.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.sql.parser.integrate.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.SQLSegmentAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.TableReferencesAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.groupby.GroupByClauseAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.limit.LimitClauseAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.orderby.OrderByClauseAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.projection.ProjectionAssert;
import org.apache.shardingsphere.sql.parser.integrate.asserts.segment.where.WhereClauseAssert;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.domain.statement.dml.SelectStatementTestCase;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.TableReferenceSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.pagination.limit.LimitSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Select statement assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SelectStatementAssert {
    
    /**
     * Assert select statement is correct with expected parser result.
     * 
     * @param assertContext assert context
     * @param actual actual select statement
     * @param expected expected select statement test case
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final SelectStatement actual, final SelectStatementTestCase expected) {
        assertProjection(assertContext, actual, expected);
        assertWhereClause(assertContext, actual, expected);
        assertGroupByClause(assertContext, actual, expected);
        assertOrderByClause(assertContext, actual, expected);
        assertLimitClause(assertContext, actual, expected);
        assertTableReferences(assertContext, actual, expected);
    }
    
    private static void assertTableReferences(final SQLCaseAssertContext assertContext, final SelectStatement actual, final SelectStatementTestCase expected) {
        TableReferencesAssert.assertIs(assertContext, (List<TableReferenceSegment>) actual.getTableReferences(), expected.getTableReferences());
    }
    
    private static void assertProjection(final SQLCaseAssertContext assertContext, final SelectStatement actual, final SelectStatementTestCase expected) {
        ProjectionAssert.assertIs(assertContext, actual.getProjections(), expected.getProjections());
    }
    
    private static void assertWhereClause(final SQLCaseAssertContext assertContext, final SelectStatement actual, final SelectStatementTestCase expected) {
        if (null != expected.getWhereClause()) {
            assertTrue(assertContext.getText("Actual where segment should exist."), actual.getWhere().isPresent());
            WhereClauseAssert.assertIs(assertContext, actual.getWhere().get(), expected.getWhereClause());
        } else {
            assertFalse(assertContext.getText("Actual where segment should not exist."), actual.getWhere().isPresent());
        }
    }
    
    private static void assertGroupByClause(final SQLCaseAssertContext assertContext, final SelectStatement actual, final SelectStatementTestCase expected) {
        if (null != expected.getGroupByClause()) {
            assertTrue(assertContext.getText("Actual group by segment should exist."), actual.getGroupBy().isPresent());
            GroupByClauseAssert.assertIs(assertContext, actual.getGroupBy().get(), expected.getGroupByClause());
        } else {
            assertFalse(assertContext.getText("Actual group by segment should not exist."), actual.getGroupBy().isPresent());
        }
    }
    
    private static void assertOrderByClause(final SQLCaseAssertContext assertContext, final SelectStatement actual, final SelectStatementTestCase expected) {
        if (null != expected.getOrderByClause()) {
            assertTrue(assertContext.getText("Actual order by segment should exist."), actual.getOrderBy().isPresent());
            OrderByClauseAssert.assertIs(assertContext, actual.getOrderBy().get(), expected.getOrderByClause());
        } else {
            assertFalse(assertContext.getText("Actual order by segment should not exist."), actual.getOrderBy().isPresent());
        }
    }
    
    private static void assertLimitClause(final SQLCaseAssertContext assertContext, final SelectStatement actual, final SelectStatementTestCase expected) {
        Optional<LimitSegment> limitSegment = actual.getLimit();
        if (null != expected.getLimitClause()) {
            assertTrue(assertContext.getText("Actual limit segment should exist."), limitSegment.isPresent());
            LimitClauseAssert.assertOffset(assertContext, limitSegment.get().getOffset().orElse(null), expected.getLimitClause().getOffset());
            LimitClauseAssert.assertRowCount(assertContext, limitSegment.get().getRowCount().orElse(null), expected.getLimitClause().getRowCount());
            SQLSegmentAssert.assertIs(assertContext, limitSegment.get(), expected.getLimitClause());
        } else {
            assertFalse(assertContext.getText("Actual limit segment should not exist."), limitSegment.isPresent());
        }
    }
}
