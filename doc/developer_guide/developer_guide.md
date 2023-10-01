# MYSQL Virtual Schema Developer Guide

Please see [Exasol Testcontainers User Guide](https://github.com/exasol/exasol-testcontainers/doc/user_guide/user_guide.md](https://github.com/exasol/mysql-virtual-schema/blob/main/src/test/java/com/exasol/adapter/dialects/mysql/MySQLVirtualSchemaIntegrationTestSetup.java) for developer instructions. For VSMYSQL in particular there are currently no specific instructions.

## Version of the Exasol Database to Use

Exasol Testcontainers defines the default version of Exasol Database as Docker Container to use in the integration tests. <br />
Typically it will be the latest available version at the moment the version of ETC used by VSMYSQL was released.

There are multiple ways to override this
* Add a version string when calling the contructor of `ExasolContainer` in [`MySQLVirtualSchemaIntegrationTestSetup`](https://github.com/exasol/mysql-virtual-schema/blob/main/src/test/java/com/exasol/adapter/dialects/mysql/MySQLVirtualSchemaIntegrationTestSetup.java).
* Set Java system property `com.exasol.dockerdb.image`.

For details see section _Overriding the Docker Image in a Build_ in the [ETC User Guide](https://github.com/exasol/exasol-testcontainers/doc/user_guide/user_guide.md#overriding-the-docker-image-in-a-build).

## Debug Output

In order to activate debug output to a remote machine please refer to section _Debug Output_ in the Test [DB Builder User Guide](https://github.com/exasol/test-db-builder-java/blob/main/doc/user_guide/user_guide.md#debug-output).