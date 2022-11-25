# Virtual Schema for MySQL 4.1.0, released 2022-??-??

Code name: Configurable datatype detection

## Summary

Virtual-schema-common-jdbc issue [#120](https://github.com/exasol/virtual-schema-common-jdbc/issues/120) introduced enhanced detection for data types of result sets based on metadata of the connection and calculated by Exasol database available with Exasol 7.1.14, Exasol 8.6.0 and above.

But bug #26 showed problems with MySQL for character set `latin1` in case of data values not strictly ASCII, e.g. German umlaut "Ü".

The current releases therefore introduces an additional parameter for MySQL Virtual Schema to configure the data type detection. For details please see the User Guide.

## Features

* #32: Introduced an additional parameter for MySQL Virtual Schema to configure the data type detection.

## Bugfixes

* #26: Provided a workaround for using MySQL database with character set latin1 and characters not strictly ASCII.

## Dependency Updates

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.8.0` to `2.9.1`
