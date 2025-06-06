# Virtual Schema for MySQL 5.1.0, released 2025-06-06

Code name: Timestamp precision

## Summary

This release improves the support for columns types with fractional second precision (FSP), i.e. TIME, DATETIME and
TIMESTAMP. The specified FSP will be maintained in Exasol newer versions (>= 8.32.0)

This release also contains a security update. We updated the dependencies of the project to fix transitive security issues.

We also added an exception for the OSSIndex for CVE-2024-55551, which is a false positive in Exasol's JDBC driver.
This issue has been fixed quite a while back now, but the OSSIndex unfortunately does not contain the fix version of 24.2.1 (2024-12-10) set.

## Features

* #48: TS(9) support in MySQL VS

## Security

* #53: Fix CVE-2024-55551 in com.exasol:exasol-jdbc:jar:24.1.1:test

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:12.0.0` to `13.0.0`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.1` to `7.1.5`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.1`
* Updated `com.exasol:test-db-builder-java:3.5.2` to `3.6.1`
* Updated `com.exasol:udf-debugging-java:0.6.13` to `0.6.16`
* Updated `com.exasol:virtual-schema-common-jdbc:12.0.0` to `13.0.0`
* Updated `com.exasol:virtual-schema-shared-integration-tests:3.0.0` to `3.0.1`
* Removed `com.google.protobuf:protobuf-java:4.28.2`
* Updated `com.mysql:mysql-connector-j:9.0.0` to `9.3.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.12` to `0.8.13`
* Added `org.junit.jupiter:junit-jupiter-api:5.13.0`
* Removed `org.junit.jupiter:junit-jupiter:5.11.0`
* Updated `org.mockito:mockito-junit-jupiter:5.13.0` to `5.18.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.16` to `2.0.17`
* Updated `org.testcontainers:junit-jupiter:1.20.1` to `1.21.1`
* Updated `org.testcontainers:mysql:1.20.1` to `1.21.1`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.2` to `0.4.3`
* Updated `com.exasol:project-keeper-maven-plugin:4.3.3` to `5.1.0`
* Added `com.exasol:quality-summarizer-maven-plugin:0.2.0`
* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.16`
* Added `org.apache.maven.plugins:maven-artifact-plugin:3.6.0`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.2.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.1` to `3.8.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.5` to `3.5.3`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.2` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.1` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.12.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.3`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.18.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.1.0.4751`
