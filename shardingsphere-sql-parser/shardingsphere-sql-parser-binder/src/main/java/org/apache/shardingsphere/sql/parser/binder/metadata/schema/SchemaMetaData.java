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

package org.apache.shardingsphere.sql.parser.binder.metadata.schema;

import org.apache.shardingsphere.sql.parser.binder.metadata.table.TableMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Schema meta data.
 */
public final class SchemaMetaData {
    
    private final Map<String, TableMetaData> tables;
    
    public SchemaMetaData(final Map<String, TableMetaData> tables) {
        this.tables = new ConcurrentHashMap<>(tables.size(), 1);
        for (Entry<String, TableMetaData> entry : tables.entrySet()) {
            this.tables.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }
    
    /**
     * Get all table names.
     *
     * @return all table names
     */
    public Collection<String> getAllTableNames() {
        return tables.keySet();
    }
    
    /**
     * Get table meta data via table name.
     * 
     * @param tableName tableName table name
     * @return table mata data
     */
    public TableMetaData get(final String tableName) {
        return tables.get(tableName.toLowerCase());
    }
    
    /**
     * Merge schema meta data.
     * 
     * @param schemaMetaData schema meta data
     */
    public void merge(final SchemaMetaData schemaMetaData) {
        tables.putAll(schemaMetaData.tables);
    }
    
    /**
     * Add table meta data.
     * 
     * @param tableName table name
     * @param tableMetaData table meta data
     */
    public void put(final String tableName, final TableMetaData tableMetaData) {
        tables.put(tableName.toLowerCase(), tableMetaData);
    }
    
    /**
     * Remove table meta data.
     *
     * @param tableName table name
     */
    public void remove(final String tableName) {
        tables.remove(tableName.toLowerCase());
    }
    
    /**
     * Judge contains table from table meta data or not.
     *
     * @param tableName table name
     * @return contains table from table meta data or not
     */
    public boolean containsTable(final String tableName) {
        return tables.containsKey(tableName.toLowerCase());
    }
    
    /**
     * Judge whether contains column name.
     *
     * @param tableName table name
     * @param columnName column name
     * @return contains column name or not
     */
    public boolean containsColumn(final String tableName, final String columnName) {
        return containsTable(tableName) && get(tableName).getColumns().containsKey(columnName.toLowerCase());
    }
    
    /**
     * Get all column names via table.
     *
     * @param tableName table name
     * @return column names
     */
    public List<String> getAllColumnNames(final String tableName) {
        return containsTable(tableName) ? new ArrayList<>(get(tableName).getColumns().keySet()) : Collections.emptyList();
    }
}
