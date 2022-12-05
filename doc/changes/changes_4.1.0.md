# Virtual Schema for MySQL 4.1.0, released 2022-12-05

Code name: Configurable datatype detection

## Summary

Virtual-schema-common-jdbc version 10.0.0 introduced enhanced detection for data types of result sets.

Unfortunately with the new algorithm compatibility problems with the source database can happen under the following circumstances:

* data type `CHAR` or `VARCHAR`
* 8-bit character sets with encodings like `latin1` or `ISO-8859-1`
* characters being not strictly ASCII, e.g. German umlaut "Ü"

The current release therefore uses an updated version of `virtual-schema-common-jdbc` with an additional adapter property to configure the data type detection.

For details please [Adapter Properties for JDBC-Based Virtual Schemas](https://github.com/exasol/virtual-schema-common-jdbc/blob/main/README.md#adapter-properties-for-jdbc-based-virtual-schemas).

## Bugfixes

* #26: Enabled to use MySQL database with character set `latin1` and characters not strictly ASCII.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:10.0.1` to `10.1.0`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.3.1` to `6.4.0`
* Updated `com.exasol:virtual-schema-common-jdbc:10.0.1` to `10.1.0`
