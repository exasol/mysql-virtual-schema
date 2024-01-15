# Virtual Schema for MySQL 5.0.0, released 2024-01-15

Code name: Changes related to Exasol V8 / importing text datatypes.

## Summary

The behaviour when it comes to character sets is now simplified,
The target char set is now always UTF-8.
The IMPORT_DATA_TYPES property (and value FROM_RESULT_SET) are now deprecated (change in vs-common-jdbc).

## Refactoring

* #37: Update tests to V8 VSMYSQL / Update to vsjdbc 12.0.0

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:11.0.2` to `12.0.0`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:11.0.2` to `12.0.0`
* Updated `com.mysql:mysql-connector-j:8.1.0` to `8.2.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.10` to `0.8.11`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.12` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`
