package com.exasol.adapter.dialects.mysql;

import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.BaseIdentifierConverter;
import com.exasol.adapter.jdbc.JDBCTypeDescription;
import com.exasol.adapter.metadata.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Types;

import static com.exasol.adapter.dialects.mysql.IntegrationTestConstants.EXASOL_VERSION;
import static com.exasol.adapter.dialects.mysql.MySQLColumnMetadataReader.TEXT_DATA_TYPE_SIZE;
import static com.exasol.adapter.metadata.DataType.MAX_EXASOL_VARCHAR_SIZE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MySQLColumnMetadataReaderTest {

    @Mock
    private ExaMetadata exaMetadataMock;

    @BeforeEach
    void beforeEach() {
        when(exaMetadataMock.getDatabaseVersion()).thenReturn(EXASOL_VERSION);
    }

    @Test
    void mapTimeWithoutSupportForTimestampPrecision() {
        when(exaMetadataMock.getDatabaseVersion()).thenReturn("7.1.30");
        final JDBCTypeDescription typeDescription = new JDBCTypeDescription(Types.TIME, 0, 9, 10, "TIME");
        assertThat(testee().mapJdbcType(typeDescription),
                equalTo(DataType.createTimestamp(false, 3)));
    }

    @Test
    void mapTimeWithMicrosecondsTimestampPrecision() {
        final JDBCTypeDescription typeDescription = new JDBCTypeDescription(Types.TIMESTAMP, 0, 26, 10, "TIME");
        assertThat(testee().mapJdbcType(typeDescription),
                equalTo(DataType.createTimestamp(false, 6)));
    }

    @Test
    void mapTimeWithZeroTimestampPrecision() {
        final JDBCTypeDescription typeDescription = new JDBCTypeDescription(Types.TIMESTAMP, 0, 19, 10, "TIME");
        assertThat(testee().mapJdbcType(typeDescription),
                equalTo(DataType.createTimestamp(false, 0)));
    }

    @Test
    void mapBinary() {
        final JDBCTypeDescription typeDescription = new JDBCTypeDescription(Types.BINARY, 0, 0, 10, "BINARY");
        assertThat(testee().mapJdbcType(typeDescription), equalTo(DataType.createUnsupported()));
    }

    @Test
    void mapText() {
        final JDBCTypeDescription typeDescription = new JDBCTypeDescription(Types.LONGVARCHAR, 0, 16383, 10, "TEXT");
        assertThat(testee().mapJdbcType(typeDescription).getSize(), equalTo(TEXT_DATA_TYPE_SIZE));
    }

    @Test
    void mapMediumText() {
        final JDBCTypeDescription typeDescription = new JDBCTypeDescription(Types.LONGVARCHAR, 0, 4194303, 10,
                "MEDIUMTEXT");
        assertThat(testee().mapJdbcType(typeDescription).getSize(), equalTo(MAX_EXASOL_VARCHAR_SIZE));
    }

    private MySQLColumnMetadataReader testee() {
        return new MySQLColumnMetadataReader(null, AdapterProperties.emptyProperties(),
                exaMetadataMock, BaseIdentifierConverter.createDefault());
    }

}