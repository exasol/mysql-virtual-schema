# Virtual Schema for MySQL 7.0.0, released 2026-??-??

Code name: Fixed OW2 ASM vulnerabilities CVE-2026-110257 through CVE-2026-110270

## Summary

Fixed eight OW2 ASM vulnerabilities in test dependencies.

- Fixes #74
- Fixes #75
- Fixes #76
- Fixes #77
- Fixes #78
- Fixes #79
- Fixes #80
- Fixes #81

## Security

* #76: Fixed CVE-2026-110257 (uncontrolled memory allocation) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #77: Fixed CVE-2026-110258 (excessive memory allocation) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #78: Fixed CVE-2026-110259 (uncontrolled recursion) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #79: Fixed CVE-2026-110260 (excessive memory allocation) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #80: Fixed CVE-2026-110261 (CPU exhaustion) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #74: Fixed CVE-2026-110262 (excessive memory allocation) in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
* #81: Fixed CVE-2026-110263 (uncontrolled recursion) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #75: Fixed CVE-2026-110270 (uncontrolled memory allocation) in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.1.0` to `14.1.1`

### Test Dependency Updates

* Updated `com.exasol:udf-debugging-java:0.6.21` to `0.6.22`
* Updated `com.exasol:virtual-schema-common-jdbc:14.1.0` to `14.1.1`
* Updated `com.mysql:mysql-connector-j:26.7.0` to `26.10.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.15` to `0.8.16`
* Removed `org.junit.jupiter:junit-jupiter-api:5.14.4`
* Added `org.junit.jupiter:junit-jupiter:6.1.3`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.2` to `2.1.3`
* Updated `com.exasol:project-keeper-maven-plugin:5.7.7` to `5.8.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.6.0` to `3.6.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.15` to `0.8.16`
