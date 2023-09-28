package com.exasol.adapter.dialects.mysql.metadata;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import com.exasol.adapter.dialects.mysql.MySQLColumnMetadataReader;
import com.exasol.adapter.dialects.mysql.charset.ColumnInspector;
import com.exasol.adapter.metadata.ColumnMetadata;

@Tag("integration")
class MySqlMetadataIT {

    private static final Logger LOGGER = Logger.getLogger(MySqlMetadataIT.class.getName());
    private static MySqlMetadataITSetup SETUP = new MySqlMetadataITSetup();

    @AfterAll
    static void afterAll() throws IOException {
        SETUP.close();
    }

    static String f(final String label, final Object value) {
        return String.format("%s: '%s'", label, value);
    }

    private String columnProperties(final ResultSetMetaData metadata, final int col, final Optional<String> value)
            throws SQLException {
        final SizeDetector sizeDetector = new SizeDetector(metadata, col);
        List<String> list = List.of( //
                f("Type", metadata.getColumnType(col)), //
                f("TypeName", metadata.getColumnTypeName(col)), //
                f("Name", metadata.getColumnName(col)), //
                f("Label", metadata.getColumnLabel(col)), //
                f("ClassName", metadata.getColumnClassName(col)), //
                f("ExaCharset", sizeDetector.getExaCharset()) //
        );

        if (value.isPresent()) {
            final String content = value.get();
            list = new ArrayList<>(list);
            list.add(f("value", content));
        }
        return String.join(", ", list);
    }

    private void report(final ResultSet resultSet, final int columnCount, final boolean withValue) throws SQLException {
        final ResultSetMetaData metadata = resultSet.getMetaData();
        for (int i = 1; i <= columnCount; ++i) {
            final String properties = (withValue ? //
                    columnProperties(metadata, i, Optional.of(resultSet.getString(i))) //
                    : columnProperties(metadata, i, Optional.empty()));
            LOGGER.info(String.format("Column %d: %s", i, properties));
        }
    }

    @Test
    void testReadMetadata() throws SQLException {
        final Statement statement = SETUP.getMySqlStatement();
        statement.execute("CREATE DATABASE MS");
        statement.execute("USE MS");
//        statement.execute("create table MT (c1 char(1), c2 ENUM('A', 'Ü')) character set 'latin1'");
//        statement.execute("insert MT VALUES ('Ü', 'Ü')");
        statement.execute("create table MT (c1 char(1), c2 enum('A', 'B'))");
        statement.execute("insert MT VALUES ('A', 'B')");
        final ResultSet resultSet = statement.executeQuery("select * from MT");
        final ColumnInspector inspector = SETUP.getColumnInspector("MS");
        inspector.describeFromMetadata("MS", "MT");

        final MySQLColumnMetadataReader columnMetadataReader = SETUP.getColumnMetadataReader();
        final List<ColumnMetadata> columns = columnMetadataReader.mapColumns("MT");
        columns.get(0).getType().getExaDataType();

        final int count = resultSet.getMetaData().getColumnCount();
        LOGGER.info(String.format("Found %d columns", count));
        report(resultSet, count, false);
        boolean next = resultSet.next();
        while (next) {
            report(resultSet, count, true);
            next = resultSet.next();
        }
        statement.execute("drop database MS");
    }
}
