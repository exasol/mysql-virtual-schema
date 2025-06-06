package com.exasol.adapter.dialects.mysql;

import java.sql.Connection;
import java.sql.Types;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.IdentifierConverter;
import com.exasol.adapter.jdbc.BaseColumnMetadataReader;
import com.exasol.adapter.jdbc.JDBCTypeDescription;
import com.exasol.adapter.metadata.DataType;

/**
 * This class implements MySQL-specific reading of column metadata.
 */
public class MySQLColumnMetadataReader extends BaseColumnMetadataReader {
    private static final String TEXT_DATA_TYPE_NAME = "TEXT";
    protected static final int TEXT_DATA_TYPE_SIZE = 65535;

    private static final int TIME_LENGTH_WITHOUT_FRACTION = "hh:mm:ss.".length();
    private static final int TIMESTAMP_LENGTH_WITHOUT_FRACTION = "yyyy-dd-mm hh:mm:ss.".length();

    /**
     * Create a new instance of the {@link MySQLColumnMetadataReader}.
     *
     * @param connection          JDBC connection through which the column metadata is read from the remote database
     * @param properties          user-defined adapter properties
     * @param identifierConverter converter between source and Exasol identifiers
     */
    public MySQLColumnMetadataReader(final Connection connection, final AdapterProperties properties,
                final ExaMetadata exaMetadata, final IdentifierConverter identifierConverter) {
        super(connection, properties, exaMetadata, identifierConverter);
    }

    @Override
    public DataType mapJdbcType(final JDBCTypeDescription jdbcTypeDescription) {
        switch (jdbcTypeDescription.getJdbcType()) {
        case Types.TIME:
            return convertTimestamp(jdbcTypeDescription, TIME_LENGTH_WITHOUT_FRACTION);
        case Types.TIMESTAMP:
            return convertTimestamp(jdbcTypeDescription, TIMESTAMP_LENGTH_WITHOUT_FRACTION);
        case Types.BINARY:
            return DataType.createUnsupported();
        case Types.LONGVARCHAR:
            return convertVarChar(jdbcTypeDescription);
        default:
            return super.mapJdbcType(jdbcTypeDescription);
        }
    }

    private DataType convertVarChar(final JDBCTypeDescription jdbcTypeDescription) {
        final int size = getVarcharSize(jdbcTypeDescription);
        final DataType.ExaCharset charset = DataType.ExaCharset.UTF8;
        if (size <= DataType.MAX_EXASOL_VARCHAR_SIZE) {
            final int precision = getVarcharPrecision(size);
            return DataType.createVarChar(precision, charset);
        } else {
            return DataType.createVarChar(DataType.MAX_EXASOL_VARCHAR_SIZE, charset);
        }
    }

    private int getVarcharPrecision(final int size) {
        if (size == 0) {
            return DataType.MAX_EXASOL_VARCHAR_SIZE;
        } else {
            return size;
        }
    }

    private int getVarcharSize(final JDBCTypeDescription jdbcTypeDescription) {
        final String typeName = jdbcTypeDescription.getTypeName();
        if (typeName.equals(TEXT_DATA_TYPE_NAME)) {
            return TEXT_DATA_TYPE_SIZE;
        } else {
            return jdbcTypeDescription.getPrecisionOrSize();
        }
    }

    private DataType convertTimestamp(final JDBCTypeDescription jdbcTypeDescription, final int lengthWithoutFraction) {
        if (supportsTimestampsWithNanoPrecision()) {
            // FSP with MySQL has to be determined from COLUMN_SIZE subtracting the length without the fractional part.
            final int fractionalPrecision = Math.max(jdbcTypeDescription.getPrecisionOrSize() - lengthWithoutFraction, 0);
            return DataType.createTimestamp(false, fractionalPrecision);
        }
        return DataType.createTimestamp(false, 3);
    }
}
