# MYSQL Virtual Schema Developer Guide

For VSMYSQL in particular there are currently no specific instructions but the following sections will point you to important documentation in some of the dedendencies used by VSMYSQL.

In general the [Exasol Testcontainers User Guide](https://github.com/exasol/exasol-testcontainers/blob/main/doc/user_guide/user_guide.md) can be used as a starting point for developer instructions.

## Version of the Exasol Database to Use

Exasol Testcontainers defines the default version of Exasol Database as Docker Container to use in the integration tests.

Typically it will be the latest available version at the moment the version of ETC used by VSMYSQL was released:

VSMYSQL &ndash;_(dependency)_&rarr; ETC &ndash;_(defines version)_&rarr; Exasol Docker DB

There are multiple ways to override the version of the Exasol Database in VSMYSQL:
* Set Java system property `com.exasol.dockerdb.image`.
* Add a version string when calling the contructor of `ExasolContainer` in [`MySQLVirtualSchemaIntegrationTestSetup`](https://github.com/exasol/mysql-virtual-schema/blob/main/src/test/java/com/exasol/adapter/dialects/mysql/MySQLVirtualSchemaIntegrationTestSetup.java).

For details see section _Overriding the Docker Image in a Build_ in the [ETC User Guide](https://github.com/exasol/exasol-testcontainers/blob/main/doc/user_guide/user_guide.md#overriding-the-docker-image-in-a-build).

## Debug Output

In order to activate debug output to a remote machine please refer to section _Debug Output_ in the [Test DB Builder User Guide](https://github.com/exasol/test-db-builder-java/blob/main/doc/user_guide/user_guide.md#debug-output).