# Virtual Schema for MySQL 4.1.4, released 2023-09-29

Code name: Fix CVE-2023-42503 in test dependency

## Summary

This release fixes CVE-2023-42503 in test dependency `org.apache.commons:commons-compress`.

## Security

* #42: Fixed CVE-2023-42503 in test dependency `org.apache.commons:commons-compress`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:11.0.1` to `11.0.2`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.1` to `6.6.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.0` to `1.6.1`
* Updated `com.exasol:test-db-builder-java:3.4.2` to `3.5.1`
* Updated `com.exasol:udf-debugging-java:0.6.10` to `0.6.11`
* Updated `com.exasol:virtual-schema-common-jdbc:11.0.1` to `11.0.2`
* Updated `com.exasol:virtual-schema-shared-integration-tests:2.2.4` to `2.2.5`
* Updated `com.google.protobuf:protobuf-java:3.23.4` to `3.24.3`
* Updated `com.mysql:mysql-connector-j:8.0.33` to `8.1.0`
* Updated `org.junit.jupiter:junit-jupiter:5.9.3` to `5.10.0`
* Updated `org.mockito:mockito-junit-jupiter:5.4.0` to `5.5.0`
* Added `org.slf4j:slf4j-jdk14:2.0.9`
* Updated `org.testcontainers:junit-jupiter:1.18.3` to `1.19.0`
* Updated `org.testcontainers:mysql:1.18.3` to `1.19.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.9` to `2.9.12`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
