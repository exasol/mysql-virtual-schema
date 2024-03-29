package com.exasol.adapter.dialects.mysql;

import java.nio.file.Path;

public final class IntegrationTestConstants {
    public static final String VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION = "virtual-schema-dist-12.0.0-mysql-5.0.1.jar";
    public static final String MYSQL_DOCKER_IMAGE_REFERENCE = "mysql:8.2.0";
    public static final Path PATH_TO_VIRTUAL_SCHEMAS_JAR = Path.of("target", VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION);
    public static final String SCHEMA_EXASOL = "SCHEMA_EXASOL";
    public static final String ADAPTER_SCRIPT_EXASOL = "ADAPTER_SCRIPT_EXASOL";
    public static final String TABLE_JOIN_1 = "TABLE_JOIN_1";
    public static final String TABLE_JOIN_2 = "TABLE_JOIN_2";
    public static final String DOCKER_IP_ADDRESS = "172.17.0.1";

    public static final String JDBC_DRIVER_NAME = "mysql-connector-j.jar";
    public static final Path JDBC_DRIVER_PATH = Path.of("target", "mysql-driver", JDBC_DRIVER_NAME);
    public static final String JDBC_DRIVER_CONFIGURATION_FILE_NAME = "settings.cfg";
    public static final String JDBC_DRIVER_CONFIGURATION_FILE_CONTENT = "DRIVERNAME=MYSQL\n" //
            + "JAR=" + JDBC_DRIVER_NAME + "\n" //
            + "DRIVERMAIN=com.mysql.jdbc.Driver\n" //
            + "PREFIX=jdbc:mysql:\n" //
            + "NOSECURITY=YES\n" //
            + "FETCHSIZE=100000\n" //
            + "INSERTSIZE=-1\n";

    private IntegrationTestConstants() {
        // intentionally left empty
    }
}
