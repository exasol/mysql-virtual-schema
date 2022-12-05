package com.exasol.adapter.dialects.mysql;

import static com.exasol.adapter.dialects.mysql.IntegrationTestConstants.*;
import static com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language.JAVA;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testcontainers.containers.MySQLContainer;

import com.exasol.adapter.dialects.mysql.charset.ColumnInspector;
import com.exasol.adapter.dialects.mysql.charset.TableWriterWithCharacterSet;
import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.containers.ExasolService;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.dbbuilder.dialects.mysql.MySqlImmediateDatabaseObjectWriter;
import com.exasol.dbbuilder.dialects.mysql.MySqlObjectFactory;
import com.exasol.errorreporting.ExaError;
import com.exasol.udfdebugging.UdfTestSetup;
import com.github.dockerjava.api.model.ContainerNetwork;

/**
 * This class contains the common integration test setup for all MySQL integration test.
 */
public class MySQLVirtualSchemaIntegrationTestSetup implements Closeable {
    private static final int MYSQL_PORT = 3306;
    private static final String SCHEMA_EXASOL = "SCHEMA_EXASOL";
    private static final String ADAPTER_SCRIPT_EXASOL = "ADAPTER_SCRIPT_EXASOL";
    private static final Logger LOGGER = Logger.getLogger(MySQLVirtualSchemaIntegrationTestSetup.class.getName());

    private static final boolean USE_JACOCO = true;
    private final Statement mySqlStatement;
    private final MySQLContainer<?> mySqlContainer = new MySQLContainer<>(MYSQL_DOCKER_IMAGE_REFERENCE)
            .withUsername("root").withPassword("");
    private final ExasolContainer<? extends ExasolContainer<?>> exasolContainer = new ExasolContainer<>(
            EXASOL_DOCKER_IMAGE_REFERENCE).withRequiredServices(ExasolService.BUCKETFS, ExasolService.UDF)
                    .withReuse(true);
    private final Connection exasolConection;
    private final Statement exasolStatement;
    private final AdapterScript adapterScript;
    private final ConnectionDefinition connectionDefinition;
    private final ExasolObjectFactory exasolFactory;
    private final MySqlObjectFactory mySqlObjectFactory;
    private final Connection mySqlConnection;
    private int virtualSchemaCounter = 0;

    MySQLVirtualSchemaIntegrationTestSetup() {
        try {
            this.exasolContainer.start();
            this.mySqlContainer.start();
            final Bucket bucket = this.exasolContainer.getDefaultBucket();
            uploadDriverToBucket(bucket);
            uploadVsJarToBucket(bucket);
            this.exasolConection = this.exasolContainer.createConnection("");
            this.exasolStatement = this.exasolConection.createStatement();
            this.mySqlConnection = this.mySqlContainer.createConnection("");
            this.mySqlStatement = this.mySqlConnection.createStatement();
            final ExasolObjectConfiguration.Builder builder = ExasolObjectConfiguration.builder();
            if (USE_JACOCO) {
                final UdfTestSetup udfTestSetup = new UdfTestSetup(getTestHostIpFromInsideExasol(),
                        this.exasolContainer.getDefaultBucket(), this.exasolConection);
                builder.withJvmOptions(udfTestSetup.getJvmOptions());
            }
            this.exasolFactory = new ExasolObjectFactory(this.exasolContainer.createConnection(""), builder.build());
            final ExasolSchema exasolSchema = this.exasolFactory.createSchema(SCHEMA_EXASOL);
            this.mySqlObjectFactory = new MySqlObjectFactory(this.mySqlConnection);
            this.adapterScript = createAdapterScript(exasolSchema);
            final String connectionString = "jdbc:mysql://" + this.exasolContainer.getHostIp() + ":"
                    + this.mySqlContainer.getMappedPort(MYSQL_PORT) + "/" + this.mySqlContainer.getDatabaseName();
            this.connectionDefinition = this.exasolFactory.createConnectionDefinition("MYSQL_CONNECTION",
                    connectionString, this.mySqlContainer.getUsername(), this.mySqlContainer.getPassword());
            logConnectionInfo();
        } catch (final SQLException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to created MySQL test setup.", exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted");
        }
    }

    private static void uploadDriverToBucket(final Bucket bucket)
            throws InterruptedException, TimeoutException, BucketAccessException {
        try {
            bucket.uploadStringContent(JDBC_DRIVER_CONFIGURATION_FILE_CONTENT,
                    "drivers/jdbc/" + JDBC_DRIVER_CONFIGURATION_FILE_NAME);
            bucket.uploadFile(JDBC_DRIVER_PATH, "drivers/jdbc/" + JDBC_DRIVER_NAME);
        } catch (final BucketAccessException | FileNotFoundException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("F-VSMYSQL-2")
                            .message("An error occurred while uploading the jdbc driver to the bucket.")
                            .mitigation("Make sure the {{JDBC_DRIVER_PATH}} file exists.")
                            .parameter("JDBC_DRIVER_PATH", JDBC_DRIVER_PATH)
                            .mitigation("You can generate it by executing the integration test with maven.").toString(),
                    exception);
        }
    }

    private static void uploadVsJarToBucket(final Bucket bucket) {
        try {
            bucket.uploadFile(PATH_TO_VIRTUAL_SCHEMAS_JAR, VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION);
        } catch (final FileNotFoundException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to upload jar to bucket " + bucket, exception);
        }
    }

    private void logConnectionInfo() {
        LOGGER.log(Level.INFO,
                "You can use one of the following statements to connect to the test-database using dbeaver:");
        LOGGER.log(Level.INFO,
                "dbeaver-ce --connect \"driver=mysql|database={0}|name=MySqlFromTest|openConsole=true|user={1}|password={2}|host={3}|port={4}\"",
                new Object[] { this.mySqlContainer.getDatabaseName(), this.mySqlContainer.getUsername(),
                        this.mySqlContainer.getPassword(), this.mySqlContainer.getHost(),
                        String.valueOf(this.mySqlContainer.getMappedPort(MYSQL_PORT)) });
        LOGGER.log(Level.INFO,
                "dbeaver-ce --connect \"driver=exasol|name=ExasolFromTest|openConsole=true|user={0}|password={1}|host={2}|port={3}\"",
                new Object[] { this.exasolContainer.getUsername(), this.exasolContainer.getPassword(),
                        this.exasolContainer.getHost(), String.valueOf(this.exasolContainer
                                .getMappedPort(this.exasolContainer.getDefaultInternalDatabasePort())) });
    }

    private AdapterScript createAdapterScript(final ExasolSchema schema) {
        final String content = "%scriptclass com.exasol.adapter.RequestDispatcher;\n" //
                + "%jar /buckets/bfsdefault/default/" + VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION + ";\n" //
                + "%jar /buckets/bfsdefault/default/drivers/jdbc/" + JDBC_DRIVER_NAME + ";\n";
        return schema.createAdapterScript(ADAPTER_SCRIPT_EXASOL, JAVA, content);
    }

    public MySqlObjectFactory getMySqlObjectFactory() {
        return this.mySqlObjectFactory;
    }

    public Statement getMySqlStatement() {
        return this.mySqlStatement;
    }

    public Statement getExasolStatement() {
        return this.exasolStatement;
    }

    public ExasolContainer<? extends ExasolContainer<?>> getExasolContainer() {
        return this.exasolContainer;
    }

    public VirtualSchema createVirtualSchema(final Map<String, String> additionalProperties,
            final String forMySqlSchema) {
        final Map<String, String> properties = new HashMap<>(Map.of("CATALOG_NAME", forMySqlSchema));
        properties.putAll(additionalProperties);
        return this.exasolFactory.createVirtualSchemaBuilder("MYSQL_VIRTUAL_SCHEMA_" + (this.virtualSchemaCounter++))
                .adapterScript(this.adapterScript).connectionDefinition(this.connectionDefinition)
                .properties(properties).build();
    }

    public MySqlImmediateDatabaseObjectWriter getTableWriterWithCharacterSet(final String characterSet) {
        return new TableWriterWithCharacterSet(this.mySqlConnection, characterSet);
    }

    public ColumnInspector getColumnInspector(final String catalogName) {
        return ColumnInspector.from(this.mySqlConnection, catalogName);
    }

    @Override
    public void close() {
        try {
            this.exasolStatement.close();
            this.exasolConection.close();
            this.mySqlStatement.close();
            this.mySqlConnection.close();
            this.exasolContainer.stop();
            this.mySqlContainer.stop();
        } catch (final SQLException exception) {
            throw new IllegalStateException("Failed to stop test setup.", exception);
        }
    }

    private String getTestHostIpFromInsideExasol() {
        final Map<String, ContainerNetwork> networks = this.exasolContainer.getContainerInfo().getNetworkSettings()
                .getNetworks();
        if (networks.size() == 0) {
            return null;
        }
        return networks.values().iterator().next().getGateway();
    }
}
