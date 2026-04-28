package com.exasol.adapter.dialects.mysql;

import com.exasol.adapter.dialects.*;
import com.exasol.logging.VersionCollector;

/**
 * Factory for the MySql SQL dialect.
 */
public class MySQLSqlDialectFactory implements SqlDialectFactory {
    @Override
    public SqlDialect createSqlDialect(final JDBCAdapterContext context) {
        return new MySQLSqlDialect(context);
    }

    @Override
    public String getSqlDialectName() {
        return MySQLSqlDialect.NAME;
    }

    @Override
    public String getSqlDialectVersion() {
        final VersionCollector versionCollector = new VersionCollector(
                "META-INF/maven/com.exasol/mysql-virtual-schema/pom.properties");
        return versionCollector.getVersionNumber();
    }

    @Override
    public String getAdapterProjectShortTag() {
        return "VSMYSQL";
    }
}