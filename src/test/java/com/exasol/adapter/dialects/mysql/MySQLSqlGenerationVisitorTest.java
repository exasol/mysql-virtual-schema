package com.exasol.adapter.dialects.mysql;

import static com.exasol.adapter.dialects.mysql.IntegrationTestConstants.EXASOL_VERSION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.lenient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.exasol.ExaMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.SqlDialect;
import com.exasol.adapter.dialects.SqlDialectFactory;
import com.exasol.adapter.dialects.rewriting.SqlGenerationContext;
import com.exasol.adapter.jdbc.ConnectionFactory;
import com.exasol.adapter.sql.*;

@ExtendWith(MockitoExtension.class)
class MySQLSqlGenerationVisitorTest {
    private SqlNodeVisitor<String> visitor;

    @Mock
    private ExaMetadata exaMetadataMock;

    @BeforeEach
    void beforeEach(@Mock final ConnectionFactory connectionFactoryMock) {
        lenient().when(exaMetadataMock.getDatabaseVersion()).thenReturn(EXASOL_VERSION);
        final SqlDialectFactory dialectFactory = new MySQLSqlDialectFactory();
        final SqlDialect dialect = dialectFactory.createSqlDialect(connectionFactoryMock,
                AdapterProperties.emptyProperties(), exaMetadataMock);
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
}