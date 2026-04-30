# Virtual Schema for MySQL 6.0.1, released 2026-04-30

Code name: Fix telemetry endpoint

## Summary

Release 6.0.0 added anonymous feature-usage telemetry via `telemetry-java`. See the [documentation](https://github.com/exasol/telemetry-java/blob/main/doc/app-user-guide.md) for details on collected data and opt-out behavior.

This release updates the telemetry endpoint to the correct URL.

## Bugfixes

* #60: Fix telemetry endpoint

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.0.1` to `14.0.2`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.0.1` to `14.0.2`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.5.1` to `5.5.2`
