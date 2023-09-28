package com.exasol.adapter.dialects.mysql.metadata;

import static com.exasol.adapter.dialects.mysql.IntegrationTestConstants.MYSQL_DOCKER_IMAGE_REFERENCE;

import java.io.Closeable;
import java.sql.*;

import org.testcontainers.containers.MySQLContainer;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.BaseIdentifierConverter;
import com.exasol.adapter.dialects.mysql.MySQLColumnMetadataReader;
import com.exasol.adapter.dialects.mysql.charset.ColumnInspector;
import com.exasol.dbbuilder.dialects.mysql.MySqlObjectFactory;

class MySqlMetadataITSetup implements Closeable {

    private final MySQLContainer<?> mySqlContainer = new MySQLContainer<>(MYSQL_DOCKER_IMAGE_REFERENCE)
            .withUsername("root").withPassword("");
    private final Connection mySqlConnection;
    private final Statement mySqlStatement;
    private final MySqlObjectFactory mySqlObjectFactory;
    private final MySQLColumnMetadataReader columnMetadataReader;

    MySqlMetadataITSetup() {
        try {
            this.mySqlContainer.start();
            this.mySqlConnection = this.mySqlContainer.createConnection("");
            this.mySqlObjectFactory = new MySqlObjectFactory(this.mySqlConnection);
            this.mySqlStatement = this.mySqlConnection.createStatement();
            this.columnMetadataReader = new MySQLColumnMetadataReader(this.mySqlConnection,
                    AdapterProperties.emptyProperties(), BaseIdentifierConverter.createDefault());
        } catch (final SQLException exception) {
            throw new IllegalStateException("Failed to create test setup.", exception);
        }
    }

    @Override
    public void close() {
        try {
            this.mySqlConnection.close();
            // maybe provide option to not stop the container?
            this.mySqlContainer.stop();
        } catch (final SQLException exception) {
            throw new IllegalStateException("Failed to stop test setup.", exception);
        }
    }

    public MySqlObjectFactory getMySqlObjectFactory() {
        return this.mySqlObjectFactory;
    }

    public MySQLColumnMetadataReader getColumnMetadataReader() {
        return this.columnMetadataReader;
    }

    public ColumnInspector getColumnInspector(final String catalogName) {
        return ColumnInspector.from(this.mySqlConnection, catalogName);
    }

    public Statement getMySqlStatement() {
        return this.mySqlStatement;
    }
}