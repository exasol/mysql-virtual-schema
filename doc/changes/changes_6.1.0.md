# Virtual Schema for MySQL 6.1.0, released 2026-10-16

Code name: Scalar Functions

## Summary

Enabled the MySQL scalar-function integration tests and extended scalar-function compatibility. Date-add functions, standard mathematical functions, date-part functions, and several MySQL-specific result-type differences are now covered and validated.

* Added support for `ADD_DAYS`, `ADD_HOURS`, `ADD_MINUTES`, `ADD_MONTHS`, `ADD_SECONDS`, `ADD_WEEKS`, and `ADD_YEARS`. These functions support DATE or TIMESTAMP values with integer offsets.
* Fixed scalar-function translation for:
  * `CAST` to character types, translated to MySQL `CHAR` casts;
  * `WEEK`, translated with ISO week-numbering mode;
  * one-argument `ROUND` on floating-point values, preserving Exasol's half-up behaviour;
  * `YEAR`, cast to a signed integer so its result type can be mapped by the virtual schema.

## Limitations

* Date-add compatibility is supported only for integer offsets. Compatibility with `DOUBLE`, `DECIMAL`, and `BOOLEAN` offsets is not guaranteed because MySQL and Exasol coerce them differently. These combinations are skipped by the integration tests.
* Compatibility is not guaranteed for `ROUND` with a floating-point precision argument or `SPACE` with a floating-point length: MySQL truncates fractional values while Exasol rounds them. These combinations are skipped by the integration tests.
* `USER` is not compatibility-supported because it reports the remote MySQL account rather than the querying Exasol user. It is skipped by the integration tests.
* The following capabilities are not supported where their semantics are incompatible:
  * `INSERT` because boolean/timestamp rendering and position/length semantics differ;
  * `REGEXP_INSTR`, `REGEXP_REPLACE`, and `REGEXP_SUBSTR` because regular-expression syntax and semantics differ;
  * string functions including `CONCAT`, `REPLACE`, `LENGTH`, `LOWER`, `UPPER`, `TRIM`, `LPAD`, `RPAD`, `LOCATE`, and `INSTR` because string rendering or coercion differs;
  * `LEAST` and `GREATEST` because mixed-type comparison precedence differs;
  * `REGEXP_COUNT` because MySQL does not support it; and
  * `SYSTIMESTAMP` because MySQL and Exasol test environments use different timezone configuration.

**Breaking Change:** Exasol version 8 is no longer covered by integration tests due to issues with timestamps with timezone.

## Features

* #72: Restore scalar function integration tests

## Security

* #70: Fixed vulnerability CVE-2026-86231 in dependency `com.github.mwiede:jsch:jar:2.28.5:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.0.5` to `14.1.0`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:8.0.1` to `8.0.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.3` to `1.7.4`
* Updated `com.exasol:test-db-builder-java:4.0.2` to `4.0.3`
* Updated `com.exasol:udf-debugging-java:0.6.20` to `0.6.21`
* Updated `com.exasol:virtual-schema-common-jdbc:14.0.5` to `14.1.0`
* Updated `com.exasol:virtual-schema-shared-integration-tests:3.0.3` to `3.1.0`
* Updated `com.mysql:mysql-connector-j:9.6.0` to `26.7.0`
* Updated `org.mockito:mockito-junit-jupiter:5.23.0` to `5.24.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.20`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.7.7`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.23.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
