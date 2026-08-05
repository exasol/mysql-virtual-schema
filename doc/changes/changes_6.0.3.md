# Virtual Schema for MySQL 6.0.3, released 2026-08-05

Code name: Support legacy nullable metadata

## Summary

This release supports legacy nullable metadata.

## Bugfixes

* #68: Handle `null` in `isNullable` metadata after DB upgrade to 2025.1.12

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.0.4` to `14.0.5`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.0.4` to `14.0.5`
* Updated `com.exasol:virtual-schema-shared-integration-tests:3.0.2` to `3.0.3`
