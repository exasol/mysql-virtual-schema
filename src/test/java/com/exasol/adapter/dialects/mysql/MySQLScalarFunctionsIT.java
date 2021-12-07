package com.exasol.adapter.dialects.mysql;

import java.io.IOException;
import java.sql.*;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.junit.jupiter.api.*;

import com.exasol.adapter.commontests.scalarfunction.ScalarFunctionsTestBase;
import com.exasol.adapter.commontests.scalarfunction.TestSetup;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.*;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.Column;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.TableRequest;
import com.exasol.adapter.metadata.DataType;
import com.exasol.dbbuilder.dialects.Schema;
import com.exasol.dbbuilder.dialects.Table;
import com.exasol.dbbuilder.dialects.exasol.VirtualSchema;
import com.exasol.dbbuilder.dialects.mysql.MySqlObjectFactory;

@Disabled("Currently too many errors") // TODO
public class MySQLScalarFunctionsIT extends ScalarFunctionsTestBase {
    private static final MySQLVirtualSchemaIntegrationTestSetup SETUP = new MySQLVirtualSchemaIntegrationTestSetup();

    @BeforeAll
    static void beforeAll() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterAll
    static void afterAll() throws IOException {
        SETUP.close();
    }

    @Override
    protected TestSetup getTestSetup() {
        return new MySqlTestSetup();
    }

    private static class MySQLSingleTableVirtualSchemaTestSetup implements VirtualSchemaTestSetup {
        private final VirtualSchema virtualSchema;
        private final Schema mySqlSchema;

        private MySQLSingleTableVirtualSchemaTestSetup(final VirtualSchema virtualSchema, final Schema mySqlSchema) {
            this.virtualSchema = virtualSchema;
            this.mySqlSchema = mySqlSchema;
        }

        @Override
        public String getFullyQualifiedName() {
            return this.virtualSchema.getFullyQualifiedName();
        }

        @Override
        public void close() {
            this.virtualSchema.drop();
            this.mySqlSchema.drop();
        }
    }

    private static class MySqlTestSetup implements TestSetup {
        private final MySqlObjectFactory mySqlFactory;

        public MySqlTestSetup() {
            mySqlFactory = SETUP.getMySqlObjectFactory();
        }

        @Override
        public VirtualSchemaTestSetupProvider getVirtualSchemaTestSetupProvider() {
            return (final CreateVirtualSchemaTestSetupRequest request) -> {
                final Schema mySqlSchema = mySqlFactory.createSchema(getUniqueIdentifier());
                for (final TableRequest tableRequest : request.getTableRequests()) {
                    final Table.Builder tableBuilder = mySqlSchema.createTableBuilder(tableRequest.getName());
                    for (final Column column : tableRequest.getColumns()) {
                        tableBuilder.column(column.getName(), column.getType());
                    }
                    final Table table = tableBuilder.build();
                    for (final List<Object> row : tableRequest.getRows()) {
                        List<Object> newRow = preprocessValuesForMySql(row);
                        table.insert(newRow.toArray());
                    }
                }
                final VirtualSchema virtualSchema = SETUP.createVirtualSchema(Collections.emptyMap(),
                        mySqlSchema.getName());
                return new MySQLSingleTableVirtualSchemaTestSetup(virtualSchema, mySqlSchema);
            };
        }

        private List<Object> preprocessValuesForMySql(List<Object> row) {
            List<Object> newRow = new ArrayList<>(row.size());
            for (Object col : row) {
                if (col instanceof Timestamp) {
                    Timestamp timestamp = (Timestamp) col;
                    final String formatted = timestamp.toInstant().atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    newRow.add(formatted);
                } else {
                    newRow.add(col);
                }
            }
            return newRow;
        }

        @Override
        public String getExternalTypeFor(final DataType exasolType) {
            switch (exasolType.getExaDataType()) {
            case VARCHAR:
                return "VARCHAR(" + exasolType.getSize() + ")";
            case DOUBLE:
                return "DOUBLE PRECISION";
            case DECIMAL:
                if (exasolType.getScale() == 0) {
                    return "INTEGER";
                } else {
                    return exasolType.toString();
                }
            default:
                return exasolType.toString();
            }
        }

        @Override
        public Set<String> getDialectSpecificExcludes() {
            return Collections.emptySet();
        }

        @Override
        public Connection createExasolConnection() throws SQLException {
            return SETUP.getExasolContainer().createConnection();
        }
    }
}
