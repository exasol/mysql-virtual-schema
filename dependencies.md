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
| [Hamcrest][2]                                   | [BSD License 3][3]                                                     |
| [JUnit Jupiter (Aggregator)][4]                 | [Eclipse Public License v2.0][5]                                       |
| [mockito-junit-jupiter][6]                      | [The MIT License][7]                                                   |
| [Test containers for Exasol on Docker][8]       | [MIT License][9]                                                       |
| [Testcontainers :: JUnit Jupiter Extension][10] | [MIT][11]                                                              |
| [Testcontainers :: JDBC :: MySQL][10]           | [MIT][11]                                                              |
| [MySQL Connector/J][12]                         | The GNU General Public License, v2 with Universal FOSS Exception, v1.0 |
| [Protocol Buffers [Core]][13]                   | [BSD-3-Clause][14]                                                     |
| [Test Database Builder for Java][15]            | [MIT License][16]                                                      |
| [Matcher for SQL Result Sets][17]               | [MIT License][18]                                                      |
| [virtual-schema-shared-integration-tests][19]   | [MIT License][20]                                                      |
| [udf-debugging-java][21]                        | [MIT License][22]                                                      |
| [JaCoCo :: Agent][23]                           | [Eclipse Public License 2.0][24]                                       |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][25]                       | [GNU LGPL 3][26]                               |
| [Apache Maven Compiler Plugin][27]                      | [Apache-2.0][28]                               |
| [Apache Maven Enforcer Plugin][29]                      | [Apache-2.0][28]                               |
| [Maven Flatten Plugin][30]                              | [Apache Software Licenese][28]                 |
| [Project keeper maven plugin][31]                       | [The MIT License][32]                          |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][33] | [ASL2][34]                                     |
| [Maven Surefire Plugin][35]                             | [Apache-2.0][28]                               |
| [Versions Maven Plugin][36]                             | [Apache License, Version 2.0][28]              |
| [duplicate-finder-maven-plugin Maven Mojo][37]          | [Apache License 2.0][38]                       |
| [Apache Maven Assembly Plugin][39]                      | [Apache-2.0][28]                               |
| [Apache Maven JAR Plugin][40]                           | [Apache License, Version 2.0][28]              |
| [Artifact reference checker and unifier][41]            | [MIT License][42]                              |
| [Apache Maven Dependency Plugin][43]                    | [Apache-2.0][28]                               |
| [Maven Failsafe Plugin][44]                             | [Apache-2.0][28]                               |
| [JaCoCo :: Maven Plugin][45]                            | [Eclipse Public License 2.0][24]               |
| [error-code-crawler-maven-plugin][46]                   | [MIT License][47]                              |
| [Reproducible Build Maven Plugin][48]                   | [Apache 2.0][34]                               |
| [Maven Clean Plugin][49]                                | [The Apache Software License, Version 2.0][34] |
| [Maven Resources Plugin][50]                            | [The Apache Software License, Version 2.0][34] |
| [Maven Install Plugin][51]                              | [The Apache Software License, Version 2.0][34] |
| [Maven Deploy Plugin][52]                               | [The Apache Software License, Version 2.0][34] |
| [Maven Site Plugin 3][53]                               | [The Apache Software License, Version 2.0][34] |

[0]: https://github.com/exasol/virtual-schema-common-jdbc/
[1]: https://github.com/exasol/virtual-schema-common-jdbc/blob/main/LICENSE
[2]: http://hamcrest.org/JavaHamcrest/
[3]: http://opensource.org/licenses/BSD-3-Clause
[4]: https://junit.org/junit5/
[5]: https://www.eclipse.org/legal/epl-v20.html
[6]: https://github.com/mockito/mockito
[7]: https://github.com/mockito/mockito/blob/main/LICENSE
[8]: https://github.com/exasol/exasol-testcontainers/
[9]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[10]: https://testcontainers.org
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
[23]: https://www.eclemma.org/jacoco/index.html
[24]: https://www.eclipse.org/legal/epl-2.0/
[25]: http://sonarsource.github.io/sonar-scanner-maven/
[26]: http://www.gnu.org/licenses/lgpl.txt
[27]: https://maven.apache.org/plugins/maven-compiler-plugin/
[28]: https://www.apache.org/licenses/LICENSE-2.0.txt
[29]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[30]: https://www.mojohaus.org/flatten-maven-plugin/
[31]: https://github.com/exasol/project-keeper/
[32]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[33]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[34]: http://www.apache.org/licenses/LICENSE-2.0.txt
[35]: https://maven.apache.org/surefire/maven-surefire-plugin/
[36]: https://www.mojohaus.org/versions/versions-maven-plugin/
[37]: https://basepom.github.io/duplicate-finder-maven-plugin
[38]: http://www.apache.org/licenses/LICENSE-2.0.html
[39]: https://maven.apache.org/plugins/maven-assembly-plugin/
[40]: https://maven.apache.org/plugins/maven-jar-plugin/
[41]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[42]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[43]: https://maven.apache.org/plugins/maven-dependency-plugin/
[44]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[45]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[46]: https://github.com/exasol/error-code-crawler-maven-plugin/
[47]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[48]: http://zlika.github.io/reproducible-build-maven-plugin
[49]: http://maven.apache.org/plugins/maven-clean-plugin/
[50]: http://maven.apache.org/plugins/maven-resources-plugin/
[51]: http://maven.apache.org/plugins/maven-install-plugin/
[52]: http://maven.apache.org/plugins/maven-deploy-plugin/
[53]: http://maven.apache.org/plugins/maven-site-plugin/
