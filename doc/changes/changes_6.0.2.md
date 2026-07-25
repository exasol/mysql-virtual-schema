# Virtual Schema for MySQL 6.0.2, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-9563, CVE-2026-60586, CVE-2026-60623, CVE-2026-60624, CVE-2026-61082

## Summary

This release fixes the following 5 vulnerabilities:

### CVE-2026-9563 (CWE-400) in dependency `org.eclipse.parsson:parsson:jar:1.1.7:test`
In Eclipse Parsson published Maven Central artifacts before version 1.1.8, the JSON parser did not enforce a default maximum on the number of characters consumed while parsing a single JSON document. Applications that parse attacker- controlled JSON can be forced to consume excessive CPU and memory by processing very large documents, including large arrays, objects, strings, numbers, whitespace, or nested structures, resulting in a denial of service. Eclipse Parsson 1.1.8 introduces a configurable maximum parsing limit with a default limit of 15 million parser-consumed characters.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-9563?component-type=maven&component-name=org.eclipse.parsson%2Fparsson&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-9563
* https://github.com/eclipse-ee4j/parsson/pull/169
* https://gitlab.eclipse.org/security/vulnerability-reports/-/work_items/444

### CVE-2026-60586 (CWE-200) in dependency `com.mysql:mysql-connector-j:jar:9.7.0:test`
Vulnerability in the MySQL Connectors product of Oracle MySQL (component: Connector/J).  Supported versions that are affected are 9.7.0-9.7.1. Easily exploitable vulnerability allows low privileged attacker with network access via multiple protocols to compromise MySQL Connectors.  While the vulnerability is in MySQL Connectors, attacks may significantly impact additional products (scope change).  Successful attacks of this vulnerability can result in  unauthorized access to critical data or complete access to all MySQL Connectors accessible data. CVSS 3.1 Base Score 7.7 (Confidentiality impacts).  CVSS Vector: (CVSS:3.1/AV:N/AC:L/PR:L/UI:N/S:C/C:H/I:N/A:N).
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-60586?component-type=maven&component-name=com.mysql%2Fmysql-connector-j&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-60586
* https://www.oracle.com/security-alerts/cpujul2026.html

### CVE-2026-60623 (CWE-668) in dependency `com.mysql:mysql-connector-j:jar:9.7.0:test`
Vulnerability in the MySQL Connectors product of Oracle MySQL (component: Connector/J).  Supported versions that are affected are 9.7.0-9.7.1. Difficult to exploit vulnerability allows low privileged attacker with network access via multiple protocols to compromise MySQL Connectors.  Successful attacks of this vulnerability can result in  unauthorized creation, deletion or modification access to critical data or all MySQL Connectors accessible data as well as  unauthorized access to critical data or complete access to all MySQL Connectors accessible data and unauthorized ability to cause a partial denial of service (partial DOS) of MySQL Connectors. CVSS 3.1 Base Score 7.1 (Confidentiality, Integrity and Availability impacts).  CVSS Vector: (CVSS:3.1/AV:N/AC:H/PR:L/UI:N/S:U/C:H/I:H/A:L).
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-60623?component-type=maven&component-name=com.mysql%2Fmysql-connector-j&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-60623
* https://www.oracle.com/security-alerts/cpujul2026.html

### CVE-2026-60624 (CWE-400) in dependency `com.mysql:mysql-connector-j:jar:9.7.0:test`
Vulnerability in the MySQL Connectors product of Oracle MySQL (component: Connector/J).  Supported versions that are affected are 9.7.0-9.7.1. Easily exploitable vulnerability allows unauthenticated attacker with network access via multiple protocols to compromise MySQL Connectors.  Successful attacks require human interaction from a person other than the attacker. Successful attacks of this vulnerability can result in unauthorized ability to cause a hang or frequently repeatable crash (complete DOS) of MySQL Connectors. CVSS 3.1 Base Score 6.5 (Availability impacts).  CVSS Vector: (CVSS:3.1/AV:N/AC:L/PR:N/UI:R/S:U/C:N/I:N/A:H).
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-60624?component-type=maven&component-name=com.mysql%2Fmysql-connector-j&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-60624
* https://www.oracle.com/security-alerts/cpujul2026.html

### CVE-2026-61082 (CWE-200) in dependency `com.mysql:mysql-connector-j:jar:9.7.0:test`
Vulnerability in the MySQL Connectors product of Oracle MySQL (component: Connector/J).  Supported versions that are affected are 9.7.0-9.7.1. Easily exploitable vulnerability allows unauthenticated attacker with network access via multiple protocols to compromise MySQL Connectors.  Successful attacks require human interaction from a person other than the attacker. Successful attacks of this vulnerability can result in  unauthorized access to critical data or complete access to all MySQL Connectors accessible data. CVSS 3.1 Base Score 6.5 (Confidentiality impacts).  CVSS Vector: (CVSS:3.1/AV:N/AC:L/PR:N/UI:R/S:U/C:H/I:N/A:N).
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-61082?component-type=maven&component-name=com.mysql%2Fmysql-connector-j&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-61082
* https://www.oracle.com/security-alerts/cpujul2026.html

## Security

* #62: Fixed vulnerability CVE-2026-9563 in dependency `org.eclipse.parsson:parsson:jar:1.1.7:test`
* #63: Fixed vulnerability CVE-2026-60586 in dependency `com.mysql:mysql-connector-j:jar:9.7.0:test`
* #64: Fixed vulnerability CVE-2026-60623 in dependency `com.mysql:mysql-connector-j:jar:9.7.0:test`
* #65: Fixed vulnerability CVE-2026-60624 in dependency `com.mysql:mysql-connector-j:jar:9.7.0:test`
* #66: Fixed vulnerability CVE-2026-61082 in dependency `com.mysql:mysql-connector-j:jar:9.7.0:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-jdbc:14.0.2` to `14.0.4`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.2.3` to `7.3.0`
* Updated `com.exasol:test-db-builder-java:4.0.0` to `4.0.1`
* Updated `com.exasol:udf-debugging-java:0.6.18` to `0.6.20`
* Updated `com.exasol:virtual-schema-common-jdbc:14.0.2` to `14.0.4`
* Updated `org.junit.jupiter:junit-jupiter-api:5.14.3` to `6.1.2`
* Updated `org.slf4j:slf4j-jdk14:2.0.17` to `2.0.18`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.5.2` to `5.7.4`
