package com.exasol.adapter.dialects.mysql;

import static com.exasol.adapter.dialects.mysql.IntegrationTestConstants.EXASOL_VERSION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.lenient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterException;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.*;
import com.exasol.adapter.dialects.rewriting.SqlGenerationContext;
import com.exasol.adapter.jdbc.ConnectionFactory;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.DataType.ExaCharset;
import com.exasol.adapter.sql.*;

@ExtendWith(MockitoExtension.class)
class MySQLSqlGenerationVisitorTest {
    private SqlNodeVisitor<String> visitor;

    @Mock
    private ExaMetadata exaMetadataMock;

    @BeforeEach
    void beforeEach(@Mock final ConnectionFactory connectionFactoryMock) {
        lenient().when(exaMetadataMock.getDatabaseVersion()).thenReturn(EXASOL_VERSION);
        @SuppressWarnings("resource") // Closing not necessary for mocks
        final SqlDialectFactory dialectFactory = new MySQLSqlDialectFactory();
        final JDBCAdapterContext adapterContext = JDBCAdapterContext.builder()
                .connectionFactory(connectionFactoryMock)
                .properties(AdapterProperties.emptyProperties())
                .metadata(exaMetadataMock)
                .build();
        final SqlDialect dialect = dialectFactory.createSqlDialect(adapterContext);
        final SqlGenerationContext context = new SqlGenerationContext("test_catalog", "test_schema", false);
        this.visitor = new MySQLSqlGenerationVisitor(dialect, context);
    }

    @Test
    void testRewriteDivFunction() throws AdapterException {
        final List<SqlNode> arguments = new ArrayList<>();
        arguments.add(new SqlLiteralDouble(10.5));
        arguments.add(new SqlLiteralDouble(10.10));
        final SqlFunctionScalar sqlFunctionScalar = new SqlFunctionScalar(ScalarFunction.DIV, arguments);
        assertThat(this.visitor.visit(sqlFunctionScalar), equalTo("1.05E1 DIV 1.01E1"));
    }

    @ParameterizedTest
    @CsvSource({ "BIT_LSHIFT, <<", "BIT_RSHIFT, >>" })
    void testRewriteDivFunction(final String functionName, final String expectedOperator) throws AdapterException {
        final List<SqlNode> arguments = List.of(new SqlLiteralExactnumeric(BigDecimal.TEN),
                new SqlLiteralExactnumeric(BigDecimal.ONE));
        final SqlFunctionScalar sqlFunctionScalar = new SqlFunctionScalar(ScalarFunction.valueOf(functionName),
                arguments);
        assertThat(this.visitor.visit(sqlFunctionScalar), equalTo("10 " + expectedOperator + " 1"));
    }

    @ParameterizedTest
    @CsvSource({ "ADD_DAYS, DAY", "ADD_HOURS, HOUR", "ADD_MINUTES, MINUTE", "ADD_SECONDS, SECOND",
            "ADD_WEEKS, WEEK", "ADD_YEARS, YEAR" })
    void testRewriteDateAddFunctions(final ScalarFunction function, final String unit) throws AdapterException {
        final SqlFunctionScalar scalarFunction = new SqlFunctionScalar(function,
                List.of(new SqlLiteralString("2024-01-31"), new SqlLiteralExactnumeric(BigDecimal.TEN)));
        assertThat(this.visitor.visit(scalarFunction), equalTo("DATE_ADD('2024-01-31', INTERVAL "
                + "(SIGN(10) * FLOOR(ABS(10) + 0.5)) " + unit + ")"));
    }

    @Test
    void testRoundsDateAddOffset() throws AdapterException {
        final SqlFunctionScalar scalarFunction = new SqlFunctionScalar(ScalarFunction.ADD_HOURS,
                List.of(new SqlLiteralString("2024-01-31"), new SqlLiteralDouble(-1.5)));
        assertThat(this.visitor.visit(scalarFunction), equalTo("DATE_ADD('2024-01-31', INTERVAL "
                + "(SIGN(-1.5E0) * FLOOR(ABS(-1.5E0) + 0.5)) HOUR)"));
    }

    @Test
    void testRewriteAddMonthsPreservingLastDayOfMonth() throws AdapterException {
        final SqlFunctionScalar scalarFunction = new SqlFunctionScalar(ScalarFunction.ADD_MONTHS,
                List.of(new SqlLiteralString("2024-02-29"), new SqlLiteralExactnumeric(BigDecimal.ONE)));
        final String dateAdd = "DATE_ADD('2024-02-29', INTERVAL (SIGN(1) * FLOOR(ABS(1) + 0.5)) MONTH)";
        final String expectedSql = "CASE WHEN DATE('2024-02-29') = LAST_DAY('2024-02-29') THEN DATE_ADD(" + dateAdd
                + ", INTERVAL (DAY(LAST_DAY(" + dateAdd + ")) - DAY(" + dateAdd + ")) DAY) ELSE " + dateAdd
                + " END";
        assertThat(this.visitor.visit(scalarFunction), equalTo(expectedSql));
    }

    @Test
    void testRewriteWeekWithIsoMode() throws AdapterException {
        final SqlFunctionScalar scalarFunction = new SqlFunctionScalar(ScalarFunction.WEEK,
                List.of(new SqlLiteralString("2024-01-01")));
        assertThat(this.visitor.visit(scalarFunction), equalTo("WEEK('2024-01-01', 3)"));
    }

    @Test
    void testRewriteYearAsSignedInteger() throws AdapterException {
        final SqlFunctionScalar scalarFunction = new SqlFunctionScalar(ScalarFunction.YEAR,
                List.of(new SqlLiteralString("2024-01-01")));
        assertThat(this.visitor.visit(scalarFunction), equalTo("CAST(YEAR('2024-01-01') AS SIGNED)"));
    }

    @Test
    void testRewriteRoundForDoubleValues() throws AdapterException {
        final SqlFunctionScalar scalarFunction = new SqlFunctionScalar(ScalarFunction.ROUND,
                List.of(new SqlLiteralDouble(0.5)));
        assertThat(this.visitor.visit(scalarFunction), equalTo("SIGN(5E-1) * FLOOR(ABS(5E-1) + 0.5)"));
    }

    @ParameterizedTest
    @CsvSource({ "true, false, '1'",
            "true, true, '1 IS NULL ASC, 1'",
            "false, false, '1 IS NULL DESC, 1 DESC'",
            "false, true, '1 DESC'" })
    void testOrderByNullSorting(final boolean ascending, final boolean nullsLast, final String expectedOrderBy)
            throws AdapterException {
        final SqlOrderBy orderBy = new SqlOrderBy(List.of(new SqlLiteralExactnumeric(BigDecimal.ONE)),
                List.of(ascending), List.of(nullsLast));
        assertThat(this.visitor.visit(orderBy), equalTo("ORDER BY " + expectedOrderBy));
    }

    @Test
    void testOrderByEmpty() throws AdapterException {
        final SqlOrderBy orderBy = new SqlOrderBy(List.of(), List.of(), List.of());
        assertThat(this.visitor.visit(orderBy), equalTo("ORDER BY "));
    }

    @Test
    void testOrderByMultipleExpressions() throws AdapterException {
        final SqlOrderBy orderBy = new SqlOrderBy(
                List.of(new SqlLiteralExactnumeric(BigDecimal.ONE), new SqlLiteralExactnumeric(BigDecimal.valueOf(2)),
                        new SqlLiteralExactnumeric(BigDecimal.valueOf(3))),
                List.of(true, false, false), List.of(true, false, true));
        assertThat(this.visitor.visit(orderBy),
                equalTo("ORDER BY 1 IS NULL ASC, 1, 2 IS NULL DESC, 2 DESC, 3 DESC"));
    }

    @Test
    void testRewriteCharacterCast() throws AdapterException {
        final SqlFunctionScalarCast cast = new SqlFunctionScalarCast(DataType.createVarChar(254, ExaCharset.UTF8),
                new SqlLiteralExactnumeric(BigDecimal.TEN));
        assertThat(this.visitor.visit(cast), equalTo("CAST(10 AS CHAR(254))"));
    }
}
