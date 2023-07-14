package com.exasol.adapter.dialects.mysql;

import static com.exasol.adapter.dialects.mysql.IntegrationTestConstants.*;
import static com.exasol.matcher.ResultSetMatcher.matchesResultSet;
import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import org.hamcrest.Matcher;
import org.junit.Assume;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.adapter.dialects.mysql.charset.ColumnInspector;
import com.exasol.adapter.dialects.mysql.charset.Version;
import com.exasol.adapter.properties.DataTypeDetection;
import com.exasol.containers.ExasolDockerImageReference;
import com.exasol.dbbuilder.dialects.*;
import com.exasol.dbbuilder.dialects.exasol.VirtualSchema;
import com.exasol.dbbuilder.dialects.mysql.MySQLIdentifier;
import com.exasol.dbbuilder.dialects.mysql.MySqlSchema;
import com.exasol.matcher.TypeMatchMode;

/**
 * How to run `MySqlSqlDialectIT`: See the documentation <a
 * href="doc/development/developing-sql-dialect/integration_testing_with_containers.md>integration_testing_with_containers.md</a>.
 */
@Tag("integration")
@Testcontainers
class MySQLSqlDialectIT {
    private static final String MYSQL_SCHEMA = "MYSQL_SCHEMA";
    private static final String MYSQL_SIMPLE_TABLE = "MYSQL_SIMPLE_TABLE";
    private static final String MYSQL_NUMERIC_DATE_DATATYPES_TABLE = "MYSQL_NUMERIC_DATE_TABLE";
    private static final String MYSQL_STRING_DATATYPES_TABLE = "MYSQL_STRING_TABLE";
    private static final MySQLVirtualSchemaIntegrationTestSetup SETUP = new MySQLVirtualSchemaIntegrationTestSetup();
    private static final String MYSQL_SOURCE_SCHEMA = "SOURCE_SCHEMA";
    private static final String MYSQL_SOURCE_TABLE = "SOURCE_TABLE";
    private static String virtualSchemaJdbc;
    private MySqlSchema sourceSchema;
    private VirtualSchema virtualSchema;

    @BeforeAll
    static void beforeAll() throws SQLException {
        final MySqlSchema mySqlSchema = SETUP.getMySqlObjectFactory().createSchema(MYSQL_SCHEMA);
        createMySqlSimpleTable(mySqlSchema);
        createMySqlNumericDateTable(mySqlSchema);
        createMySqlStringTable(mySqlSchema);
        createTestTablesForJoinTests(SETUP.getMySqlStatement(), mySqlSchema.getName());
        virtualSchemaJdbc = SETUP.createVirtualSchema(Collections.emptyMap(), mySqlSchema.getName()).getName();
    }

    @AfterAll
    static void afterAll() throws IOException {
        SETUP.close();
    }

    @AfterEach
    void afterEach() {
        dropAll(this.virtualSchema, this.sourceSchema);
        this.virtualSchema = null;
        this.sourceSchema = null;
    }

    private static void dropAll(final DatabaseObject... databaseObjects) {
        for (final DatabaseObject databaseObject : databaseObjects) {
            if (databaseObject != null) {
                databaseObject.drop();
            }
        }
    }

    private static void createTestTablesForJoinTests(final Statement statement, final String schemaName)
            throws SQLException {
        statement.execute("CREATE TABLE " + schemaName + "." + TABLE_JOIN_1 + "(x INT, y VARCHAR(100))");
        statement.execute("INSERT INTO " + schemaName + "." + TABLE_JOIN_1 + " VALUES (1,'aaa')");
        statement.execute("INSERT INTO " + schemaName + "." + TABLE_JOIN_1 + " VALUES (2,'bbb')");
        statement.execute("CREATE TABLE " + schemaName + "." + TABLE_JOIN_2 + "(x INT, y VARCHAR(100))");
        statement.execute("INSERT INTO " + schemaName + "." + TABLE_JOIN_2 + " VALUES (2,'bbb')");
        statement.execute("INSERT INTO " + schemaName + "." + TABLE_JOIN_2 + " VALUES (3,'ccc')");
    }

    private ResultSet getExpectedResultSet(final List<String> expectedColumns, final List<String> expectedRows)
            throws SQLException {
        final Statement statement = SETUP.getExasolStatement();
        final String expectedValues = expectedRows.stream().map(row -> "(" + row + ")")
                .collect(Collectors.joining(","));
        final String qualifiedExpectedTableName = SCHEMA_EXASOL + ".EXPECTED";
        statement.execute("CREATE OR REPLACE TABLE " + qualifiedExpectedTableName + "("
                + String.join(", ", expectedColumns) + ")");
        statement.execute("INSERT INTO " + qualifiedExpectedTableName + " VALUES" + expectedValues);
        return statement.executeQuery("SELECT * FROM " + qualifiedExpectedTableName);
    }

    private ResultSet getActualResultSet(final String query) throws SQLException {
        final Statement statement = SETUP.getExasolStatement();
        return statement.executeQuery(query);
    }

    private static void createMySqlSimpleTable(final Schema mySqlSchema) {
        final Table table = mySqlSchema.createTable(MYSQL_SIMPLE_TABLE, List.of("int_col", "bool_col", "varchar_col"),
                List.of("INT", "BOOLEAN", "VARCHAR(20)"));
        table.insert(-100, true, "a");
        table.insert(-1, false, "abbb");
        table.insert(0, true, "b");
        table.insert(10, false, "bbbb");
        table.insert(50, true, "abc");
        table.insert(100, false, "a");
    }

    private static void createMySqlNumericDateTable(final Schema mySqlSchema) {
        final Table table = mySqlSchema.createTable(MYSQL_NUMERIC_DATE_DATATYPES_TABLE,
                List.of("BiT_Col", "tinyint_col", "BOOL_COL", "smallint_col", "mediumint_col", "int_col", "bigint_col",
                        "decimal_col", "float_col", "double_col", //
                        "date_col", "datetime_col", "timestamp_col", "time_col", "year_col"),
                List.of("BIT(6)", "TINYINT", "BOOLEAN", "SMALLINT", "MEDIUMINT", "INT", "BIGINT", "DECIMAL(5, 2)",
                        "FLOAT(7, 4)", "DOUBLE", //
                        "DATE", "DATETIME", "TIMESTAMP", "TIME", "YEAR"));
        table.insert("5", 127, 1, 32767, 8388607, 2147483647, 9223372036854775807L, 999.99, 999.00009, 999.00009, //
                "1000-01-01", "1000-01-01 00:00:00", "1970-01-01 00:00:01.000000", "16:59:59.000000", 1901);
        table.insert("9", -127, 0, -32768, -8388608, -2147483648, -9223372036854775808L, -999.99, -999.9999, -999.9999, //
                "9999-12-31", "9999-12-31 22:59:59", "2037-01-19 03:14:07.999999", "05:34:13.000000", 2155);
        table.insert(null, 0, true, 0, 0, 0, 0, 0, 0, 0, //
                null, null, null, null, "1901");
        table.insert(null, 0, false, 0, 0, 0, 0, 0, 0, 0, //
                null, null, null, null, 69);
    }

    private static void createMySqlStringTable(final Schema mySqlSchema) {
        final Table table = mySqlSchema.createTable(MYSQL_STRING_DATATYPES_TABLE,
                List.of("binary_col", "varbinary_col", "tinyblob_col", "tinytext_col", "blob_col", "text_col",
                        "mediumblob_col", "mediumtext_col", "longblob_col", "longtext_col", "enum_col", "set_col",
                        "varchar_col", "char_col"),
                List.of(" BINARY(20)", "VARBINARY(20)", "TINYBLOB", "TINYTEXT", "BLOB", "TEXT", "MEDIUMBLOB",
                        "MEDIUMTEXT", "LONGBLOB", "LONGTEXT", "ENUM('1', '2', '3')", "SET('1')", "VARCHAR(16000)",
                        "CHAR(255)"));
        table.insert("a", "a", "a", "a", "blob", "text", "mediumblob", "mediumtext", "longblob", "longtext", "1", "1",
                "ab", "asd24");
        table.insert("a\0", "a\0", "aa", "b", "blob", "text2", "mediumblob2", "mediumtext2", "longblob", "longtext2",
                "2", "1", "a", "11111");
        table.insert(null, null, "aaa", "aaaaaaaaaaaaa", "bloooooooooooob", "text3", null, null, null, null, "3", null,
                "", "");
        table.insert(null, null, "aaaaa", "a", "blob", "text", null, null, null, null, null, null, null, null);
    }

    @Test
    void importDataTypesFromResultSet() throws SQLException {
        Assume.assumeTrue(runCharsetTest());
        final String query = setupCharacterSet(DataTypeDetection.Strategy.FROM_RESULT_SET);
        final ResultSet actual = getActualResultSet(query);
        final ResultSet expected = getExpectedResultSet(List.of("c1 CHAR(1) UTF8", "c2 CHAR(1) UTF8"), //
                List.of(SPECIAL_CHAR_QUOTED + ", " + SPECIAL_CHAR_QUOTED));
        assertThat(actual, matchesResultSet(expected));
    }

    @Test
    void importDataTypesExasolCalculated() throws SQLException {
        Assume.assumeTrue(runCharsetTest());
        final String query = setupCharacterSet(DataTypeDetection.Strategy.EXASOL_CALCULATED);
        final Exception exception = assertThrows(SQLException.class, () -> getActualResultSet(query));
        assertThat(exception.getMessage(),
                matchesRegex("ETL-3009: .*Charset conversion from 'UTF-8' to 'ASCII' failed.*"));
    }

    private boolean runCharsetTest() {
        final ExasolDockerImageReference dockerImage = SETUP.getExasolContainer().getDockerImageReference();
        if (!dockerImage.hasMajor() || !dockerImage.hasMinor() || !dockerImage.hasFix()) {
            return false;
        }
        final Version version = Version.of(dockerImage.getMajor(), dockerImage.getMinor(), dockerImage.getFixVersion());
        if ((dockerImage.getMajor() == 7) && version.isGreaterOrEqualThan(Version.parse("7.1.14"))) {
            return true;
        }
        if ((dockerImage.getMajor() == 8) && version.isGreaterOrEqualThan(Version.parse("8.6.0"))) {
            return true;
        }
        return false;
    }

    private String setupCharacterSet(final DataTypeDetection.Strategy strategy) throws SQLException {
        final String tableName = MYSQL_SOURCE_TABLE;
        createMySqlTableWithCharacterSet(MYSQL_SOURCE_SCHEMA, tableName, "latin1");
        this.virtualSchema = SETUP.createVirtualSchema( //
                Map.of(DataTypeDetection.STRATEGY_PROPERTY, strategy.name()), //
                MYSQL_SOURCE_SCHEMA);
        final ColumnInspector inspector = SETUP.getColumnInspector(MYSQL_SOURCE_SCHEMA);
        inspector.describeFromMetadata(MYSQL_SOURCE_SCHEMA, MYSQL_SOURCE_TABLE);
        final String query = String.format("select * from %s.%s", MYSQL_SOURCE_SCHEMA, MYSQL_SOURCE_TABLE);
        inspector.describeFromQuery(MYSQL_SOURCE_SCHEMA, query);
        return "SELECT * FROM " + this.virtualSchema.getName() + "." + tableName;
    }

    private static final char[] GERMAN_UMLAUT = { 0xDC };
    private static final String SPECIAL_CHAR = new String(GERMAN_UMLAUT);
    private static final String SPECIAL_CHAR_QUOTED = "'" + SPECIAL_CHAR + "'";

    private void createMySqlTableWithCharacterSet(final String schemaName, final String tableName,
            final String characterSet) {
        this.sourceSchema = getSchemaWithCharacterSet(schemaName, "latin1");
        final String mySqlEnum = "ENUM('A', " + SPECIAL_CHAR_QUOTED + ")";
        final Table table = this.sourceSchema.createTable(tableName, List.of("c1", "c2"),
                List.of("CHAR(1)", mySqlEnum));
        table.insert(SPECIAL_CHAR, SPECIAL_CHAR);
    }

    private static MySqlSchema getSchemaWithCharacterSet(final String schemaName, final String characterSet) {
        return new MySqlSchema(SETUP.getTableWriterWithCharacterSet(characterSet), MySQLIdentifier.of(schemaName));
    }

    @Test
    void testSelectAll() throws SQLException {
        final String query = "SELECT * FROM " + virtualSchemaJdbc + "." + MYSQL_SIMPLE_TABLE;
        final ResultSet actualResultSet = getActualResultSet(query);
        final ResultSet expected = getExpectedResultSet(List.of("col1 INT", "col2 BOOLEAN", "col3 VARCHAR(20)"), //
                List.of("-100, true, 'a'", //
                        "-1, false, 'abbb'", //
                        "0, true, 'b'", //
                        "10, false, 'bbbb'", //
                        "50, true, 'abc'", //
                        "100, false, 'a'"));
        assertThat(actualResultSet, matchesResultSet(expected));
    }

    @Test
    void testAggregateGroupByColumn() throws SQLException {
        final String query = "SELECT \"bool_col\", min(\"int_col\") FROM " + virtualSchemaJdbc + "."
                + MYSQL_SIMPLE_TABLE + " GROUP BY \"bool_col\"";
        final ResultSet expected = getExpectedResultSet(List.of("bool_col BOOLEAN", "int_col DECIMAL(10,0)"),
                List.of("true, -100", //
                        "false, -1"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testAggregateHaving() throws SQLException {
        final String query = "SELECT \"bool_col\", min(\"int_col\") FROM " + virtualSchemaJdbc + "."
                + MYSQL_SIMPLE_TABLE + " GROUP BY \"bool_col\" HAVING MIN(\"int_col\") < 0";
        final ResultSet expected = getExpectedResultSet(List.of("bool_col BOOLEAN", "int_col DECIMAL(10,0)"),
                List.of("true, -100", //
                        "false, -1"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    // =, !=, <, <=, >, >=
    void testComparisonPredicates() throws SQLException {
        final String query = "SELECT \"int_col\", \"int_col\" = 60, \"int_col\" != 60, \"int_col\" < 60, "
                + "\"int_col\" <= 60, \"int_col\" > 60, \"int_col\" >= 60 FROM " + virtualSchemaJdbc + "."
                + MYSQL_SIMPLE_TABLE + " WHERE \"int_col\" = 0";
        final ResultSet expected = getExpectedResultSet(
                List.of("int_col DECIMAL(10,0)", "b1 BOOLEAN", "b2 BOOLEAN", "b3 BOOLEAN", "b4 BOOLEAN", "b5 BOOLEAN",
                        "b6 BOOLEAN"), //
                List.of("0, 0, 1, 1, 1, 0, 0"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    // NOT, AND, OR
    void testLogicalPredicates() throws SQLException {
        final String query = "SELECT \"int_col\" FROM " + virtualSchemaJdbc + "." + MYSQL_SIMPLE_TABLE
                + " WHERE (\"int_col\" < 0 or \"int_col\" > 0) AND NOT (\"int_col\" is null)";
        final ResultSet expected = getExpectedResultSet(List.of("int_col DECIMAL(10,0)"), //
                List.of("-100", //
                        "-1", //
                        "10", //
                        "50", //
                        "100"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    // LIKE, LIKE ESCAPE (not pushed down)
    void testLikePredicates() throws SQLException {
        final String query = "SELECT \"varchar_col\", \"varchar_col\" LIKE 'a%' ESCAPE 'a' FROM " + virtualSchemaJdbc
                + "." + MYSQL_SIMPLE_TABLE + " WHERE (\"varchar_col\" LIKE 'a%')";
        final ResultSet expected = getExpectedResultSet(List.of("varchar_col VARCHAR(10)", "bool_col BOOLEAN"),
                List.of("'a', false", //
                        "'abbb', false", //
                        "'abc', false", //
                        "'a', false"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    // BETWEEN, IN, IS NULL, !=NULL(rewritten to "IS NOT NULL")
    void testMiscPredicates() throws SQLException {
        final String query = "SELECT \"int_col\", \"int_col\" in (56, 61), \"int_col\" is null, \"int_col\" != null"
                + " FROM " + virtualSchemaJdbc + "." + MYSQL_SIMPLE_TABLE + " WHERE \"int_col\" between -10 and 51";
        final ResultSet expected = getExpectedResultSet(
                List.of("int_col DECIMAL(10,0)", "b1 BOOLEAN", "b2 BOOLEAN", "b3 BOOLEAN"), //
                List.of("-1, false, false, false", //
                        "0, false, false, false", //
                        "10, false, false, false", //
                        "50, false, false, false"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testCountSumAggregateFunction() throws SQLException {
        final String query = "SELECT COUNT(\"int_col\"), COUNT(*), COUNT(DISTINCT \"int_col\"), SUM(\"int_col\"), "
                + "SUM(DISTINCT \"int_col\") FROM " + virtualSchemaJdbc + "." + MYSQL_SIMPLE_TABLE;
        final ResultSet expected = getExpectedResultSet(
                List.of("a DECIMAL(10,0)", "b DECIMAL(10,0)", "c DECIMAL(10,0)", "d DECIMAL(19,0)", "e DECIMAL(19,0)"),
                List.of("6, 6, 6, 59, 59"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testAvgMinMaxAggregateFunction() throws SQLException {
        final String query = "SELECT AVG(\"int_col\"), MIN(\"int_col\"), MAX(\"int_col\") FROM " + virtualSchemaJdbc
                + "." + MYSQL_SIMPLE_TABLE;
        final ResultSet expected = getExpectedResultSet( //
                List.of("a DOUBLE", "b DECIMAL(10,0)", "c DECIMAL(10,0)"), //
                List.of("9.833300000000001, -100.0000, 100.0000"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testCastedStringFunctions() throws SQLException {
        final String query = "SELECT concat(upper(\"varchar_col\"),lower(repeat(\"varchar_col\",2))) FROM "
                + virtualSchemaJdbc + "." + MYSQL_SIMPLE_TABLE;
        final ResultSet expected = getExpectedResultSet(List.of("a VARCHAR(100)"), //
                List.of("'Aaa'", //
                        "'ABBBabbbabbb'", //
                        "'Bbb'", //
                        "'BBBBbbbbbbbb'", //
                        "'ABCabcabc'", //
                        "'Aaa'"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testRewrittenDivAndModFunctions() throws SQLException {
        final String query = "SELECT DIV(\"int_col\",\"int_col\"), mod(\"int_col\",\"int_col\") FROM "
                + virtualSchemaJdbc + "." + MYSQL_SIMPLE_TABLE;
        final ResultSet expected = getExpectedResultSet(List.of("a DECIMAL(10,0)", "b DECIMAL(10,0)"), //
                List.of("1, 0", //
                        "1, 0", //
                        "null, null", //
                        "1, 0", //
                        "1, 0", //
                        "1, 0"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testRewrittenSubStringFunction() throws SQLException {
        final String query = "SELECT substring(\"varchar_col\" FROM 1 FOR 2) FROM " + virtualSchemaJdbc + "."
                + MYSQL_SIMPLE_TABLE;
        final ResultSet expected = getExpectedResultSet(List.of("a VARCHAR(100)"), //
                List.of("'a'", //
                        "'ab'", //
                        "'b'", //
                        "'bb'", //
                        "'ab'", //
                        "'a'"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testOrderByLimit() throws SQLException {
        final String query = "SELECT \"bool_col\", \"int_col\" FROM " + virtualSchemaJdbc + "." + MYSQL_SIMPLE_TABLE
                + " ORDER BY \"int_col\" LIMIT 3";
        final ResultSet expected = getExpectedResultSet(List.of("a BOOLEAN", "b DECIMAL(10,0)"), //
                List.of("true, -100", //
                        "false, -1", //
                        "true, 0"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testOrderByLimitOffset() throws SQLException {
        final String query = "SELECT \"bool_col\", \"int_col\" FROM " + virtualSchemaJdbc + "." + MYSQL_SIMPLE_TABLE
                + " ORDER BY \"int_col\" LIMIT 2 OFFSET 1";
        final ResultSet expected = getExpectedResultSet(List.of("a BOOLEAN", "b DECIMAL(10,0)"), //
                List.of("false, -1", //
                        "true, 0"));
        assertThat(getActualResultSet(query), matchesResultSet(expected));
    }

    @Test
    void testLeftShiftScalarFunction() {
        createSourceTable(List.of("INT_COL_1", "INT_COL_2"), List.of("INT", "INT"), new Object[][] { { 10, 3 } });
        this.virtualSchema = SETUP.createVirtualSchema(Collections.emptyMap(), MYSQL_SOURCE_SCHEMA);
        final String query = "SELECT BIT_LSHIFT(INT_COL_1, INT_COL_2) FROM " + this.virtualSchema.getName() + "."
                + MYSQL_SOURCE_TABLE;
        assertVsQuery(query, table().row(80).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    private void createSourceTable(final List<String> columnNames, final List<String> types, final Object[][] values) {
        this.sourceSchema = SETUP.getMySqlObjectFactory().createSchema(MYSQL_SOURCE_SCHEMA);
        final Table table = this.sourceSchema.createTable(MYSQL_SOURCE_TABLE, columnNames, types);
        for (final Object[] value : values) {
            table.insert(value);
        }
    }

    private ResultSet query(final String sql) throws SQLException {
        return SETUP.getExasolStatement().executeQuery(sql);
    }

    @Test
    void testRightShiftScalarFunction() {
        createSourceTable(List.of("INT_COL_1", "INT_COL_2"), List.of("INT", "INT"), new Object[][] { { 10, 3 } });
        this.virtualSchema = SETUP.createVirtualSchema(Collections.emptyMap(), MYSQL_SOURCE_SCHEMA);
        final String query = "SELECT BIT_RSHIFT(INT_COL_1, INT_COL_2) FROM " + this.virtualSchema.getName() + "."
                + MYSQL_SOURCE_TABLE;
        assertVsQuery(query, table().row(1).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    private void assertVsQuery(final String sql, final Matcher<ResultSet> expected) {
        try {
            assertThat(query(sql), expected);
        } catch (final SQLException exception) {
            fail("Unable to run assertion query: " + sql + "\nCaused by: " + exception.getMessage());
        }
    }

    @Test
    void testHourScalarFunction() {
        createSourceTable(List.of("TIMESTAMP_COL"), List.of("TIMESTAMP"), new Object[][] { { "2021-02-16 11:48:01" } });
        this.virtualSchema = SETUP.createVirtualSchema(Collections.emptyMap(), MYSQL_SOURCE_SCHEMA);
        final String query = "SELECT HOUR(timestamp_col) FROM " + this.virtualSchema.getName() + "."
                + MYSQL_SOURCE_TABLE;
        assertVsQuery(query, table().row(11).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    @Nested
    @DisplayName("Datatype tests")
    class DatatypeTest {
        @Test
        void testBit() throws SQLException {
            final String query = "SELECT \"BiT_Col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 BOOLEAN"), //
                    List.of("true", "true", "false", "false"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testTinyInt() throws SQLException {
            final String query = "SELECT \"tinyint_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DECIMAL(3,0)"), //
                    List.of("127", "-127", "0", "0"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testBoolean() throws SQLException {
            final String query = "SELECT \"BOOL_COL\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 BOOLEAN"), //
                    List.of("true", "false", "true", "false"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testSmallInt() throws SQLException {
            final String query = "SELECT \"smallint_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DECIMAL(5,0)"), //
                    List.of("32767", "-32768", "0", "0"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testMediumInt() throws SQLException {
            final String query = "SELECT \"mediumint_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DECIMAL(7,0)"), //
                    List.of("8388607", "-8388608", "0", "0"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testInt() throws SQLException {
            final String query = "SELECT \"int_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DECIMAL(10,0)"), //
                    List.of("2147483647", "-2147483648", "0", "0"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testBigInt() throws SQLException {
            final String query = "SELECT \"bigint_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DECIMAL(19,0)"), //
                    List.of("9223372036854775807", "-9223372036854775808", "0", "0"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testDecimal() throws SQLException {
            final String query = "SELECT \"decimal_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DECIMAL(5,2)"), //
                    List.of("999.99", "-999.99", "0", "0"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testFloat() throws SQLException {
            final String query = "SELECT \"float_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DOUBLE PRECISION"), //
                    List.of("999.0001", "-999.9999", "0", "0"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testDouble() throws SQLException {
            final String query = "SELECT \"double_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DOUBLE PRECISION"), //
                    List.of("999.00009", "-999.9999", "0", "0"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testDate() throws SQLException {
            final String query = "SELECT \"date_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DATE"), //
                    List.of("'1000-01-01'", "'9999-12-31'", "null", "null"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testDatetime() throws SQLException {
            final String query = "SELECT \"datetime_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 TIMESTAMP"), //
                    List.of("'1000-01-01 00:00:00'", "'9999-12-31 22:59:59'", "null", "null"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testTimestamp() throws SQLException {
            final String query = "SELECT \"timestamp_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 TIMESTAMP"), //
                    List.of("'1970-01-01 00:00:01.000000'", "'2037-01-19 03:14:08.0'", "null", "null"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testTime() throws SQLException {
            final String query = "SELECT \"time_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 TIMESTAMP"), //
                    List.of("'1970-01-01 16:59:59.000000'", "'1970-01-01 05:34:13.000000'", "null", "null"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testYear() throws SQLException {
            final String query = "SELECT \"year_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_NUMERIC_DATE_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 DATE"), //
                    List.of("'1901-01-01'", "'2155-01-01'", "'1901-01-01'", "'2069-01-01'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testUnsupported() {
            final String query = "SELECT * FROM " + virtualSchemaJdbc + "." + MYSQL_STRING_DATATYPES_TABLE;
            assertDoesNotThrow(() -> getActualResultSet(query));
        }

        @Test
        void testTinyText() throws SQLException {
            final String query = "SELECT \"tinytext_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_STRING_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 VARCHAR(100)"), //
                    List.of("'a'", "'b'", "'aaaaaaaaaaaaa'", "'a'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testText() throws SQLException {
            final String query = "SELECT \"text_col\" FROM " + virtualSchemaJdbc + "." + MYSQL_STRING_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 VARCHAR(65535)"), //
                    List.of("'text'", "'text2'", "'text3'", "'text'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testMediumText() throws SQLException {
            final String query = "SELECT \"mediumtext_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_STRING_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 VARCHAR(2000000)"), //
                    List.of("'mediumtext'", "'mediumtext2'", "NULL", "NULL"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testLongText() throws SQLException {
            final String query = "SELECT \"longtext_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_STRING_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 VARCHAR(2000000)"), //
                    List.of("'longtext'", "'longtext2'", "NULL", "NULL"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testEnum() throws SQLException {
            final String query = "SELECT \"enum_col\" FROM " + virtualSchemaJdbc + "." + MYSQL_STRING_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 CHAR"), //
                    List.of("'1'", "'2'", "'3'", "NULL"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testSet() throws SQLException {
            final String query = "SELECT \"set_col\" FROM " + virtualSchemaJdbc + "." + MYSQL_STRING_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 CHAR"), //
                    List.of("'1'", "'1'", "NULL", "NULL"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testVarchar() throws SQLException {
            final String query = "SELECT \"varchar_col\" FROM " + virtualSchemaJdbc + "."
                    + MYSQL_STRING_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 VARCHAR(100)"), //
                    List.of("'ab'", "'a'", "''", "NULL"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testChar() throws SQLException {
            final String query = "SELECT \"char_col\" FROM " + virtualSchemaJdbc + "." + MYSQL_STRING_DATATYPES_TABLE;
            final ResultSet expected = getExpectedResultSet(List.of("col1 CHAR(255)"), //
                    List.of("'asd24'", "'11111'", "''", "NULL"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }
    }

    @Nested
    @DisplayName("Join test")
    class JoinTest {
        @Test
        void testInnerJoin() throws SQLException {
            final String query = "SELECT * FROM " + virtualSchemaJdbc + "." + TABLE_JOIN_1 + " a INNER JOIN  "
                    + virtualSchemaJdbc + "." + TABLE_JOIN_2 + " b ON a.\"x\"=b.\"x\"";
            final ResultSet expected = getExpectedResultSet(
                    List.of("x INT", "y VARCHAR(100)", "a INT", "b VARCHAR(100)"), //
                    List.of("2,'bbb', 2,'bbb'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testInnerJoinWithProjection() throws SQLException {
            final String query = "SELECT b.\"y\" || " + virtualSchemaJdbc + "." + TABLE_JOIN_1 + ".\"y\" FROM "
                    + virtualSchemaJdbc + "." + TABLE_JOIN_1 + " INNER JOIN  " + virtualSchemaJdbc + "." + TABLE_JOIN_2
                    + " b ON " + virtualSchemaJdbc + "." + TABLE_JOIN_1 + ".\"x\"=b.\"x\"";
            final ResultSet expected = getExpectedResultSet(List.of("y VARCHAR(100)"), //
                    List.of("'bbbbbb'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testLeftJoin() throws SQLException {
            final String query = "SELECT * FROM " + virtualSchemaJdbc + "." + TABLE_JOIN_1 + " a LEFT OUTER JOIN "
                    + virtualSchemaJdbc + "." + TABLE_JOIN_2 + " b ON a.\"x\"=b.\"x\" ORDER BY a.\"x\"";
            final ResultSet expected = getExpectedResultSet(
                    List.of("x INT", "y VARCHAR(100)", "a INT", "b VARCHAR(100)"), //
                    List.of("1, 'aaa', null, null", //
                            "2, 'bbb', 2, 'bbb'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testRightJoin() throws SQLException {
            final String query = "SELECT * FROM " + virtualSchemaJdbc + "." + TABLE_JOIN_1 + " a RIGHT OUTER JOIN "
                    + virtualSchemaJdbc + "." + TABLE_JOIN_2 + " b ON a.\"x\"=b.\"x\" ORDER BY a.\"x\"";
            final ResultSet expected = getExpectedResultSet(
                    List.of("x INT", "y VARCHAR(100)", "a INT", "b VARCHAR(100)"), //
                    List.of("null, null, 3, 'ccc'", //
                            "2, 'bbb', 2, 'bbb'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testFullOuterJoin() throws SQLException {
            final String query = "SELECT * FROM " + virtualSchemaJdbc + "." + TABLE_JOIN_1 + " a FULL OUTER JOIN "
                    + virtualSchemaJdbc + "." + TABLE_JOIN_2 + " b ON a.\"x\"=b.\"x\" ORDER BY a.\"x\"";
            final ResultSet expected = getExpectedResultSet(
                    List.of("x INT", "y VARCHAR(100)", "a INT", "b VARCHAR(100)"), //
                    List.of("1, 'aaa', null, null", //
                            "2, 'bbb', 2, 'bbb'", //
                            "null, null, 3, 'ccc'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testRightJoinWithComplexCondition() throws SQLException {
            final String query = "SELECT * FROM " + virtualSchemaJdbc + "." + TABLE_JOIN_1 + " a RIGHT OUTER JOIN "
                    + virtualSchemaJdbc + "." + TABLE_JOIN_2
                    + " b ON a.\"x\"||a.\"y\"=b.\"x\"||b.\"y\" ORDER BY a.\"x\"";
            final ResultSet expected = getExpectedResultSet(
                    List.of("x INT", "y VARCHAR(100)", "a INT", "b VARCHAR(100)"), //
                    List.of("null, null, 3, 'ccc'", //
                            "2, 'bbb', 2, 'bbb'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }

        @Test
        void testFullOuterJoinWithComplexCondition() throws SQLException {
            final String query = "SELECT * FROM " + virtualSchemaJdbc + "." + TABLE_JOIN_1 + " a FULL OUTER JOIN "
                    + virtualSchemaJdbc + "." + TABLE_JOIN_2 + " b ON a.\"x\"-b.\"x\"=0 ORDER BY a.\"x\"";
            final ResultSet expected = getExpectedResultSet(
                    List.of("x INT", "y VARCHAR(100)", "a INT", "b VARCHAR(100)"), //
                    List.of("1, 'aaa', null, null", //
                            "2, 'bbb', 2, 'bbb'", //
                            "null, null, 3, 'ccc'"));
            assertThat(getActualResultSet(query), matchesResultSet(expected));
        }
    }
}