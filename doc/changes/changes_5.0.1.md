# Virtual Schema for MySQL 5.0.1, released 2024-03-12

Code name: Fixed vulnerabilities CVE-2024-25710 and CVE-2024-26308 in test dependencies

## Summary

This is a security release in which we updated test dependency `com.exasol:exasol-test-setup-abstraction-java` to fix vulnerabilities CVE-2024-25710 and CVE-2024-26308 in its transitive dependencies.

## Security

* #45: Fixed vulnerability CVE-2024-25710
* #46: Fixed vulnerability CVE-2024-26308

## Dependency Updates

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.2` to `7.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.1` to `1.6.5`
* Updated `com.exasol:test-db-builder-java:3.5.1` to `3.5.2`
* Updated `com.exasol:udf-debugging-java:0.6.11` to `0.6.12`
* Updated `com.exasol:virtual-schema-shared-integration-tests:2.2.5` to `3.0.0`
* Updated `com.google.protobuf:protobuf-java:3.24.3` to `3.25.3`
* Updated `com.mysql:mysql-connector-j:8.2.0` to `8.3.0`
* Updated `org.junit.jupiter:junit-jupiter:5.10.0` to `5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.5.0` to `5.11.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.9` to `2.0.12`
* Updated `org.testcontainers:junit-jupiter:1.19.0` to `1.19.7`
* Updated `org.testcontainers:mysql:1.19.0` to `1.19.7`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.1` to `2.0.1`
* Updated `com.exasol:project-keeper-maven-plugin:3.0.0` to `4.2.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.11.0` to `3.12.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.3` to `3.2.5`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.3` to `3.2.5`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.5.0` to `1.6.0`
