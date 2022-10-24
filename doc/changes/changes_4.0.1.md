# Virtual Schema for MySQL 4.0.1, released 2022-10-24

Code name: Dependency Updates

## Summary

Updated dependencies to fix vulnerabilities.

## Features

* #23: Fixed CVE-2022-3171 reported for `com.google.protobuf:protobuf-java` by updating  `com.mysql:mysql-connector-j`.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:9.0.5` to `10.0.1`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.1.1` to `6.2.0`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.1` to `1.5.2`
* Updated `com.exasol:test-db-builder-java:3.3.2` to `3.4.0`
* Updated `com.exasol:udf-debugging-java:0.6.2` to `0.6.4`
* Updated `com.exasol:virtual-schema-common-jdbc:9.0.5` to `10.0.1`
* Updated `com.exasol:virtual-schema-shared-integration-tests:2.2.0` to `2.2.2`
* Added `com.google.protobuf:protobuf-java:3.21.8`
* Added `com.mysql:mysql-connector-j:8.0.31`
* Removed `mysql:mysql-connector-java:8.0.29`
* Updated `org.junit.jupiter:junit-jupiter:5.8.2` to `5.9.1`
* Updated `org.mockito:mockito-junit-jupiter:4.6.1` to `4.8.1`
* Updated `org.testcontainers:junit-jupiter:1.17.2` to `1.17.5`
* Updated `org.testcontainers:mysql:1.17.2` to `1.17.5`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.4.6` to `2.8.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.11.0` to `2.10.0`
