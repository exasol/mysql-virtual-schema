# Virtual Schema for MySQL 4.0.1, released 2022-11-24

Code name: Improved documentation

## Summary

In release 4.0.1 we improved the installation instructions in the user guide, removed an old file that was left over from when the VS used Lombok and updated dependencies to fix vulnerabilities.

## Bugfixes

* #23: Fixed CVE-2022-3171 reported for `com.google.protobuf:protobuf-java` by updating `com.mysql:mysql-connector-j`.

## Dependency Updates

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.2.0` to `6.3.1`
* Updated `com.exasol:test-db-builder-java:3.4.0` to `3.4.1`
* Updated `com.exasol:udf-debugging-java:0.6.4` to `0.6.5`
* Updated `com.google.protobuf:protobuf-java:3.21.8` to `3.21.9`
* Updated `org.mockito:mockito-junit-jupiter:4.8.1` to `4.9.0`
* Updated `org.testcontainers:junit-jupiter:1.17.5` to `1.17.6`
* Updated `org.testcontainers:mysql:1.17.5` to `1.17.6`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.0` to `0.4.2`
* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.2` to `1.2.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.8.0` to `2.9.1`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.15` to `0.16`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.3.0` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.2` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.2.7` to `1.3.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.10.0` to `2.13.0`
