package com.exasol.adapter.dialects.mysql.charset;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.adapternotes.ColumnAdapterNotes;
import com.exasol.adapter.adapternotes.ColumnAdapterNotesJsonConverter;
import com.exasol.adapter.dialects.mysql.MySQLMetadataReader;
import com.exasol.adapter.jdbc.ColumnMetadataReader;
import com.exasol.adapter.jdbc.ResultSetMetadataReader;
import com.exasol.adapter.metadata.ColumnMetadata;

/**
 * This class enables to take a closer look at the columns of a MySQL table from the perspective of the JDBC driver.
 */
public class ColumnInspector {

    public static ColumnInspector from(final Connection mySqlConnection, final String catalogName) {
        return new ColumnInspector(mySqlConnection,
                getMetadataReader(mySqlConnection, catalogName).getColumnMetadataReader());
    }

    private static final Logger LOGGER = Logger.getLogger(ColumnInspector.class.getName());

    private static MySQLMetadataReader getMetadataReader(final Connection mySqlConnection, final String catalogName) {
        return new MySQLMetadataReader(mySqlConnection, //
                new AdapterProperties(Map.of(AdapterProperties.CATALOG_NAME_PROPERTY, catalogName)));
    }

    private final Connection mySqlConnection;
    private final ColumnMetadataReader columnMetadataReader;

    public ColumnInspector(final Connection mySqlConnection, final ColumnMetadataReader columnMetadataReader) {
        this.mySqlConnection = mySqlConnection;
        this.columnMetadataReader = columnMetadataReader;
    }

    public void describeFromMetadata(final String catalogName, final String tableName) throws SQLException {
        LOGGER.info(() -> "Column Metadata for MySQL table '" + tableName + "' provided by JDBC driver:");
        for (final ColumnMetadata column : this.columnMetadataReader.mapColumns(tableName)) {
            LOGGER.info(() -> "- " + describeColumn(column));
        }
    }

    public void describeFromQuery(final String catalogName, final String query) throws SQLException {
        LOGGER.info(() -> "Column descriptions from query '" + query + "', as provided by JDBC driver: "
                + getResultSetMetadataReader().describeColumns(query));
    }

    private ResultSetMetadataReader getResultSetMetadataReader() {
        return new ResultSetMetadataReader(this.mySqlConnection, this.columnMetadataReader);
    }

    private String describeColumn(final ColumnMetadata column) {
        return String.format("Column '%s': from adapter notes: { %s }, type = '%s'.", //
                column.getName(), jdbcDataType(column), column.getType());
    }

    private String jdbcDataType(final ColumnMetadata column) {
        try {
            final ColumnAdapterNotes notes = ColumnAdapterNotesJsonConverter.getInstance()
                    .convertFromJsonToColumnAdapterNotes(column.getAdapterNotes(), column.getName());
            final int code = notes.getJdbcDataType();
            return String.format("JdbcDataType = %d (%s), typeName = '%s'", code, JdbcType.name(code),
                    notes.getTypeName());
        } catch (final AdapterException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
