package com.exasol.adapter.dialects.mysql.metadata;

import static com.exasol.adapter.dialects.mysql.metadata.MySqlMetadataIT.f;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.adapter.jdbc.JDBCTypeDescription;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.DataType.ExaCharset;

class SizeDetector {
    private static final Logger LOGGER = Logger.getLogger(SizeDetector.class.getName());
    private static final String TEXT_DATA_TYPE_NAME = "TEXT";
    protected static final int TEXT_DATA_TYPE_SIZE = 65535;

    private final ResultSetMetaData metadata;
    private final int columnNumber;

    public SizeDetector(final ResultSetMetaData metadata, final int columnNumber) {
        this.metadata = metadata;
        this.columnNumber = columnNumber;
    }

    public JDBCTypeDescription getJdbcTypeDescription() throws SQLException {
        return getJdbcTypeDescription(this.metadata, this.columnNumber);
    }

    public int getVarcharSize() throws SQLException {
        return getVarcharSize(getJdbcTypeDescription());
    }

    // VSCJDBC ResultSetMetaDataReader always sets JDBCTypeDescription.byteSize to 0
    public ExaCharset getExaCharset() throws SQLException {
        final int octetLength = getJdbcTypeDescription().getByteSize(); // 0
        final int size = getVarcharSize();
        final ExaCharset charset = (octetLength == size) ? DataType.ExaCharset.ASCII : DataType.ExaCharset.UTF8;
        final String report = String.join(", ", List.of( //
                f("octetLength", octetLength), //
                f("size", size), //
                f("charset", charset) //
        ));
        LOGGER.info(report);
        return charset;
    }

    private static JDBCTypeDescription getJdbcTypeDescription(final ResultSetMetaData metadata, final int columnNumber)
            throws SQLException {
        final int jdbcType = metadata.getColumnType(columnNumber);
        final int jdbcPrecisions = metadata.getPrecision(columnNumber);
        final int jdbcScales = metadata.getScale(columnNumber);
        return new JDBCTypeDescription(jdbcType, jdbcScales, jdbcPrecisions, 0,
                metadata.getColumnTypeName(columnNumber));
    }

    private int getVarcharSize(final JDBCTypeDescription jdbcTypeDescription) {
        final String typeName = jdbcTypeDescription.getTypeName();
        if (typeName.equals(TEXT_DATA_TYPE_NAME)) {
            return TEXT_DATA_TYPE_SIZE;
        } else {
            return jdbcTypeDescription.getPrecisionOrSize();
        }
    }
}
