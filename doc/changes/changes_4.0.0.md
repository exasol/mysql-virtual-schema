# Virtual Schema for MySQL 4.0.0, released 2022-09-30

Code name: Enhanced Data Type Detection for Result Sets and Fixed Vulnerabilities in Dependencies

## Summary

Starting with version 7.1.14 Exasol database uses the capabilities reported by each virtual schema to provide select list data types for each push down request. Based on this information the JDBC virtual schemas no longer need to infer the data types of the result set by inspecting its values. Instead the JDBC virtual schemas can now use the information provided by the database.

This release provides enhanced data type detection for result sets by updating `virtual-schema-common-jdbc` to version [10.0.1](https://github.com/exasol/virtual-schema-common-jdbc/releases/tag/10.0.1).

The following table shows the changed return type for some operations:

| Operation | Return type up to version 3.0.0 | Return type for version 4.0.0  |
|-----------|---------------------------------|--------------------------------|
| Comparison, e.g. `x < 60` | `DECIMAL`| `BOOLEAN` |
| `AVG(DECIMAL)`            | `DECIMAL`| `DOUBLE`  |
| `DIV(DECIMAL)`            | `DECIMAL(19,0)`| `DECIMAL(10,0)`, i.e. `BIGINT` |

Additionally this release fixes vulnerabilities CVE-2022-38751 and CVE-2022-38752 reported for transitive dependency snakeyaml required by

## Bug Fixes

* #21: Fixed vulnerabilities in dependencies.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:9.0.5` to `10.0.1`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.1.1` to `6.2.0`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.1` to `1.5.2`
* Updated `com.exasol:test-db-builder-java:3.3.2` to `3.3.4`
* Updated `com.exasol:udf-debugging-java:0.6.2` to `0.6.4`
* Updated `com.exasol:virtual-schema-common-jdbc:9.0.5` to `10.0.1`
* Updated `com.exasol:virtual-schema-shared-integration-tests:2.2.0` to `2.2.2`
* Updated `mysql:mysql-connector-java:8.0.29` to `8.0.30`
* Updated `org.junit.jupiter:junit-jupiter:5.8.2` to `5.9.1`
* Updated `org.mockito:mockito-junit-jupiter:4.6.1` to `4.8.0`
* Updated `org.testcontainers:junit-jupiter:1.17.2` to `1.17.3`
* Updated `org.testcontainers:mysql:1.17.2` to `1.17.3`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.4.6` to `2.8.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
