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
| [JUnit Jupiter API][4]                          | [Eclipse Public License v2.0][5]                                       |
| [mockito-junit-jupiter][6]                      | [MIT][7]                                                               |
| [Test containers for Exasol on Docker][8]       | [MIT License][9]                                                       |
| [Testcontainers :: JUnit Jupiter Extension][10] | [MIT][11]                                                              |
| [Testcontainers :: JDBC :: MySQL][10]           | [MIT][11]                                                              |
| [MySQL Connector/J][12]                         | The GNU General Public License, v2 with Universal FOSS Exception, v1.0 |
| [Test Database Builder for Java][13]            | [MIT License][14]                                                      |
| [Matcher for SQL Result Sets][15]               | [MIT License][16]                                                      |
| [virtual-schema-shared-integration-tests][17]   | [MIT License][18]                                                      |
| [udf-debugging-java][19]                        | [MIT License][20]                                                      |
| [SLF4J JDK14 Provider][21]                      | [MIT][22]                                                              |
| [JaCoCo :: Agent][23]                           | [EPL-2.0][24]                                                          |

## Plugin Dependencies

| Dependency                                              | License                                     |
| ------------------------------------------------------- | ------------------------------------------- |
| [SonarQube Scanner for Maven][25]                       | [GNU LGPL 3][26]                            |
| [Apache Maven Toolchains Plugin][27]                    | [Apache-2.0][28]                            |
| [Apache Maven Compiler Plugin][29]                      | [Apache-2.0][28]                            |
| [Apache Maven Enforcer Plugin][30]                      | [Apache-2.0][28]                            |
| [Maven Flatten Plugin][31]                              | [Apache Software License][28]               |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][32] | [ASL2][33]                                  |
| [Maven Surefire Plugin][34]                             | [Apache-2.0][28]                            |
| [Versions Maven Plugin][35]                             | [Apache License, Version 2.0][28]           |
| [duplicate-finder-maven-plugin Maven Mojo][36]          | [Apache License 2.0][37]                    |
| [Apache Maven Artifact Plugin][38]                      | [Apache-2.0][28]                            |
| [Apache Maven Assembly Plugin][39]                      | [Apache-2.0][28]                            |
| [Apache Maven JAR Plugin][40]                           | [Apache-2.0][28]                            |
| [Artifact reference checker and unifier][41]            | [MIT License][42]                           |
| [Apache Maven Dependency Plugin][43]                    | [Apache-2.0][28]                            |
| [Project Keeper Maven plugin][44]                       | [The MIT License][45]                       |
| [Maven Failsafe Plugin][46]                             | [Apache-2.0][28]                            |
| [JaCoCo :: Maven Plugin][47]                            | [EPL-2.0][24]                               |
| [Quality Summarizer Maven Plugin][48]                   | [MIT License][49]                           |
| [error-code-crawler-maven-plugin][50]                   | [MIT License][51]                           |
| [Git Commit Id Maven Plugin][52]                        | [GNU Lesser General Public License 3.0][53] |
| [Apache Maven Clean Plugin][54]                         | [Apache-2.0][28]                            |
| [Apache Maven Resources Plugin][55]                     | [Apache-2.0][28]                            |
| [Apache Maven Install Plugin][56]                       | [Apache-2.0][28]                            |
| [Apache Maven Site Plugin][57]                          | [Apache-2.0][28]                            |

[0]: https://github.com/exasol/virtual-schema-common-jdbc/
[1]: https://github.com/exasol/virtual-schema-common-jdbc/blob/main/LICENSE
[2]: http://hamcrest.org/JavaHamcrest/
[3]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[4]: https://junit.org/
[5]: https://www.eclipse.org/legal/epl-v20.html
[6]: https://github.com/mockito/mockito
[7]: https://opensource.org/licenses/MIT
[8]: https://github.com/exasol/exasol-testcontainers/
[9]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[10]: https://java.testcontainers.org
[11]: http://opensource.org/licenses/MIT
[12]: http://dev.mysql.com/doc/connector-j/en/
[13]: https://github.com/exasol/test-db-builder-java/
[14]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[15]: https://github.com/exasol/hamcrest-resultset-matcher/
[16]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[17]: https://github.com/exasol/virtual-schema-shared-integration-tests/
[18]: https://github.com/exasol/virtual-schema-shared-integration-tests/blob/main/LICENSE
[19]: https://github.com/exasol/udf-debugging-java/
[20]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[21]: http://www.slf4j.org
[22]: https://opensource.org/license/mit
[23]: https://www.eclemma.org/jacoco/index.html
[24]: https://www.eclipse.org/legal/epl-2.0/
[25]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[26]: http://www.gnu.org/licenses/lgpl.txt
[27]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[28]: https://www.apache.org/licenses/LICENSE-2.0.txt
[29]: https://maven.apache.org/plugins/maven-compiler-plugin/
[30]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[31]: https://www.mojohaus.org/flatten-maven-plugin/
[32]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[33]: http://www.apache.org/licenses/LICENSE-2.0.txt
[34]: https://maven.apache.org/surefire/maven-surefire-plugin/
[35]: https://www.mojohaus.org/versions/versions-maven-plugin/
[36]: https://basepom.github.io/duplicate-finder-maven-plugin
[37]: http://www.apache.org/licenses/LICENSE-2.0.html
[38]: https://maven.apache.org/plugins/maven-artifact-plugin/
[39]: https://maven.apache.org/plugins/maven-assembly-plugin/
[40]: https://maven.apache.org/plugins/maven-jar-plugin/
[41]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[42]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[43]: https://maven.apache.org/plugins/maven-dependency-plugin/
[44]: https://github.com/exasol/project-keeper/
[45]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[46]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[47]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[48]: https://github.com/exasol/quality-summarizer-maven-plugin/
[49]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[50]: https://github.com/exasol/error-code-crawler-maven-plugin/
[51]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[52]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[53]: http://www.gnu.org/licenses/lgpl-3.0.txt
[54]: https://maven.apache.org/plugins/maven-clean-plugin/
[55]: https://maven.apache.org/plugins/maven-resources-plugin/
[56]: https://maven.apache.org/plugins/maven-install-plugin/
[57]: https://maven.apache.org/plugins/maven-site-plugin/
