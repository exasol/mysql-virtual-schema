package com.exasol.adapter.dialects.mysql;

import java.sql.*;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.exasol.adapter.capabilities.ScalarFunctionCapability;
import com.exasol.adapter.commontests.scalarfunction.ScalarFunctionsTestBase;
import com.exasol.adapter.commontests.scalarfunction.TestSetup;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.*;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.Column;
import com.exasol.adapter.commontests.scalarfunction.virtualschematestsetup.request.TableRequest;
import com.exasol.adapter.dialects.JDBCAdapterContext;
import com.exasol.adapter.dialects.SqlDialect;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.sql.ScalarFunction;
import com.exasol.dbbuilder.dialects.Schema;
import com.exasol.dbbuilder.dialects.Table;
import com.exasol.dbbuilder.dialects.exasol.VirtualSchema;
import com.exasol.dbbuilder.dialects.mysql.MySqlObjectFactory;

public class MySQLScalarFunctionsIT extends ScalarFunctionsTestBase {
    private static final MySQLVirtualSchemaIntegrationTestSetup SETUP = new MySQLVirtualSchemaIntegrationTestSetup();

    @Override
    protected void beforeAllSetup() {
        // Nothing to do
    }

    @Override
    protected void afterAllTeardown() {
        // Nothing to do
    }

    @BeforeAll
    static void beforeAll() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterAll
    static void afterAll() {
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
            this.mySqlFactory = SETUP.getMySqlObjectFactory();
        }

        @Override
        public VirtualSchemaTestSetupProvider getVirtualSchemaTestSetupProvider() {
            return (final CreateVirtualSchemaTestSetupRequest request) -> {
                final Schema mySqlSchema = this.mySqlFactory.createSchema(getUniqueIdentifier());
                for (final TableRequest tableRequest : request.getTableRequests()) {
                    final Table.Builder tableBuilder = mySqlSchema.createTableBuilder(tableRequest.getName());
                    for (final Column column : tableRequest.getColumns()) {
                        tableBuilder.column(column.getName(), column.getType());
                    }
                    final Table table = tableBuilder.build();
                    for (final List<Object> row : tableRequest.getRows()) {
                        final List<Object> newRow = preprocessValuesForMySql(row);
                        table.insert(newRow.toArray());
                    }
                }
                final VirtualSchema virtualSchema = SETUP.createVirtualSchema(Collections.emptyMap(),
                        mySqlSchema.getName());
                return new MySQLSingleTableVirtualSchemaTestSetup(virtualSchema, mySqlSchema);
            };
        }

        private List<Object> preprocessValuesForMySql(final List<Object> row) {
            final List<Object> newRow = new ArrayList<>(row.size());
            for (final Object col : row) {
                if (col instanceof Timestamp) {
                    final Timestamp timestamp = (Timestamp) col;
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
            final Set<String> excludes = new HashSet<>();
            final List<String> dateAddFunctions = List.of("add_days", "add_hours", "add_minutes", "add_months",
                    "add_seconds", "add_weeks", "add_years");
            final List<String> temporalColumns = List.of("DATE_C5", "TIMESTAMP_C6");
            final List<String> unsupportedOffsetColumns = List.of("DOUBLE_PRECISION_C0", "DECIMAL18__3_C2",
                    "BOOLEAN_C3");
            for (final String function : dateAddFunctions) {
                for (final String temporalColumn : temporalColumns) {
                    for (final String offsetColumn : unsupportedOffsetColumns) {
                        excludes.add(function + "(\"" + temporalColumn + "\", \"" + offsetColumn + "\")");
                    }
                }
            }
            // MySQL truncates fractional SPACE lengths and ROUND precisions, whereas Exasol rounds them.
            excludes.add("space(\"DOUBLE_PRECISION_C0\")");
            excludes.add("round(\"DOUBLE_PRECISION_C0\", \"DOUBLE_PRECISION_C0\")");
            // USER() describes the remote MySQL account, not the querying Exasol user.
            excludes.add("user()");
            excludes.addAll(getDialectExcludes());
            return excludes;
        }

        /**
         * Returns a collection of SQL functions that are excluded by the MySQL dialect.
         * This is a workaround for https://github.com/exasol/virtual-schema-shared-integration-tests/issues/55
         */
        private Collection<String> getDialectExcludes() {
            try (SqlDialect dialect = new MySQLSqlDialect(JDBCAdapterContext.builder().build())) {
                final Set<ScalarFunctionCapability> enabledCapabilties = dialect.getCapabilities().getScalarFunctionCapabilities();
                final Set<ScalarFunctionCapability> allCapabilties = Set.of(ScalarFunctionCapability.values());
                final Set<ScalarFunctionCapability> excludedCapabilities = allCapabilties.stream()
                        .filter(capability -> !enabledCapabilties.contains(capability)).collect(Collectors.toSet());
                return excludedCapabilities.stream().map(ScalarFunctionCapability::getFunction).map(ScalarFunction::name).collect(Collectors.toSet());
            }
        }

        @Override
        public Connection createExasolConnection() throws SQLException {
            return SETUP.getExasolContainer().createConnection();
        }
    }
}
