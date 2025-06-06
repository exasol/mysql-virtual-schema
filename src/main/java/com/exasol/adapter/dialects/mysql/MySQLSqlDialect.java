package com.exasol.adapter.dialects.mysql;

import static com.exasol.adapter.AdapterProperties.CATALOG_NAME_PROPERTY;
import static com.exasol.adapter.capabilities.AggregateFunctionCapability.*;
import static com.exasol.adapter.capabilities.LiteralCapability.*;
import static com.exasol.adapter.capabilities.MainCapability.*;
import static com.exasol.adapter.capabilities.PredicateCapability.*;
import static com.exasol.adapter.capabilities.ScalarFunctionCapability.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.capabilities.Capabilities;
import com.exasol.adapter.capabilities.ScalarFunctionCapability;
import com.exasol.adapter.dialects.*;
import com.exasol.adapter.dialects.rewriting.ImportIntoTemporaryTableQueryRewriter;
import com.exasol.adapter.dialects.rewriting.SqlGenerationContext;
import com.exasol.adapter.jdbc.*;
import com.exasol.errorreporting.ExaError;

/**
 * This class implements the SQL dialect of MySQL.
 *
 * @see <a href="https://dev.mysql.com/doc/">MySQL developer documentation</a>
 */
public class MySQLSqlDialect extends AbstractSqlDialect {
    static final String NAME = "MYSQL";
    private static final Set<ScalarFunctionCapability> DISABLED_SCALAR_FUNCTION = Set.of(
            // Not implemented (without or unknown reason)
            ADD, BIT_CHECK, BIT_LROTATE, BIT_NOT, BIT_RROTATE, BIT_SET, BIT_TO_NUM, CHR, COLOGNE_PHONETIC, COSH,
            CURRENT_CLUSTER, CURRENT_SCHEMA, CURRENT_SESSION, CURRENT_STATEMENT, DATE_TRUNC, DAY, DAYS_BETWEEN,
            DBTIMEZONE, DUMP, EDIT_DISTANCE, FLOAT_DIV, FROM_POSIX_TIME, HASHTYPE_MD5, HASHTYPE_SHA1, HASHTYPE_SHA256,
            HASHTYPE_SHA512, HASHTYPE_TIGER, HASH_MD5, HASH_SHA1, HASH_SHA256, HASH_SHA512, HASH_TIGER, HOURS_BETWEEN,
            INITCAP, IS_BOOLEAN, IS_DATE, IS_DSINTERVAL, IS_NUMBER, IS_TIMESTAMP, IS_YMINTERVAL, JSON_VALUE,
            MINUTES_BETWEEN, MIN_SCALE, MONTHS_BETWEEN, MULT, NEG, NULLIFZERO, NUMTODSINTERVAL, NUMTOYMINTERVAL,
            POSIX_TIME, SECONDS_BETWEEN, SESSIONTIMEZONE, SESSION_PARAMETER, SINH, ST_BOUNDARY, ST_FORCE2D, ST_ISRING,
            ST_SETSRID, SUB, SYS_GUID, TANH, TO_CHAR, TO_DATE, TO_DSINTERVAL, TO_NUMBER, TO_TIMESTAMP, TO_YMINTERVAL,
            TRANSLATE, TRUNC, TYPEOF, UNICODE, UNICODECHR, YEARS_BETWEEN, ZEROIFNULL);
    private static final Capabilities CAPABILITIES = createCapabilityList();

    /**
     * Create a new instance of the {@link MySQLSqlDialect}.
     *
     * @param connectionFactory factory for the JDBC connection to the Athena service
     * @param properties        user-defined adapter properties
     */
    public MySQLSqlDialect(final ConnectionFactory connectionFactory, final AdapterProperties properties,
                final ExaMetadata exaMetadata) {
        super(connectionFactory, properties, exaMetadata, Set.of(CATALOG_NAME_PROPERTY));
    }

    private static Capabilities createCapabilityList() {
        return Capabilities //
                .builder() //
                .addMain(SELECTLIST_PROJECTION, SELECTLIST_EXPRESSIONS, FILTER_EXPRESSIONS, AGGREGATE_SINGLE_GROUP,
                        AGGREGATE_GROUP_BY_COLUMN, AGGREGATE_GROUP_BY_EXPRESSION, AGGREGATE_GROUP_BY_TUPLE,
                        AGGREGATE_HAVING, ORDER_BY_COLUMN, ORDER_BY_EXPRESSION, LIMIT, LIMIT_WITH_OFFSET, JOIN,
                        JOIN_TYPE_INNER, JOIN_TYPE_LEFT_OUTER, JOIN_TYPE_RIGHT_OUTER, JOIN_CONDITION_EQUI) //
                .addLiteral(NULL, BOOL, DATE, TIMESTAMP, TIMESTAMP_UTC, DOUBLE, EXACTNUMERIC, STRING, INTERVAL) //
                .addPredicate(AND, OR, NOT, EQUAL, NOTEQUAL, LESS, LESSEQUAL, LIKE, BETWEEN, IS_NULL, IS_NOT_NULL) //
                .addScalarFunction(getEnabledScalarFunctionCapabilities()) //
                .addAggregateFunction(COUNT, SUM, MIN, MAX, AVG, STDDEV, STDDEV_POP, STDDEV_SAMP, VARIANCE, VAR_POP,
                        VAR_SAMP, COUNT_STAR, COUNT_DISTINCT) //
                .build();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Capabilities getCapabilities() {
        return CAPABILITIES;
    }

    @Override
    public StructureElementSupport supportsJdbcCatalogs() {
        return StructureElementSupport.MULTIPLE;
    }

    @Override
    public StructureElementSupport supportsJdbcSchemas() {
        return StructureElementSupport.NONE;
    }

    @Override
    // https://dev.mysql.com/doc/refman/8.0/en/identifiers.html
    public String applyQuote(final String identifier) {
        return "`" + identifier.replace("`", "``") + "`";
    }

    @Override
    public boolean requiresCatalogQualifiedTableNames(final SqlGenerationContext context) {
        return true;
    }

    @Override
    public boolean requiresSchemaQualifiedTableNames(final SqlGenerationContext context) {
        return false;
    }

    @Override
    public NullSorting getDefaultNullSorting() {
        return NullSorting.NULLS_SORTED_AT_END;
    }

    @Override
    // https://dev.mysql.com/doc/refman/8.0/en/string-literals.html
    public String getStringLiteral(final String value) {
        if (value == null) {
            return "NULL";
        } else {
            // We replace \ with \\ because we expect that the mode NO_BACKSLASH_ESCAPES is not used.
            return "'" + value.replace("\\", "\\\\").replace("'", "''") + "'";
        }
    }

    @Override
    protected RemoteMetadataReader createRemoteMetadataReader() {
        try {
            return new MySQLMetadataReader(this.connectionFactory.getConnection(), this.properties, this.exaMetadata);
        } catch (final SQLException exception) {
            throw new RemoteMetadataReaderException(ExaError.messageBuilder("E-VSMYSQL-1")
                    .message("Unable to create MySQL remote metadata reader. Caused by: {{cause|uq}}")
                    .parameter("cause", exception.getMessage()).toString(), exception);
        }
    }

    /**
     * Get all {@link ScalarFunctionCapability}s that are not explicitly excluded by {@link #DISABLED_SCALAR_FUNCTION}.
     *
     * @return list enabled scalar function capabilities
     */
    private static ScalarFunctionCapability[] getEnabledScalarFunctionCapabilities() {
        return Arrays.stream(ScalarFunctionCapability.values())
                .filter(Predicate.not(DISABLED_SCALAR_FUNCTION::contains)).toArray(ScalarFunctionCapability[]::new);
    }

    @Override
    protected QueryRewriter createQueryRewriter() {
        return new ImportIntoTemporaryTableQueryRewriter(this, createRemoteMetadataReader(), this.connectionFactory);
    }

    @Override
    public SqlGenerator getSqlGenerator(final SqlGenerationContext context) {
        return new MySQLSqlGenerationVisitor(this, context);
    }
}
