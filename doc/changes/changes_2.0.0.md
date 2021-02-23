# Virtual Schema for MySQL 1.0.0, released 2021-02-23

Code name: Removed `SQL_DIALECT` property

## Summary

The `SQL_DIALECT` property used when executing a `CREATE VIRTUAL SCHEMA` from the Exasol database is obsolete from this version. Please, do not provide this property anymore.

## Features

* #4: Unified error messages with `error-reporting-java` 
* #5: Added new capabilities for scalar and aggregate function.

## Refactoring

* #8: Enabled an integration test.

## Dependencies Updates

### Runtime Dependencies

* Updated `com.exasol:error-reporting-java:0.2.0` to `0.2.2`
* Updated `com.exasol:virtual-schema-common-jdbc:8.0.0` to `9.0.1`

### Test Dependencies

* Updated `com.exasol:exasol-testcontainers:3.3.1` to `3.5.0`
* Updated `com.exasol:test-db-builder-java:2.0.0` to `3.0.0`
* Updated `mysql:mysql-connector-java:8.0.22` to `8.0.23`
* Updated `org.junit.jupiter:junit-jupiter:5.7.0` to `5.7.1`
* Updated `org.mockito:mockito-junit-jupiter:3.6.28` to `3.7.7`
* Updated `org.testcontainers:junit-jupiter:1.15.0` to `1.15.2`
* Updated `org.testcontainers:mysql:1.15.0` to `1.15.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.2.2` to `1.4.0`
* Removed `junit:junit:4.13.1`


### Plugin Dependencies

* Added `com.exasol:error-code-crawler-maven-plugin:0.1.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.5` to `0.8.6`
* Updated ` org.codehaus.mojo:versions-maven-plugin:2.7` to `2.8.1`

