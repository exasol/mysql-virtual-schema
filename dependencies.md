<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                      | License          |
| ------------------------------- | ---------------- |
| [Virtual Schema Common JDBC][0] | [MIT License][1] |

## Test Dependencies

| Dependency                                      | License                                                                |
| ----------------------------------------------- | ---------------------------------------------------------------------- |
| [Virtual Schema Common JDBC][0]                 | [MIT License][1]                                                       |
| [Hamcrest][2]                                   | [BSD-3-Clause][3]                                                      |
| [JUnit Jupiter (Aggregator)][4]                 | [Eclipse Public License v2.0][5]                                       |
| [mockito-junit-jupiter][6]                      | [MIT][7]                                                               |
| [Test containers for Exasol on Docker][8]       | [MIT License][9]                                                       |
| [Testcontainers :: JUnit Jupiter Extension][10] | [MIT][11]                                                              |
| [Testcontainers :: JDBC :: MySQL][10]           | [MIT][11]                                                              |
| [MySQL Connector/J][12]                         | The GNU General Public License, v2 with Universal FOSS Exception, v1.0 |
| [Protocol Buffers [Core]][13]                   | [BSD-3-Clause][14]                                                     |
| [Test Database Builder for Java][15]            | [MIT License][16]                                                      |
| [Matcher for SQL Result Sets][17]               | [MIT License][18]                                                      |
| [virtual-schema-shared-integration-tests][19]   | [MIT License][20]                                                      |
| [udf-debugging-java][21]                        | [MIT License][22]                                                      |
| [SLF4J JDK14 Provider][23]                      | [MIT License][24]                                                      |
| [JaCoCo :: Agent][25]                           | [EPL-2.0][26]                                                          |

## Plugin Dependencies

| Dependency                                              | License                                     |
| ------------------------------------------------------- | ------------------------------------------- |
| [Apache Maven Clean Plugin][27]                         | [Apache-2.0][28]                            |
| [Apache Maven Install Plugin][29]                       | [Apache-2.0][28]                            |
| [Apache Maven Resources Plugin][30]                     | [Apache-2.0][28]                            |
| [Apache Maven Site Plugin][31]                          | [Apache-2.0][28]                            |
| [SonarQube Scanner for Maven][32]                       | [GNU LGPL 3][33]                            |
| [Apache Maven Toolchains Plugin][34]                    | [Apache-2.0][28]                            |
| [Apache Maven Compiler Plugin][35]                      | [Apache-2.0][28]                            |
| [Apache Maven Enforcer Plugin][36]                      | [Apache-2.0][28]                            |
| [Maven Flatten Plugin][37]                              | [Apache Software Licenese][28]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][38] | [ASL2][39]                                  |
| [Maven Surefire Plugin][40]                             | [Apache-2.0][28]                            |
| [Versions Maven Plugin][41]                             | [Apache License, Version 2.0][28]           |
| [duplicate-finder-maven-plugin Maven Mojo][42]          | [Apache License 2.0][43]                    |
| [Apache Maven Artifact Plugin][44]                      | [Apache-2.0][28]                            |
| [Apache Maven Assembly Plugin][45]                      | [Apache-2.0][28]                            |
| [Apache Maven JAR Plugin][46]                           | [Apache-2.0][28]                            |
| [Artifact reference checker and unifier][47]            | [MIT License][48]                           |
| [Apache Maven Dependency Plugin][49]                    | [Apache-2.0][28]                            |
| [Project Keeper Maven plugin][50]                       | [The MIT License][51]                       |
| [Maven Failsafe Plugin][52]                             | [Apache-2.0][28]                            |
| [JaCoCo :: Maven Plugin][53]                            | [EPL-2.0][26]                               |
| [Quality Summarizer Maven Plugin][54]                   | [MIT License][55]                           |
| [error-code-crawler-maven-plugin][56]                   | [MIT License][57]                           |
| [Git Commit Id Maven Plugin][58]                        | [GNU Lesser General Public License 3.0][59] |

[0]: https://github.com/exasol/virtual-schema-common-jdbc/
[1]: https://github.com/exasol/virtual-schema-common-jdbc/blob/main/LICENSE
[2]: http://hamcrest.org/JavaHamcrest/
[3]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[4]: https://junit.org/junit5/
[5]: https://www.eclipse.org/legal/epl-v20.html
[6]: https://github.com/mockito/mockito
[7]: https://opensource.org/licenses/MIT
[8]: https://github.com/exasol/exasol-testcontainers/
[9]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[10]: https://java.testcontainers.org
[11]: http://opensource.org/licenses/MIT
[12]: http://dev.mysql.com/doc/connector-j/en/
[13]: https://developers.google.com/protocol-buffers
[14]: https://opensource.org/licenses/BSD-3-Clause
[15]: https://github.com/exasol/test-db-builder-java/
[16]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[17]: https://github.com/exasol/hamcrest-resultset-matcher/
[18]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[19]: https://github.com/exasol/virtual-schema-shared-integration-tests/
[20]: https://github.com/exasol/virtual-schema-shared-integration-tests/blob/main/LICENSE
[21]: https://github.com/exasol/udf-debugging-java/
[22]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[23]: http://www.slf4j.org
[24]: http://www.opensource.org/licenses/mit-license.php
[25]: https://www.eclemma.org/jacoco/index.html
[26]: https://www.eclipse.org/legal/epl-2.0/
[27]: https://maven.apache.org/plugins/maven-clean-plugin/
[28]: https://www.apache.org/licenses/LICENSE-2.0.txt
[29]: https://maven.apache.org/plugins/maven-install-plugin/
[30]: https://maven.apache.org/plugins/maven-resources-plugin/
[31]: https://maven.apache.org/plugins/maven-site-plugin/
[32]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-scanner-maven/sonar-maven-plugin
[33]: http://www.gnu.org/licenses/lgpl.txt
[34]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[35]: https://maven.apache.org/plugins/maven-compiler-plugin/
[36]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[37]: https://www.mojohaus.org/flatten-maven-plugin/
[38]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[39]: http://www.apache.org/licenses/LICENSE-2.0.txt
[40]: https://maven.apache.org/surefire/maven-surefire-plugin/
[41]: https://www.mojohaus.org/versions/versions-maven-plugin/
[42]: https://basepom.github.io/duplicate-finder-maven-plugin
[43]: http://www.apache.org/licenses/LICENSE-2.0.html
[44]: https://maven.apache.org/plugins/maven-artifact-plugin/
[45]: https://maven.apache.org/plugins/maven-assembly-plugin/
[46]: https://maven.apache.org/plugins/maven-jar-plugin/
[47]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[48]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[49]: https://maven.apache.org/plugins/maven-dependency-plugin/
[50]: https://github.com/exasol/project-keeper/
[51]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[52]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[53]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[54]: https://github.com/exasol/quality-summarizer-maven-plugin/
[55]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[56]: https://github.com/exasol/error-code-crawler-maven-plugin/
[57]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[58]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[59]: http://www.gnu.org/licenses/lgpl-3.0.txt
