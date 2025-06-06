package com.exasol.adapter.dialects.mysql;

import static com.exasol.adapter.dialects.mysql.IntegrationTestConstants.EXASOL_VERSION;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import com.exasol.ExaMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.*;
import com.exasol.adapter.jdbc.BaseTableMetadataReader;

@ExtendWith(MockitoExtension.class)
class MySQLMetadataReaderTest {
    private MySQLMetadataReader reader;

    @Mock
    private Connection connectionMock;
    @Mock
    private ExaMetadata exaMetadataMock;

    @BeforeEach
    void beforeEach() {
        when(exaMetadataMock.getDatabaseVersion()).thenReturn(EXASOL_VERSION);
        this.reader = new MySQLMetadataReader(connectionMock, AdapterProperties.emptyProperties(), exaMetadataMock);
    }

    @Test
    void testGetTableMetadataReader() {
        assertThat(this.reader.getTableMetadataReader(), instanceOf(BaseTableMetadataReader.class));
    }

    @Test
    void testGetColumnMetadataReader() {
        assertThat(this.reader.getColumnMetadataReader(), instanceOf(MySQLColumnMetadataReader.class));
    }

    @Test
    void testGetSupportedTableTypes() {
        assertThat(this.reader.getSupportedTableTypes(), emptyIterableOf(String.class));
    }

    @Test
    void testCreateIdentifierConverter() {
        final IdentifierConverter converter = this.reader.getIdentifierConverter();
        assertAll(() -> assertThat(converter, instanceOf(BaseIdentifierConverter.class)),
                () -> assertThat(converter.getQuotedIdentifierHandling(),
                        equalTo(IdentifierCaseHandling.INTERPRET_CASE_SENSITIVE)),
                () -> assertThat(converter.getUnquotedIdentifierHandling(),
                        equalTo(IdentifierCaseHandling.INTERPRET_CASE_SENSITIVE)));
    }
}